package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.stripe.model.PaymentIntent;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.*;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.bill.Bill;
import com.zbkj.common.model.bill.MerchantBill;
import com.zbkj.common.model.coupon.StoreCouponUser;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreOrderInfo;
import com.zbkj.common.model.order.StoreOrderProfitSharing;
import com.zbkj.common.model.product.StoreProductCoupon;
import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserBill;
import com.zbkj.common.request.OrderPayRequest;
import com.zbkj.common.request.PayPalResultRequest;
import com.zbkj.common.response.OrderPayResultResponse;
import com.zbkj.common.response.PayMethodResponse;
import com.zbkj.common.response.PayPalResultResponse;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.common.utils.RequestUtil;
import com.zbkj.common.vo.MyRecord;
import com.zbkj.service.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * OrderPayService 实现类
 * +----------------------------------------------------------------------
 * | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 * +----------------------------------------------------------------------
 * | Copyright (c) 2016~2023 https://www.crmeb.com All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 * +----------------------------------------------------------------------
 * | Author: CRMEB Team <admin@crmeb.com>
 * +----------------------------------------------------------------------
 */
@Service
public class OrderPayServiceImpl implements OrderPayService {
    private static final Logger logger = LoggerFactory.getLogger(OrderPayServiceImpl.class);

    @Autowired
    private StoreOrderService storeOrderService;
    @Autowired
    private StoreOrderStatusService storeOrderStatusService;
    @Autowired
    private StoreOrderInfoService storeOrderInfoService;
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreProductCouponService storeProductCouponService;
    @Autowired
    private StoreCouponUserService storeCouponUserService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StoreCouponService storeCouponService;
    @Autowired
    private PaypalService paypalService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MasterOrderService masterOrderService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private StoreOrderProfitSharingService profitSharingService;
    @Autowired
    private BillService billService;
    @Autowired
    private MerchantBillService merchantBillService;
    @Autowired
    private StripeService stripeService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private CrmebConfig crmebConfig;

    /**
     * 支付成功处理
     *
     * @param masterOrder 订单
     */
    @Override
    public Boolean paySuccess(MasterOrder masterOrder) {

        User user = userService.getById(masterOrder.getUid());

        List<UserBill> billList = CollUtil.newArrayList();
        // 订单支付记录

        UserBill userBill = userBillInit(masterOrder, user);
        billList.add(userBill);
        // 更新用户下单数量
        user.setPayCount(user.getPayCount() + 1);
        // 订单分账
        List<StoreOrder> orderList = storeOrderService.getListByMasterNo(masterOrder.getOrderNo());
        List<StoreOrderProfitSharing> sharingList = orderList.stream().map(order -> {
            StoreOrderProfitSharing sharing = new StoreOrderProfitSharing();
            // 获取商户信息
            Merchant merchant = merchantService.getByIdException(order.getMerId());
            // 分账计算
            BigDecimal sharingFee = order.getPayPrice().multiply(new BigDecimal(merchant.getHandlingFee())).divide(new BigDecimal(100), 2, BigDecimal.ROUND_UP);
            BigDecimal merchantFee = order.getPayPrice().subtract(sharingFee);
            sharing.setMerOrderNo(order.getOrderNo());
            sharing.setMerId(order.getMerId());
            sharing.setProfitSharingPrice(sharingFee);
            sharing.setProfitSharingMerPrice(merchantFee);
            sharing.setProfitSharingTime(DateUtil.date());
            sharing.setStatus(OrderConstants.ORDER_PROFIT_SHARING_STATUS_SHARING);
            return sharing;
        }).collect(Collectors.toList());

        // 帐单记录
        Bill bill = billInit(masterOrder);
        List<MerchantBill> merchantBillList = merchantBillInit(orderList);


        Boolean execute = transactionTemplate.execute(e -> {
            // 保存分账信息
            sharingList.forEach(sharing -> {
                profitSharingService.save(sharing);
                merchantService.operationBalance(sharing.getMerId(), sharing.getProfitSharingMerPrice(), Constants.OPERATION_TYPE_ADD);
            });
            //订单日志
            orderList.forEach(order -> {
                storeOrderStatusService.createLog(order.getOrderNo(), OrderConstants.ORDER_LOG_PAY_SUCCESS, OrderConstants.ORDER_LOG_MESSAGE_PAY_SUCCESS);
            });
            // 用户信息变更
            userService.updateById(user);
            //帐单记录
            userBillService.saveBatch(billList);
            billService.save(bill);
            merchantBillList.forEach(b -> b.setPid(bill.getId()));
            merchantBillService.saveBatch(merchantBillList);
            return Boolean.TRUE;
        });
        if (execute) {
            try {
                // 购买成功后根据配置送优惠券
                autoSendCoupons(masterOrder);
                // 给手机号用户发送短信通知
                sendNotification(user, masterOrder.getPayPrice(), masterOrder.getOrderNo());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("购买成功后发送优惠券/短信异常");
            }
        }
        return execute;
    }

