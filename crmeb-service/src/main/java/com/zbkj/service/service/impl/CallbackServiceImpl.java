package com.zbkj.service.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.bean.notify.OriginNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stripe.model.*;
import com.stripe.net.ApiResource;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.OrderConstants;
import com.zbkj.common.constants.TaskConstants;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreOrderProfitSharing;
import com.zbkj.common.model.order.StoreRefundOrder;
import com.zbkj.common.model.stripe.StripeCallback;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.service.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * 订单支付回调 CallbackService 实现类
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
public class CallbackServiceImpl implements CallbackService {

    private static final Logger logger = LoggerFactory.getLogger(CallbackServiceImpl.class);

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private MasterOrderService masterOrderService;
    @Autowired
    private StripeCallbackService stripeCallbackService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private StoreRefundOrderService storeRefundOrderService;
    @Autowired
    private StoreOrderProfitSharingService orderProfitSharingService;
    @Autowired
    private StoreOrderRefundStatusService orderRefundStatusService;
    @Autowired
    private StoreOrderStatusService storeOrderStatusService;
    @Autowired
    private MerchantService merchantService;

    private Gson gson = new Gson();


    /**
     * stripe回调
     */
    @Override
    public Integer stripe(String request) {
        Integer status = 400;
        Event event = null;
        try {
            event = ApiResource.GSON.fromJson(request, Event.class);
        } catch (JsonSyntaxException e) {
            logger.error("stripe GSON fromJson error");
            return status;
        }
        StripeCallback stripeCallback = new StripeCallback();
        stripeCallback.setId(event.getId());
        stripeCallback.setType(event.getType());
        stripeCallback.setCreated(event.getCreated());
        stripeCallback.setRequest(request);
        stripeCallback.setAddTime(DateUtil.date());
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
            logger.error("stripe Deserialization failed");
            stripeCallback.setResponseStatus(400);
            stripeCallback.setResponseDes("stripe Deserialization failed");
            stripeCallbackService.save(stripeCallback);
            return status;
        }
        stripeCallback.setData(stripeObject.toJson());