    private List<MerchantBill> merchantBillInit(List<StoreOrder> orderList) {
        return orderList.stream().map(order -> {
            MerchantBill bill = new MerchantBill();
            bill.setUid(order.getUid());
            bill.setMerId(order.getMerId());
            bill.setLinkId(order.getId());
            bill.setOrderNo(order.getOrderNo());
            bill.setPm(1);
            bill.setAmount(order.getPayPrice());
            bill.setType(BillConstants.USER_BILL_TYPE_PAY_ORDER);
            bill.setMark("支付" + order.getPayPrice() + "元购买商品");
            return bill;
        }).collect(Collectors.toList());
    }

    private Bill billInit(MasterOrder masterOrder) {
        Bill bill = new Bill();
        bill.setUid(masterOrder.getUid());
        bill.setLinkId(masterOrder.getId());
        bill.setOrderNo(masterOrder.getOrderNo());
        bill.setPm(1);
        bill.setAmount(masterOrder.getPayPrice());
        bill.setType(BillConstants.USER_BILL_TYPE_PAY_ORDER);
        bill.setMark("支付" + masterOrder.getPayPrice() + "元购买商品");
        return bill;
    }

    /**
     * 发送用户通知
     *
     * @param user     用户
     * @param payPrice 支付金额
     * @param orderNo  订单号
     */
    private void sendNotification(User user, BigDecimal payPrice, String orderNo) {
        if (user.getUserType().equals(UserConstants.USER_LOGIN_TYPE_PHONE)) {
            // 发送短信通知
            smsService.sendPaySuccess(user.getCountryCode(), user.getPhone(), payPrice, orderNo);
            return;
        }
        if (StrUtil.isNotBlank(user.getEmail())) {
            emailService.sendPaySuccess(user.getUserType(), user.getEmail(), payPrice, orderNo, user.getIdentity());
        }
    }

    /**
     * 订单支付
     *
     * @param orderPayRequest 支付参数
     * @return OrderPayResultResponse
     * 1.微信支付拉起微信预支付，返回前端调用微信支付参数，在之后需要调用微信支付查询接口
     * 2.余额支付，更改对应信息后，加入支付成功处理task
     */
    @Override
    public OrderPayResultResponse payment(OrderPayRequest orderPayRequest) {
        MasterOrder masterOrder = masterOrderService.getByOrderNo(orderPayRequest.getOrderNo());
        if (ObjectUtil.isNull(masterOrder)) {
            throw new CrmebException("order does not exist");
        }
        if (masterOrder.getIsCancel()) {
            throw new CrmebException("order cancelled");
        }
        if (masterOrder.getPaid()) {
            throw new CrmebException("order paid");
        }

        User user = userService.getById(masterOrder.getUid());
        if (ObjectUtil.isNull(user)) throw new CrmebException("User does not exist");

        OrderPayResultResponse response = new OrderPayResultResponse();
        response.setOrderNo(masterOrder.getOrderNo());
        response.setPayType(masterOrder.getPayType());
        MasterOrder order = new MasterOrder();
        order.setId(masterOrder.getId());

        if (!masterOrder.getPayType().equals(orderPayRequest.getPayType())) {
            order.setPayType(orderPayRequest.getPayType());
        }
        if (!masterOrder.getPayChannel().equals(orderPayRequest.getPayChannel())) {
            order.setPayChannel(orderPayRequest.getPayChannel());
        }
        if (orderPayRequest.getPayType().equals(PayConstants.PAY_TYPE_PAYPAL)) {
            String approveUrl = paypalService.createPayPalOrder(masterOrder);
            response.setRedirect(approveUrl);
            order.setRedirect(response.getRedirect());
            masterOrderService.updateById(order);
        }
        if (orderPayRequest.getPayType().equals(PayConstants.PAY_TYPE_STRIPE)) {
            if (masterOrder.getPayType().equals(PayConstants.PAY_TYPE_STRIPE) && StrUtil.isNotBlank(masterOrder.getOutTradeNo())) {
                PaymentIntent paymentIntent = stripeService.retrieve(masterOrder.getOutTradeNo());
                response.setStripeClientSecret(paymentIntent.getClientSecret());
                response.setStatus(false);
                return response;
            }
            PaymentIntent paymentIntent = stripeService.paymentIntents(masterOrder);
            response.setStripeClientSecret(paymentIntent.getClientSecret());
            order.setOutTradeNo(paymentIntent.getId());
            masterOrderService.updateById(order);
        }
        if (orderPayRequest.getPayType().equals(PayConstants.PAY_TYPE_WECHAT)) {
            WxPayUnifiedOrderV3Request v3Request = getWxV3Request(masterOrder, orderPayRequest.getPayChannel());
            String responseUrl;
            try {
                responseUrl = wxPayService.createOrderV3(orderPayRequest.getPayChannel().equals("pc") ? TradeTypeEnum.NATIVE : TradeTypeEnum.H5, v3Request);
            } catch (WxPayException e) {
                e.printStackTrace();
                throw new CrmebException("微信支付异常，" + e.getMessage());
            }
            response.setRedirect(responseUrl);
        }
        // 有余额支付的时候，这里设置为true
        response.setStatus(false);
        return response;
    }

    private WxPayUnifiedOrderV3Request getWxV3Request(MasterOrder masterOrder, String payChannel) {
        WxPayUnifiedOrderV3Request v3Request = new WxPayUnifiedOrderV3Request();
        v3Request.setNotifyUrl(crmebConfig.getWxpayNotifyUrl());
        v3Request.setDescription(crmebConfig.getPaypalBrandName());
        v3Request.setOutTradeNo(masterOrder.getOrderNo());

        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(masterOrder.getPayPrice().multiply(new BigDecimal("100")).intValue());
        // CNY：人民币，境内商户号仅支持人民币。 非必传
        amount.setCurrency("CNY");
        v3Request.setAmount(amount);

        if (payChannel.equals("mobile")) {
            WxPayUnifiedOrderV3Request.SceneInfo sceneInfo = new WxPayUnifiedOrderV3Request.SceneInfo();
            sceneInfo.setPayerClientIp(RequestUtil.getClientIp());
            WxPayUnifiedOrderV3Request.H5Info h5Info = new WxPayUnifiedOrderV3Request.H5Info();
            // 场景类型 示例值：iOS, Android, Wap
            h5Info.setType("Wap");
            sceneInfo.setH5Info(h5Info);
            v3Request.setSceneInfo(sceneInfo);
        }
        return v3Request;
    }