        logger.info("stripeObject = " + stripeObject.toJson());
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                logger.info("stripe payment intent succeeded paymentIntent = " + gson.toJson(paymentIntent));
                // Then define and call a method to handle the successful payment intent.
                status = handlePaymentIntentSucceeded(paymentIntent);
                stripeCallback.setDataId(paymentIntent.getId());
                stripeCallback.setResponseStatus(status);
                if (status.equals(200)) {
                    stripeCallback.setResponseDes("success");
                } else {
                    stripeCallback.setResponseDes("stripe order error");
                }
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                logger.info("stripe payment method attached");
                // Then define and call a method to handle the successful attachment of a PaymentMethod.
                // handlePaymentMethodAttached(paymentMethod);
                break;
            // ... handle other event types
            default:
                logger.warn("Stripe Unhandled event type: " + event.getType());
        }
        stripeCallbackService.save(stripeCallback);
        return status;
    }

    /**
     * 微信支付回调
     *
     * @param request   微信回调请求体
     * @param timestamp 微信回调时间戳
     * @param nonce     微信回调随机串
     * @param signature 微信回调签名
     * @param serial    微信回调
     */
    @Override
    public Boolean wechat(String request, String timestamp, String nonce, String signature, String serial) {
        if (StrUtil.isBlank(request)) {
            return Boolean.FALSE;
        }
        SignatureHeader signatureHeader = new SignatureHeader(timestamp, nonce, signature, serial);
        WxPayOrderNotifyV3Result v3Result;
        try {
            v3Result = wxPayService.parseOrderNotifyV3Result(request, signatureHeader);
        } catch (WxPayException e) {
            e.printStackTrace();
            logger.error("微信支付回调V3发生异常，异常信息={}", e.getMessage());
            return Boolean.FALSE;
        }
        logger.warn("微信支付回调,WxPayOrderNotifyV3Result = {}", v3Result);
        WxPayOrderNotifyV3Result.DecryptNotifyResult notifyResult = v3Result.getResult();
        OriginNotifyResponse notifyResponse = v3Result.getRawData();
        if (!"TRANSACTION.SUCCESS".equals(notifyResponse.getEventType()) || !"encrypt-resource".equals(notifyResponse.getResourceType())) {
            return Boolean.FALSE;
        }
        if (!wxPayService.getConfig().getAppId().equals(notifyResult.getAppid())) {
            logger.error("微信支付回调，appid不一致");
            return Boolean.FALSE;
        }
        MasterOrder masterOrder = masterOrderService.getByOrderNo(notifyResult.getOutTradeNo());
        if (ObjectUtil.isNull(masterOrder)) {
            logger.error("wechat pay error : 订单不存在==》" + notifyResult.getOutTradeNo());
            return Boolean.FALSE;
        }
        if (masterOrder.getPaid()) {
            logger.error("wechat pay error : 订单已处理==》" + notifyResult.getOutTradeNo());
            return Boolean.TRUE;
        }
        // 支付成功处理
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

    /**
     * 微信退款支付回调
     *
     * @param request   微信回调请求体
     * @param timestamp 微信回调时间戳
     * @param nonce     微信回调随机串
     * @param signature 微信回调签名
     * @param serial    微信回调
     */
    @Override
    public Boolean weChatRefund(String request, String timestamp, String nonce, String signature, String serial) {
        if (StrUtil.isBlank(request)) {
            return Boolean.FALSE;
        }
        SignatureHeader signatureHeader = new SignatureHeader(timestamp, nonce, signature, serial);
        WxPayRefundNotifyV3Result v3Result;
        try {
            v3Result = wxPayService.parseRefundNotifyV3Result(request, signatureHeader);
        } catch (WxPayException e) {
            e.printStackTrace();
            logger.error("微信退款回调V3发生异常，异常信息={}", e.getMessage());
            return Boolean.FALSE;
        }
        logger.warn("微信支付回调,WxPayOrderNotifyV3Result = {}", v3Result);
        WxPayRefundNotifyV3Result.DecryptNotifyResult notifyResult = v3Result.getResult();
        OriginNotifyResponse notifyResponse = v3Result.getRawData();
        if (!"REFUND.SUCCESS".equals(notifyResponse.getEventType()) || !"encrypt-resource".equals(notifyResponse.getResourceType())) {
            return Boolean.FALSE;
        }
        StoreRefundOrder refundOrder = storeRefundOrderService.getByRefundOrderNo(notifyResult.getOutRefundNo());
        StoreOrder storeOrder = storeOrderService.getByOderNo(refundOrder.getMerOrderNo());
        //修改订单退款状态
        storeOrder.setRefundStatus(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUND);
        refundOrder.setRefundStatus(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUND);
        refundOrder.setRefundTime(com.zbkj.common.utils.DateUtil.nowDateTime());
        // 获取分账信息
        StoreOrderProfitSharing profitSharing = orderProfitSharingService.getByMerOrderNo(refundOrder.getMerOrderNo());
        profitSharing.setStatus(OrderConstants.ORDER_PROFIT_SHARING_STATUS_REFUND);
        profitSharing.setProfitSharingRefund(storeOrder.getPayPrice());

        Boolean execute = transactionTemplate.execute(e -> {
            storeRefundOrderService.updateById(refundOrder);
            storeOrderService.updateById(storeOrder);
            //新增日志
            storeOrderStatusService.saveRefund(storeOrder.getOrderNo(), refundOrder.getRefundPrice(), "成功");
            orderRefundStatusService.createLog(refundOrder.getRefundOrderNo(),
                    OrderConstants.REFUND_ORDER_LOG_TYPE_REFUND, OrderConstants.ORDER_LOG_MESSAGE_REFUND_PRICE.replace("{amount}", refundOrder.getRefundPrice().toString()));
            // 分账处理
            orderProfitSharingService.updateById(profitSharing);
            merchantService.operationBalance(profitSharing.getMerId(), profitSharing.getProfitSharingMerPrice(), Constants.OPERATION_TYPE_SUBTRACT);
            // 退款task
            return Boolean.TRUE;
        });
        if (execute) {
            redisUtil.lPush(TaskConstants.ORDER_TASK_REDIS_KEY_AFTER_REFUND_BY_USER, refundOrder.getRefundOrderNo());
        } else {
            storeOrderStatusService.saveRefund(storeOrder.getOrderNo(), storeOrder.getPayPrice(), "失败");
        }
        return execute;
    }

    private Integer handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        String paymentIntentId = paymentIntent.getId();
        MasterOrder masterOrder = masterOrderService.getByOutTradeNo(paymentIntentId);
        if (ObjectUtil.isNull(masterOrder)) {
            logger.error("stripe order error : single number error");
            return 400;
        }
        Boolean execute = transactionTemplate.execute(e -> {
            masterOrderService.updatePaid(masterOrder.getOrderNo());
            storeOrderService.updatePaidByMasterNo(masterOrder.getOrderNo());
            return Boolean.TRUE;
        });
        if (!execute) {
            logger.error("stripe order error : update order failed");
            return 400;
        }
        // 添加支付成功task
        redisUtil.lPush(TaskConstants.ORDER_TASK_PAY_SUCCESS_AFTER, masterOrder.getOrderNo());
        return 200;
    }

}