    /**
     * PayPal订单授权成功
     *
     * @param request PayPal支付返回请求对象
     * @return PayPalResultResponse
     */
    @Override
    public PayPalResultResponse paypalSuccess(PayPalResultRequest request) {
        if (StrUtil.isBlank(request.getPayerID())) {
            throw new CrmebException("Payer ID is null");
        }
        MasterOrder masterOrder = masterOrderService.getByOutTradeNo(request.getToken());
        if (ObjectUtil.isNull(masterOrder)) {
            throw new CrmebException("single number error");
        }
        PayPalResultResponse response = new PayPalResultResponse();
        response.setOrderNo(masterOrder.getOrderNo());
        response.setPayPrice(masterOrder.getPayPrice());
        if (masterOrder.getPaid()) {
            response.setStatus(Boolean.TRUE);
            return response;
        }
        try { // 核销订单
            paypalService.captureOrder(masterOrder);
        } catch (IOException e) {
            logger.error("核销订单失败：outTradeNo = " + request.getToken(), e);
            response.setStatus(Boolean.FALSE);
            return response;
        }
        Boolean updatePaid = transactionTemplate.execute(e -> {
            masterOrderService.updatePaid(masterOrder.getOrderNo());
            storeOrderService.updatePaidByMasterNo(masterOrder.getOrderNo());
            return Boolean.TRUE;
        });
        if (!updatePaid) {
            throw new CrmebException("Payment succeeded, update order failed");
        }
        // 添加支付成功task
        redisUtil.lPush(TaskConstants.ORDER_TASK_PAY_SUCCESS_AFTER, masterOrder.getOrderNo());
        response.setStatus(Boolean.TRUE);
        return response;
    }

    /**
     * PayPal订单取消
     *
     * @param request PayPal支付返回请求对象
     * @return Boolean
     */
    @Override
    public Boolean paypalCancel(PayPalResultRequest request) {
        MasterOrder masterOrder = masterOrderService.getByOutTradeNo(request.getToken());
        if (ObjectUtil.isNull(masterOrder)) {
            return true;
        }
        if (masterOrder.getPaid()) {
            throw new CrmebException("Paid orders cannot be cancelled");
        }
        Boolean execute = transactionTemplate.execute(e -> {
            masterOrderService.cancel(masterOrder.getOrderNo());
            storeOrderService.cancelByMasterNo(masterOrder.getOrderNo(), true);
            return Boolean.TRUE;
        });

        if (execute) {
            //后续操作放入redis
            redisUtil.lPush(TaskConstants.ORDER_TASK_REDIS_KEY_AFTER_CANCEL_BY_USER, masterOrder.getOrderNo());
        }
        return execute;
    }

    /**
     * 获取支付方式
     */
    @Override
    public PayMethodResponse getPayMethod() {
        String paypalSwitch = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_PAYPAL_SWITCH);
        String stripeSwitch = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_STRIPE_SWITCH);
//        if (StrUtil.isNotBlank(stripeSwitch) && stripeSwitch.equals("true")) {
//            String stripeApiKey = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_STRIPE_API_KEY);
//        }
        String wechatPaySwitch = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_WECHAT_PAY_SWITCH);
        PayMethodResponse response = new PayMethodResponse();
        response.setPaypalStatus(StrUtil.isNotBlank(paypalSwitch) && paypalSwitch.equals(Constants.COMMON_SWITCH_OPEN_TYPE_ONE));
        response.setStripeStatus(StrUtil.isNotBlank(stripeSwitch) && stripeSwitch.equals(Constants.COMMON_SWITCH_OPEN_TYPE_ONE));
        response.setWechatPaySwitch(StrUtil.isNotBlank(wechatPaySwitch) && wechatPaySwitch.equals(Constants.COMMON_SWITCH_OPEN_TYPE_ONE));
        return response;
    }

    /**
     * 查询微信支付结果
     *
     * @param orderNo 订单编号
     */
    @Override
    public Boolean queryWechatPayResult(String orderNo) {
        MasterOrder masterOrder = masterOrderService.getByOrderNo(orderNo);
        if (ObjectUtil.isNull(masterOrder)) {
            throw new CrmebException("order is not exist!");
        }
        if (masterOrder.getIsCancel()) {
            throw new CrmebException("order is cancel");
        }
        if (!masterOrder.getPayType().equals(PayConstants.PAY_TYPE_WECHAT)) {
            throw new CrmebException("Not a WeChat Pay order");
        }
        if (masterOrder.getPaid()) {
            return Boolean.TRUE;
        }
        // 查询微信支付结果
        WxPayOrderQueryV3Result v3Result;
        try {
            v3Result = wxPayService.queryOrderV3("", masterOrder.getOrderNo());
        } catch (WxPayException e) {
            logger.error("wechat query pay exception, e = {}", e.getMessage());
            e.printStackTrace();
            return Boolean.FALSE;
        }
        if (!"SUCCESS".equals(v3Result.getTradeState())) {
            return Boolean.FALSE;
        }
        Boolean execute = transactionTemplate.execute(e -> {
            Boolean updated = masterOrderService.updatePaid(masterOrder.getOrderNo());
            if (!updated) {
                logger.error("wechat pay callback update master_order paid error");
                e.setRollbackOnly();
                return Boolean.FALSE;
            }
            updated = storeOrderService.updatePaidByMasterNo(masterOrder.getOrderNo());
            if (!updated) {
                logger.error("wechat pay callback update store_order paid error");
                e.setRollbackOnly();
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        });
        if (execute) {
            redisUtil.lPush(TaskConstants.ORDER_TASK_PAY_SUCCESS_AFTER, masterOrder.getOrderNo());
        }
        return execute;
    }

    private UserBill userBillInit(MasterOrder order, User user) {
        UserBill userBill = new UserBill();
        userBill.setPm(0);
        userBill.setUid(order.getUid());
        userBill.setLinkId(order.getId().toString());
        userBill.setTitle("购买商品");
        userBill.setCategory(BillConstants.USER_BILL_CATEGORY_MONEY);
        userBill.setType(BillConstants.USER_BILL_TYPE_PAY_ORDER);
        userBill.setNumber(order.getPayPrice());
        userBill.setBalance(user.getNowMoney());
        userBill.setMark("支付" + order.getPayPrice() + "元购买商品");
        return userBill;
    }

    /**
     * 商品购买后根据配置送券
     */
    private void autoSendCoupons(MasterOrder masterOrder) {
        // 根据订单详情获取商品信息
        List<StoreOrder> orderList = storeOrderService.getListByMasterNo(masterOrder.getOrderNo());
        List<String> orderNoList = orderList.stream().map(StoreOrder::getOrderNo).collect(Collectors.toList());
        List<StoreOrderInfo> orderInfoList = storeOrderInfoService.getListByOrderNoList(orderNoList);
        if (CollUtil.isEmpty(orderInfoList)) {
            return;
        }
        List<StoreCouponUser> couponUserList = CollUtil.newArrayList();
        Map<Integer, Boolean> couponMap = CollUtil.newHashMap();
        for (StoreOrderInfo orderInfo : orderInfoList) {
            List<StoreProductCoupon> couponsForGiveUser = storeProductCouponService.getListByProductId(orderInfo.getProductId());
            for (int i = 0; i < couponsForGiveUser.size(); ) {
                StoreProductCoupon storeProductCoupon = couponsForGiveUser.get(i);
                MyRecord record = storeCouponUserService.paySuccessGiveAway(storeProductCoupon.getIssueCouponId(), masterOrder.getUid());
                if (record.getStr("status").equals("fail")) {
                    logger.error(StrUtil.format("支付成功领取优惠券失败，失败原因：{}", record.getStr("errMsg")));
                    couponsForGiveUser.remove(i);
                    continue;
                }

                StoreCouponUser storeCouponUser = record.get("storeCouponUser");
                couponUserList.add(storeCouponUser);
                couponMap.put(storeCouponUser.getCouponId(), record.getBoolean("isLimited"));
                i++;
            }
        }

        Boolean execute = transactionTemplate.execute(e -> {
            if (CollUtil.isNotEmpty(couponUserList)) {
                storeCouponUserService.saveBatch(couponUserList);
                couponUserList.forEach(i -> storeCouponService.deduction(i.getCouponId(), 1, couponMap.get(i.getCouponId())));
            }
            return Boolean.TRUE;
        });
        if (!execute) {
            logger.error(StrUtil.format("支付成功领取优惠券，更新数据库失败，订单编号：{}", masterOrder.getOrderNo()));
        }
    }
}
