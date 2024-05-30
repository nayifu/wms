package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.*;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.cat.StoreCart;
import com.zbkj.common.model.coupon.StoreCouponUser;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.order.*;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.model.product.StoreProductAttrValue;
import com.zbkj.common.model.product.StoreProductReply;
import com.zbkj.common.model.user.User;
import com.zbkj.common.model.user.UserAddress;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.common.vo.MyRecord;
import com.zbkj.common.vo.PreOrderVo;
import com.zbkj.common.vo.PreStoreOrderInfoVo;
import com.zbkj.common.vo.PreStoreOrderVo;
import com.zbkj.service.service.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * H5端订单操作
 *  +----------------------------------------------------------------------
 *  | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 *  +----------------------------------------------------------------------
 *  | Copyright (c) 2016~2023 https://www.crmeb.com All rights reserved.
 *  +----------------------------------------------------------------------
 *  | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 *  +----------------------------------------------------------------------
 *  | Author: CRMEB Team <admin@crmeb.com>
 *  +----------------------------------------------------------------------
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private StoreCartService storeCartService;

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private StoreOrderInfoService storeOrderInfoService;

    @Autowired
    private StoreOrderStatusService storeOrderStatusService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private StoreProductReplyService storeProductReplyService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private SystemGroupDataService systemGroupDataService;

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private StoreProductAttrValueService attrValueService;

    @Autowired
    private StoreCouponUserService storeCouponUserService;

    @Autowired
    private StoreProductAttrValueService storeProductAttrValueService;

    @Autowired
    private OrderLogisticsService orderLogisticsService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MasterOrderService masterOrderService;

    @Autowired
    private StoreRefundOrderService storeRefundOrderService;

    @Autowired
    private StoreOrderRefundStatusService storeOrderRefundStatusService;

    /**
     * 删除已完成订单
     * @param orderNo 商户订单号
     * @return 删除结果
     */
    @Override
    public Boolean delete(String orderNo) {
        StoreOrder storeOrder = storeOrderService.getByOderNo(orderNo);
        Integer userId = userService.getUserIdException();
        if (ObjectUtil.isNull(storeOrder) || !userId.equals(storeOrder.getUid())) {
            throw new CrmebException("No relevant order information found!");
        }
        if (storeOrder.getIsUserDel() || storeOrder.getIsMerchantDel()) {
            throw new CrmebException("Order deleted!");
        }
        if (!storeOrder.getStatus().equals(OrderConstants.ORDER_STATUS_OVER)) {
            throw new CrmebException("Only completed orders can be deleted!");
        }
        if (storeOrder.getPaid()) {
            if (storeOrder.getRefundStatus() > 0 && !storeOrder.getRefundStatus().equals(OrderConstants.ORDER_REFUND_STATUS_REFUND)) {
                throw new CrmebException("Orders cannot be deleted during the refund process!");
            }
        } else {
            throw new CrmebException("Unpaid orders cannot be deleted!");
        }

        //可以删除
        storeOrder.setIsUserDel(true);
        return transactionTemplate.execute(e -> {
            storeOrderService.updateById(storeOrder);
            //日志
            storeOrderStatusService.createLog(storeOrder.getOrderNo(), OrderConstants.ORDER_LOG_REMOVE, OrderConstants.ORDER_LOG_MESSAGE_REMOVE);
            return Boolean.TRUE;
        });
    }

    /**
     * 创建订单商品评价
     * @param request 请求参数
     * @return Boolean
     */
    @Override
    public Boolean reply(StoreProductReplyAddRequest request) {
        StoreOrder order = getByOrderNoException(request.getOrderNo());
        if (order.getIsReply()) {
            throw new CrmebException("Order reviewed!");
        }
        StoreOrderInfo orderInfo = storeOrderInfoService.getById(request.getOrderInfoId());
        if (ObjectUtil.isNull(orderInfo) || !order.getOrderNo().equals(orderInfo.getMerOrderNo())) {
            throw new CrmebException("Order details do not exist");
        }
        if (!orderInfo.getIsReceipt()) {
            throw new CrmebException("Please comment after the order is received");
        }
        if (orderInfo.getIsReply()) {
            throw new CrmebException("Evaluated!");
        }
        orderInfo.setIsReply(true);
        User user = userService.getInfoException();
        StoreProductReply storeProductReply = new StoreProductReply();
        BeanUtils.copyProperties(request, storeProductReply);
        storeProductReply.setMerId(orderInfo.getMerId());
        storeProductReply.setProductId(orderInfo.getProductId());
        storeProductReply.setAttrValueId(orderInfo.getAttrValueId());
        storeProductReply.setSku(orderInfo.getSku());
        storeProductReply.setUid(user.getUid());
        storeProductReply.setAvatar(systemAttachmentService.clearPrefix(user.getAvatar()));
        storeProductReply.setNickname(user.getNickname());
        if (StringUtils.isNotBlank(request.getPics())) {
            String pics = request.getPics().replace("[\"","").replace("\"]","")
                    .replace("\"","");
            storeProductReply.setPics(systemAttachmentService.clearPrefix(ArrayUtils.toString(pics)));
        }
        // 为评论订单详情数量
        Integer notReplyNum = storeOrderInfoService.getNotReplyNumByOrderNo(orderInfo.getMerOrderNo());
        Boolean execute = transactionTemplate.execute(e -> {
            storeOrderInfoService.updateById(orderInfo);
            storeProductReplyService.save(storeProductReply);
            //修改订单信息
            if (notReplyNum <= 0) {
                order.setIsReply(true);
                storeOrderService.updateById(order);
            }
            return Boolean.TRUE;
        });
        if (!execute) {
            throw new CrmebException("Evaluation order failed");
        }
        return execute;
    }

    /**
     * 订单收货
     * @param orderNo 商户订单号
     */
    @Override
    public Boolean take(String orderNo) {
        StoreOrder storeOrder = storeOrderService.getByOderNo(orderNo);
        Integer userId = userService.getUserIdException();
        if(ObjectUtil.isNull(storeOrder) || !userId.equals(storeOrder.getUid())){
            //订单号错误
            throw new CrmebException("No relevant order information found!");
        }
        if (!storeOrder.getStatus().equals(OrderConstants.ORDER_STATUS_AWAIT_RECEIVING)) {
            throw new CrmebException("wrong order status");
        }

        //已收货，待评价
        storeOrder.setStatus(OrderConstants.ORDER_STATUS_OVER);
        return transactionTemplate.execute(e -> {
            storeOrderService.updateById(storeOrder);
            storeOrderInfoService.orderReceipt(storeOrder.getOrderNo());
            // 日志
            storeOrderStatusService.createLog(storeOrder.getOrderNo(), OrderConstants.ORDER_LOG_RECEIVING, OrderConstants.ORDER_LOG_MESSAGE_RECEIVING);
            return Boolean.TRUE;
        });
    }

    /**
     * 订单取消
     * @param orderNo 主订单号
     */
    @Override
    public Boolean cancel(String orderNo) {
        MasterOrder masterOrder = masterOrderService.getByOrderNo(orderNo);
        if (ObjectUtil.isNull(masterOrder)) {
            throw new CrmebException("order does not exist");
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
     * 订单退款申请
     * @param request OrderRefundApplyRequest 退款参数
     */
    @Override
    public Boolean refundApply(OrderRefundApplyRequest request) {
        StoreOrder storeOrder = storeOrderService.getByOderNo(request.getOrderNo());
        if (ObjectUtil.isNull(storeOrder) || storeOrder.getIsUserDel() || storeOrder.getIsMerchantDel()) {
            throw new CrmebException("order does not exist");
        }
        if (!storeOrder.getPaid()) {
            throw new CrmebException("Unpaid orders cannot be refunded");
        }
        if (storeOrder.getStatus().equals(OrderConstants.ORDER_STATUS_AWAIT_RECEIVING)) {
            throw new CrmebException("Pending orders cannot be refunded");
        }
        if (storeOrder.getRefundStatus().equals(OrderConstants.ORDER_REFUND_STATUS_APPLY)) {
            throw new CrmebException("Applying for a refund");
        }
        if (storeOrder.getRefundStatus().equals(OrderConstants.ORDER_REFUND_STATUS_REFUNDING)) {
            throw new CrmebException("Order refund process");
        }
        if (storeOrder.getRefundStatus().equals(OrderConstants.ORDER_REFUND_STATUS_REFUND)) {
            throw new CrmebException("Order has been refunded");
        }
        storeOrder.setRefundStatus(OrderConstants.ORDER_REFUND_STATUS_APPLY);
        StoreRefundOrder refundOrder = new StoreRefundOrder();
        refundOrder.setRefundOrderNo(CrmebUtil.getOrderNo(OrderConstants.ORDER_PREFIX_REFUND));
        refundOrder.setMerOrderNo(storeOrder.getOrderNo());
        refundOrder.setMasterOrderNo(storeOrder.getMasterOrderNo());
        refundOrder.setMerId(storeOrder.getMerId());
        refundOrder.setUid(storeOrder.getUid());
        refundOrder.setEmail(storeOrder.getEmail());
        refundOrder.setRealName(storeOrder.getRealName());
        refundOrder.setUserPhone(storeOrder.getUserPhone());
        refundOrder.setUserAddress(storeOrder.getUserAddress());
        refundOrder.setRefundPrice(storeOrder.getPayPrice());
        refundOrder.setTotalNum(storeOrder.getTotalNum());
        refundOrder.setRefundReasonWap(request.getText());
        refundOrder.setRefundReasonWapImg(systemAttachmentService.clearPrefix(request.getReasonImage()));
        refundOrder.setRefundReasonWapExplain(request.getExplain());
        refundOrder.setRefundStatus(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_APPLY);

        Boolean execute = transactionTemplate.execute(e -> {
            storeOrderService.updateById(storeOrder);
            storeRefundOrderService.save(refundOrder);
            storeOrderRefundStatusService.createLog(refundOrder.getRefundOrderNo(), OrderConstants.REFUND_ORDER_LOG_TYPE_APPLY,
                    StrUtil.format(OrderConstants.ORDER_LOG_MESSAGE_REFUND_APPLY, request.getExplain()));
            return Boolean.TRUE;
        });
        if (!execute) throw new CrmebException("Failed to apply for refund");
        return execute;
    }

    /**
     * 订单列表
     * @param status 类型
     * @param pageRequest 分页
     * @return List<OrderDetailResponse>
     */
    @Override
    public PageInfo<OrderDetailResponse> list(Integer status, PageParamRequest pageRequest) {
        Integer userId = userService.getUserIdException();
        PageInfo<StoreOrder> pageInfo = storeOrderService.getUserOrderList(userId, status, pageRequest);
        List<StoreOrder> orderList = pageInfo.getList();
        if (CollUtil.isEmpty(orderList)) {
            return CommonPage.copyPageInfo(pageInfo, CollUtil.newArrayList());
        }
        List<OrderDetailResponse> responseList = CollUtil.newArrayList();
        List<Integer> merIdList = orderList.stream().map(StoreOrder::getMerId).distinct().collect(Collectors.toList());
        Map<Integer, Merchant> merchantMap = merchantService.getMerIdMapByIdList(merIdList);
        for (StoreOrder storeOrder : orderList) {
            OrderDetailResponse infoResponse = new OrderDetailResponse();
            BeanUtils.copyProperties(storeOrder, infoResponse);
            // 订单详情对象列表
            List<StoreOrderInfo> orderInfoList = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderNo());
            List<OrderInfoResponse> infoResponseList = CollUtil.newArrayList();
            orderInfoList.forEach(e -> {
                OrderInfoResponse orderInfoResponse = new OrderInfoResponse();
                BeanUtils.copyProperties(e, orderInfoResponse);
                infoResponseList.add(orderInfoResponse);
            });
            infoResponse.setOrderInfoList(infoResponseList);
            infoResponse.setMerName(merchantMap.get(storeOrder.getMerId()).getName());
            responseList.add(infoResponse);
        }
        return CommonPage.copyPageInfo(pageInfo, responseList);
    }

    /**
     * 订单详情
     * @param orderNo 订单id
     */
    @Override
    public StoreOrderDetailInfoResponse detailOrder(String orderNo) {
        User currentUser = userService.getInfoException();

        // 查询订单
        StoreOrder storeOrder = storeOrderService.getByOderNo(orderNo);
        if (ObjectUtil.isNull(storeOrder) || storeOrder.getIsUserDel() || storeOrder.getIsMerchantDel()) {
            throw new CrmebException("order does not exist");
        }
        if (!storeOrder.getUid().equals(currentUser.getUid())) {
            throw new CrmebException("order does not exist");
        }

        StoreOrderDetailInfoResponse storeOrderDetailResponse = new StoreOrderDetailInfoResponse();
        BeanUtils.copyProperties(storeOrder, storeOrderDetailResponse);
        // 订单详情对象列表
        List<OrderInfoResponse> infoResponseList = CollUtil.newArrayList();
        List<StoreOrderInfo> infoList = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderNo());
        infoList.forEach(e -> {
            OrderInfoResponse orderInfoResponse = new OrderInfoResponse();
            BeanUtils.copyProperties(e, orderInfoResponse);
            infoResponseList.add(orderInfoResponse);
        });
        storeOrderDetailResponse.setOrderInfoList(infoResponseList);
        // 商户名称
        Merchant merchant = merchantService.getById(storeOrder.getMerId());
        storeOrderDetailResponse.setMerName(merchant.getName());
        return storeOrderDetailResponse;
    }

    /**
     * 订单tap data
     * @return 订单状态数据量
     */
    @Override
    public OrderDataResponse orderData() {
        Integer userId = userService.getUserIdException();
        OrderDataResponse result = new OrderDataResponse();

        // 待支付订单数
        result.setUnPaidCount(masterOrderService.getAwaitPayCount(userId));
        // 待发货
        result.setUnShippedCount(storeOrderService.getTopDataUtil(OrderConstants.ORDER_STATUS_H5_NOT_SHIPPED, userId));
        // 待收货
        result.setReceivedCount(storeOrderService.getTopDataUtil(OrderConstants.ORDER_STATUS_H5_SPIKE, userId));
        // 已完成
        result.setCompleteCount(storeOrderService.getTopDataUtil(OrderConstants.ORDER_STATUS_H5_COMPLETE, userId));
        return result;
    }

    /**
     * 查询退款理由
     * @return 退款理由集合
     */
    @Override
    public List<String> getRefundReason() {
        String reasonString = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_STOR_REASON);
        reasonString = CrmebUtil.UnicodeToCN(reasonString);
        reasonString = reasonString.replace("rn", "n");
        return Arrays.asList(reasonString.split("\\n"));
    }

    /**
     * 订单物流查看
     * @param orderNo 订单id
     * @return OrderLogistics
     */
    @Override
    public OrderLogistics expressOrder(String orderNo) {
        getByOrderNoException(orderNo);
        return orderLogisticsService.getByOrderNo(orderNo);
    }

    /**
     * 获取申请订单退款信息
     * @param orderNo 商户订单编号
     * @return ApplyRefundOrderInfoResponse
     */
    @Override
    public ApplyRefundOrderInfoResponse applyRefundOrderInfo(String orderNo) {
        StoreOrder storeOrder = getByOrderNoException(orderNo);
        ApplyRefundOrderInfoResponse response = new ApplyRefundOrderInfoResponse();
        BeanUtils.copyProperties(storeOrder, response);
        // 订单详情对象列表
        List<StoreOrderInfo> infoVoList = storeOrderInfoService.getListByOrderNo(orderNo);
        List<OrderInfoResponse> infoResponseList = CollUtil.newArrayList();
        infoVoList.forEach(e -> {
            OrderInfoResponse orderInfoResponse = new OrderInfoResponse();
            BeanUtils.copyProperties(e, orderInfoResponse);
            infoResponseList.add(orderInfoResponse);
        });
        response.setOrderInfoList(infoResponseList);
        return response;
    }

    /**
     * 订单预下单
     * @param request 预下单请求参数
     * @return PreOrderResponse
     */
    @Override
    public MyRecord  preOrder(PreOrderRequest request) {
        if (CollUtil.isEmpty(request.getOrderDetails())) {
            throw new CrmebException("Pre-order details cannot be empty");
        }
        User user = userService.getInfoException();
        // 校验预下单商品信息
        PreOrderVo preOrderVo = validatePreOrderRequest(request, user);
        List<PreStoreOrderInfoVo> orderInfoList = new ArrayList<>();
        for (PreStoreOrderVo orderVo : preOrderVo.getOrderList()) {
            orderInfoList.addAll(orderVo.getOrderInfoList());
        }
        // 商品总计金额
        BigDecimal proTotalPrice = orderInfoList.stream().map(e -> e.getPrice().multiply(new BigDecimal(e.getPayNum()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        preOrderVo.setProTotalFee(proTotalPrice);
        // 购买商品总数量
        int orderProNum = orderInfoList.stream().mapToInt(PreStoreOrderInfoVo::getPayNum).sum();
        preOrderVo.setOrderProNum(orderProNum);
        // 运费 = 单件商品运费 * 商品数量
        preOrderVo.setFreightFee(getFreightFee(orderInfoList));
        // 获取默认地址
        UserAddress userAddress = userAddressService.getDefaultByUid(user.getUid());
        if (ObjectUtil.isNotNull(userAddress)) {
            preOrderVo.setAddressId(userAddress.getId());
            preOrderVo.setRealName(userAddress.getRealName());
            preOrderVo.setPhone(userAddress.getPhone());
            preOrderVo.setDetail(userAddress.getDetail());
            preOrderVo.setCountry(userAddress.getCountry());
            preOrderVo.setEmail(userAddress.getEmail());
        }
        // 实际支付金额
        BigDecimal totalPrice = preOrderVo.getProTotalFee().add(preOrderVo.getFreightFee());
        preOrderVo.setTotalPrice(totalPrice);
        preOrderVo.setPayFee(totalPrice);
        // 缓存订单
        String key = user.getUid() + DateUtil.getNowTime().toString()+CrmebUtil.getUuid();
        redisUtil.set(RedisConstants.USER_READY_ORDER_KEY + key, JSONObject.toJSONString(preOrderVo), Constants.ORDER_CASH_CONFIRM, TimeUnit.MINUTES);
        MyRecord record = new MyRecord();
        record.set("preOrderNo", key);
        return record;
    }

    /**
     * 加载预下单信息
     * @param preOrderNo 预下单号
     * @return 预下单信息
     */
    @Override
    public PreOrderVo loadPreOrder(String preOrderNo) {
        // 通过缓存获取预下单对象
        String key = RedisConstants.USER_READY_ORDER_KEY + preOrderNo;
        boolean exists = redisUtil.exists(key);
        if (!exists) {
            throw new CrmebException("Pre-order order does not exist");
        }
        String orderVoString = redisUtil.get(key).toString();
        PreOrderVo orderInfoVo = JSONObject.parseObject(orderVoString, PreOrderVo.class);
        return orderInfoVo;
    }

    /**
     * 计算订单价格
     * @param request 计算订单价格请求对象
     * @return ComputedOrderPriceResponse
     */
    @Override
    public ComputedOrderPriceResponse computedOrderPrice(OrderComputedPriceRequest request) {
        // 通过缓存获取预下单对象
        PreOrderVo preOrderVo = loadPreOrder(request.getPreOrderNo());
        User user = userService.getInfoException();
        return computedPrice(request, preOrderVo, user);
    }

    /**
     * 创建订单
     * @param request 创建订单请求参数
     * @return MyRecord 订单编号
     */
    @Override
    public MyRecord createOrder(CreateOrderRequest request) {
        User user = userService.getInfoException();
        // 通过缓存获取预下单对象
        String key = RedisConstants.USER_READY_ORDER_KEY + request.getPreOrderNo();
        boolean exists = redisUtil.exists(key);
        if (!exists) {
            throw new CrmebException("Pre-order order does not exist");
        }
        String orderVoString = redisUtil.get(key).toString();
        PreOrderVo preOrderVo = JSONObject.parseObject(orderVoString, PreOrderVo.class);

        // 检测支付方式
        if (!checkPayType(request.getPayType())) {
            throw new CrmebException("This payment method is not currently supported, please refresh the page or contact the administrator");
        }

        // 校验商品库存
        List<MyRecord> skuRecordList = validateProductStock(preOrderVo);

        // 校验收货信息
        if (request.getAddressId() <= 0) throw new CrmebException("Please select a delivery address");
        UserAddress userAddress = userAddressService.getById(request.getAddressId());
        if (ObjectUtil.isNull(userAddress) || userAddress.getIsDel()) {
            throw new CrmebException("Incorrect delivery address");
        }

        // 计算订单各种价格
        OrderComputedPriceRequest orderComputedPriceRequest = new OrderComputedPriceRequest();
        orderComputedPriceRequest.setAddressId(request.getAddressId());
        orderComputedPriceRequest.setCouponId(request.getCouponId());
        ComputedOrderPriceResponse computedOrderPriceResponse = computedPrice(orderComputedPriceRequest, preOrderVo, user);

        // 生成主订单号
        String orderNo = CrmebUtil.getOrderNo(OrderConstants.ORDER_PREFIX_PLATFORM);
        // 主订单
        MasterOrder masterOrder = new MasterOrder();
        masterOrder.setOrderNo(orderNo);
        masterOrder.setUid(user.getUid());
        masterOrder.setEmail(userAddress.getEmail());
        masterOrder.setRealName(userAddress.getRealName());
        masterOrder.setUserPhone(userAddress.getPhone());
        masterOrder.setUserAddress(userAddress.getDetail());
        masterOrder.setTotalNum(preOrderVo.getOrderProNum());
        masterOrder.setProTotalPrice(computedOrderPriceResponse.getProTotalFee());
        masterOrder.setTotalPostage(computedOrderPriceResponse.getFreightFee());
        BigDecimal totalPrice = computedOrderPriceResponse.getProTotalFee().add(computedOrderPriceResponse.getFreightFee());
        masterOrder.setTotalPrice(totalPrice);
        masterOrder.setPayPostage(computedOrderPriceResponse.getFreightFee());
        masterOrder.setPayPrice(computedOrderPriceResponse.getPayFee());
        masterOrder.setCouponId(Optional.ofNullable(request.getCouponId()).orElse(0));
        masterOrder.setCouponPrice(computedOrderPriceResponse.getCouponFee());
        masterOrder.setPaid(false);
        masterOrder.setPayType(request.getPayType());
        masterOrder.setMark(StrUtil.isEmpty(request.getMark()) ? "" : request.getMark());
        masterOrder.setPayChannel(request.getPayChannel());
        // 商户订单
        List<StoreOrderInfo> storeOrderInfoList = CollUtil.newArrayList();
        StoreCouponUser storeCouponUser = null;
        if (masterOrder.getCouponId() > 0) {
            storeCouponUser = storeCouponUserService.getById(request.getCouponId());
        }
        StoreCouponUser finalStoreCouponUser = storeCouponUser;
        List<StoreOrder> storeOrderList = preOrderVo.getOrderList().stream().map(e -> {
            // 生成商户订单号
            String childOrderNo = CrmebUtil.getOrderNo(OrderConstants.ORDER_PREFIX_MERCHANT);
            // 商户订单
            StoreOrder storeOrder = new StoreOrder();
            storeOrder.setMasterOrderNo(masterOrder.getOrderNo());
            storeOrder.setOrderNo(childOrderNo);
            storeOrder.setMerId(e.getMerId());
            storeOrder.setUid(user.getUid());
            storeOrder.setEmail(userAddress.getEmail());
            storeOrder.setRealName(userAddress.getRealName());
            storeOrder.setUserPhone(userAddress.getPhone());
            storeOrder.setUserAddress(userAddress.getDetail());
            storeOrder.setPaid(false);
            storeOrder.setPayType(request.getPayType());
            storeOrder.setUserRemark(masterOrder.getMark());
            // 订单详情
            List<StoreOrderInfo> list = e.getOrderInfoList().stream().map(info -> {
                StoreOrderInfo orderInfo = new StoreOrderInfo();
                orderInfo.setMerOrderNo(childOrderNo);
                orderInfo.setMerId(e.getMerId());
                orderInfo.setProductId(info.getProductId());
                orderInfo.setProductName(info.getProductName());
                orderInfo.setImage(info.getImage());
                orderInfo.setAttrValueId(info.getAttrValueId());
                orderInfo.setSku(info.getSku());
                orderInfo.setPrice(info.getPrice());
                orderInfo.setPayNum(info.getPayNum());
                orderInfo.setWeight(info.getWeight());
                orderInfo.setVolume(info.getVolume());
                return orderInfo;
            }).collect(Collectors.toList());
            storeOrderInfoList.addAll(list);

            storeOrder.setTotalNum(list.stream().mapToInt(StoreOrderInfo::getPayNum).sum());
            BigDecimal proTotalPrice = list.stream().map(i -> i.getPrice().multiply(new BigDecimal(i.getPayNum()))).reduce(BigDecimal.ZERO, BigDecimal::add);
            storeOrder.setProTotalPrice(proTotalPrice);
            storeOrder.setTotalPostage(getFreightFee(e.getOrderInfoList()));
            storeOrder.setTotalPrice(proTotalPrice.add(storeOrder.getTotalPostage()));
            storeOrder.setPayPrice(storeOrder.getTotalPrice());
            storeOrder.setPayPostage(storeOrder.getTotalPostage());
            // 优惠金额部分
            storeOrder.setCouponId(0);
            storeOrder.setCouponPrice(BigDecimal.ZERO);
            if (masterOrder.getCouponId() > 0 && storeOrder.getMerId().equals(finalStoreCouponUser.getMerId())) {
                storeOrder.setCouponId(masterOrder.getCouponId());
                if (storeOrder.getProTotalPrice().compareTo(masterOrder.getCouponPrice()) > 0) {
                    storeOrder.setPayPrice(storeOrder.getProTotalPrice().add(storeOrder.getPayPostage()).subtract(masterOrder.getCouponPrice()));
                } else {
                    storeOrder.setPayPrice(storeOrder.getPayPostage());
                }
                storeOrder.setCouponPrice(masterOrder.getCouponPrice());
            }
            return storeOrder;
        }).collect(Collectors.toList());

        Boolean execute = transactionTemplate.execute(e -> {
            // 扣减库存
            for (MyRecord skuRecord : skuRecordList) {
                // 普通商品口库存
                storeProductService.operationStock(skuRecord.getInt("productId"), skuRecord.getInt("num"), "sub");
                // 普通商品规格扣库存
                storeProductAttrValueService.operationStock(skuRecord.getInt("attrValueId"), skuRecord.getInt("num"), "sub", ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL, skuRecord.getInt("attrValueVersion"));
            }
            masterOrderService.save(masterOrder);
            storeOrderService.saveBatch(storeOrderList);
            storeOrderInfoService.saveBatch(storeOrderInfoList);
            // 优惠券修改
            if (masterOrder.getCouponId() > 0) {
                finalStoreCouponUser.setStatus(CouponConstants.STORE_COUPON_USER_STATUS_USED);
                storeCouponUserService.updateById(finalStoreCouponUser);
            }
            // 生成订单日志
            storeOrderList.forEach(order -> {
                storeOrderStatusService.createLog(order.getOrderNo(), OrderConstants.ORDER_LOG_CREATE, OrderConstants.ORDER_LOG_MESSAGE_CREATE);
            });

            // 清除购物车数据
            if (CollUtil.isNotEmpty(preOrderVo.getCartIdList())) {
                storeCartService.deleteCartByIds(preOrderVo.getCartIdList());
            }
            return Boolean.TRUE;
        });
        if (!execute) {
            throw new CrmebException("Order generation failed");
        }

        // 删除缓存订单
        if (redisUtil.exists(key)) {
            redisUtil.delete(key);
        }

        // 加入自动未支付自动取消队列
        redisUtil.lPush(TaskConstants.ORDER_TASK_REDIS_KEY_AUTO_CANCEL_KEY, masterOrder.getOrderNo());

        MyRecord record = new MyRecord();
        record.set("orderNo", masterOrder.getOrderNo());
        return record;
    }

    /**
     * 检测支付方式
     */
    private Boolean checkPayType(String payType) {
        ArrayList<String> list = CollUtil.newArrayList();
        list.add(PayConstants.PAY_TYPE_PAYPAL);
        list.add(PayConstants.PAY_TYPE_STRIPE);
        list.add(PayConstants.PAY_TYPE_WECHAT);
        return list.contains(payType);
    }

    /**
     * 游客订单详情
     * @param orderNo 订单号
     * @param identity 用户标识
     * @return StoreOrderDetailInfoResponse
     */
    @Override
    public StoreOrderDetailInfoResponse visitorStoreOrderDetail(String orderNo, String identity) {
//        User user = userService.getInfo();
//        if (ObjectUtil.isNotNull(user)) {
//            throw new CrmebException("不能查询非自己的订单");
//        }
        User currentUser = userService.getByIdentityAndType(identity, UserConstants.USER_LOGIN_TYPE_VISITOR);
        if (ObjectUtil.isNull(currentUser)) {
            throw new CrmebException("order does not exist");
        }
        StoreOrderDetailInfoResponse storeOrderDetailResponse = new StoreOrderDetailInfoResponse();
        // 查询订单
        StoreOrder storeOrder = storeOrderService.getByOderNo(orderNo);
        if (ObjectUtil.isNull(storeOrder) || storeOrder.getIsUserDel() || storeOrder.getIsMerchantDel()) {
            throw new CrmebException("order does not exist");
        }
        if (!storeOrder.getUid().equals(currentUser.getUid())) {
            throw new CrmebException("order does not exist");
        }

        BeanUtils.copyProperties(storeOrder, storeOrderDetailResponse);
        // 订单详情对象列表
        List<OrderInfoResponse> infoResponseList = CollUtil.newArrayList();
        List<StoreOrderInfo> infoList = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderNo());
        infoList.forEach(e -> {
            OrderInfoResponse orderInfoResponse = new OrderInfoResponse();
            BeanUtils.copyProperties(e, orderInfoResponse);
            infoResponseList.add(orderInfoResponse);
        });
        storeOrderDetailResponse.setOrderInfoList(infoResponseList);

        OrderLogistics expressInfo = orderLogisticsService.getByOrderNo(orderNo);
        storeOrderDetailResponse.setExpressInfo(expressInfo);

        // 商户名称
        Merchant merchant = merchantService.getById(storeOrder.getMerId());
        storeOrderDetailResponse.setMerName(merchant.getName());
        return storeOrderDetailResponse;
    }

    /**
     * 退款订单详情
     * @param orderNo 退款订单号
     * @return RefundOrderInfoResponse
     */
    @Override
    public RefundOrderInfoResponse refundOrderDetail(String orderNo) {
        StoreRefundOrder refundOrder = storeRefundOrderService.getByRefundOrderNo(orderNo);
        if (ObjectUtil.isNull(refundOrder)) {
            throw new CrmebException("Refund order does not exist");
        }
        RefundOrderInfoResponse infoResponse = new RefundOrderInfoResponse();
        BeanUtils.copyProperties(refundOrder, infoResponse);
        Merchant merchant = merchantService.getById(refundOrder.getMerId());
        infoResponse.setMerName(merchant.getName());
        List<StoreOrderInfo> orderInfoList = storeOrderInfoService.getListByOrderNo(refundOrder.getMerOrderNo());
        List<OrderInfoResponse> infoResponseList = orderInfoList.stream().map(e -> {
            OrderInfoResponse orderInfoResponse = new OrderInfoResponse();
            BeanUtils.copyProperties(e, orderInfoResponse);
            return orderInfoResponse;
        }).collect(Collectors.toList());
        infoResponse.setOrderInfoList(infoResponseList);
        return infoResponse;
    }

    /**
     * 退款订单列表
     * @param pageRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<RefundOrderResponse> getRefundOrderList(PageParamRequest pageRequest) {
        PageInfo<StoreRefundOrder> pageInfo = storeRefundOrderService.getH5List(pageRequest);
        List<StoreRefundOrder> refundOrderList = pageInfo.getList();
        if (CollUtil.isEmpty(refundOrderList)) {
            return CommonPage.copyPageInfo(pageInfo, CollUtil.newArrayList());
        }
        List<RefundOrderResponse> responseList = refundOrderList.stream().map(refundOrder -> {
            RefundOrderResponse response = new RefundOrderResponse();
            BeanUtils.copyProperties(refundOrder, response);
            List<StoreOrderInfo> orderInfoList = storeOrderInfoService.getListByOrderNo(refundOrder.getMerOrderNo());
            List<OrderInfoResponse> infoResponseList = orderInfoList.stream().map(e -> {
                OrderInfoResponse orderInfoResponse = new OrderInfoResponse();
                BeanUtils.copyProperties(e, orderInfoResponse);
                return orderInfoResponse;
            }).collect(Collectors.toList());
            response.setOrderInfoList(infoResponseList);
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(pageInfo, responseList);
    }

    /**
     * 订单商品评论列表
     * @param pageRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<InfoReplyResponse> replyList(PageParamRequest pageRequest) {
        Integer userId = userService.getUserId();
        PageInfo<StoreOrderInfo> pageInfo = storeOrderInfoService.getReplyList(userId, pageRequest);
        List<StoreOrderInfo> orderInfoList = pageInfo.getList();
        if (CollUtil.isEmpty(orderInfoList)) {
            return CommonPage.copyPageInfo(pageInfo, CollUtil.newArrayList());
        }
        List<Integer> merIdList = orderInfoList.stream().map(StoreOrderInfo::getMerId).distinct().collect(Collectors.toList());
        Map<Integer, Merchant> merchantMap = merchantService.getMerIdMapByIdList(merIdList);
        List<InfoReplyResponse> responseList = orderInfoList.stream().map(info -> {
            InfoReplyResponse replyResponse = new InfoReplyResponse();
            BeanUtils.copyProperties(info, replyResponse);
            replyResponse.setMerName(merchantMap.get(info.getMerId()).getName());
            return replyResponse;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(pageInfo, responseList);
    }

    /**
     * 待支付订单列表
     * @param pageRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<MasterOrderAwaitPayResponse> awaitPayList(PageParamRequest pageRequest) {
        Integer userId = userService.getUserIdException();
        PageInfo<MasterOrder> pageInfo = masterOrderService.getAwaitPayList(userId, pageRequest);
        List<MasterOrder> masterOrderList = pageInfo.getList();
        if (CollUtil.isEmpty(masterOrderList)) {
            return CommonPage.copyPageInfo(pageInfo, CollUtil.newArrayList());
        }
        List<MasterOrderAwaitPayResponse> responseList = masterOrderList.stream().map(masterOrder -> {
            MasterOrderAwaitPayResponse awaitPayResponse = new MasterOrderAwaitPayResponse();
            BeanUtils.copyProperties(masterOrder, awaitPayResponse);
            List<StoreOrder> storeOrderList = storeOrderService.getListByMasterNo(masterOrder.getOrderNo());
            List<PreStoreOrderVo> storeOrderVoList = storeOrderList.stream().map(storeOrder -> {
                Merchant merchant = merchantService.getById(storeOrder.getMerId());
                List<StoreOrderInfo> infoList = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderNo());
                PreStoreOrderVo orderVo = new PreStoreOrderVo();
                orderVo.setMerId(storeOrder.getMerId());
                orderVo.setMerName(merchant.getName());
                List<PreStoreOrderInfoVo> infoVoList = infoList.stream().map(info -> {
                    PreStoreOrderInfoVo infoVo = new PreStoreOrderInfoVo();
                    BeanUtils.copyProperties(info, infoVo);
                    return infoVo;
                }).collect(Collectors.toList());
                orderVo.setOrderInfoList(infoVoList);
                return orderVo;
            }).collect(Collectors.toList());
            awaitPayResponse.setOrderList(storeOrderVoList);
            return awaitPayResponse;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(pageInfo, responseList);
    }

    /**
     * 主订单详情
     * @param orderNo 主订单订单号
     * @return MasterOrderDetailResponse
     */
    @Override
    public MasterOrderDetailResponse masterOrderDetail(String orderNo) {
        Integer userId = userService.getUserId();
        if (userId <= 0) {
            throw new CrmebException("Please login and try again");
        }
        return getMasterOrderDetail(orderNo, userId);
    }

    /**
     * 主订单详情(游客订单查询)
     * @param orderNo 主订单号
     * @param identity 用户标识
     * @return MasterOrderDetailResponse
     */
    @Override
    public MasterOrderDetailResponse visitorMasterOrderDetail(String orderNo, String identity) {
        User currentUser = userService.getByIdentityAndType(identity, UserConstants.USER_LOGIN_TYPE_VISITOR);
        if (ObjectUtil.isNull(currentUser)) {
            throw new CrmebException("order does not exist");
        }
        return getMasterOrderDetail(orderNo, currentUser.getUid());
    }

    /**
     * 个人中心订单数量
     * @return CenterOrderDataResponse
     */
    @Override
    public CenterOrderDataResponse getCenterOrderData() {
        Integer userId = userService.getUserIdException();
        CenterOrderDataResponse response = new CenterOrderDataResponse();
        // 待支付订单数
        response.setUnPaidCount(masterOrderService.getAwaitPayCount(userId));
        // 待发货
        response.setUnShippedCount(storeOrderService.getTopDataUtil(OrderConstants.ORDER_STATUS_H5_NOT_SHIPPED, userId));
        // 待收货
        response.setReceivedCount(storeOrderService.getTopDataUtil(OrderConstants.ORDER_STATUS_H5_SPIKE, userId));
        // 待评价
        response.setCommentCount(storeOrderInfoService.getAwaitNumByUid(userId));
        // 待退款
        response.setRefundCount(storeRefundOrderService.getAwaitNumByUid(userId));
        return response;
    }

    /**
     * 获取主订单信息
     * @param orderNo 主订单号
     * @param uid 用户uid
     * @return MasterOrderDetailResponse
     */
    private MasterOrderDetailResponse getMasterOrderDetail(String orderNo, Integer uid) {
        MasterOrder masterOrder = masterOrderService.getByOrderNo(orderNo);
        if (ObjectUtil.isNull(masterOrder) || !masterOrder.getUid().equals(uid) || masterOrder.getIsCancel()) {
            throw new CrmebException("order does not exist");
        }
        MasterOrderDetailResponse response = new MasterOrderDetailResponse();
        BeanUtils.copyProperties(masterOrder, response);
        List<StoreOrder> storeOrderList = storeOrderService.getListByMasterNo(masterOrder.getOrderNo());
        List<MasterStoreOrderResponse> orderResponseList = storeOrderList.stream().map(storeOrder -> {
            Merchant merchant = merchantService.getById(storeOrder.getMerId());
            List<StoreOrderInfo> infoList = storeOrderInfoService.getListByOrderNo(storeOrder.getOrderNo());
            MasterStoreOrderResponse orderResponse = new MasterStoreOrderResponse();
            BeanUtils.copyProperties(storeOrder, orderResponse);
            orderResponse.setMerName(merchant.getName());
            List<OrderInfoResponse> infoResponseList = infoList.stream().map(info -> {
                OrderInfoResponse orderInfoResponse = new OrderInfoResponse();
                BeanUtils.copyProperties(info, orderInfoResponse);
                return orderInfoResponse;
            }).collect(Collectors.toList());
            orderResponse.setOrderInfoList(infoResponseList);
            return orderResponse;
        }).collect(Collectors.toList());
        response.setOrderList(orderResponseList);
        return response;
    }

    /**
     * 校验商品库存（生成订单）
     * @return List<MyRecord>
     */
    private List<MyRecord> validateProductStock(PreOrderVo preOrderVo) {
        List<MyRecord> recordList = CollUtil.newArrayList();
        List<PreStoreOrderVo> orderList = preOrderVo.getOrderList();
        List<PreStoreOrderInfoVo> orderInfoList = CollUtil.newArrayList();
        for (PreStoreOrderVo storeOrderVo : orderList) {
            orderInfoList.addAll(storeOrderVo.getOrderInfoList());
        }
        orderInfoList.forEach(e -> {
            // 查询商品信息
            StoreProduct storeProduct = storeProductService.getById(e.getProductId());
            if (ObjectUtil.isNull(storeProduct)) {
                throw new CrmebException("Purchased product information does not exist");
            }
            if (storeProduct.getIsDel()) {
                throw new CrmebException("Purchased item deleted");
            }
            if (!storeProduct.getIsShow() || storeProduct.getIsForced()) {
                throw new CrmebException("The purchased item has been discontinued");
            }
            if (storeProduct.getStock().equals(0) || e.getPayNum() > storeProduct.getStock()) {
                throw new CrmebException("The purchased item is out of stock");
            }
            // 查询商品规格属性值信息
            StoreProductAttrValue attrValue = attrValueService.getByIdAndProductIdAndType(e.getAttrValueId(), e.getProductId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
            if (ObjectUtil.isNull(attrValue)) {
                throw new CrmebException("The purchased product specification information does not exist");
            }
            if (attrValue.getStock() < e.getPayNum()) {
                throw new CrmebException("The purchased item is out of stock");
            }
            MyRecord record = new MyRecord();
            record.set("productId", e.getProductId());
            record.set("num", e.getPayNum());
            record.set("attrValueId", e.getAttrValueId());
            record.set("attrValueVersion", attrValue.getVersion());
            recordList.add(record);
        });
        return recordList;
    }


    /**
     * 校验预下单商品信息
     * @param request 预下单请求参数
     * @return OrderInfoVo
     */
    private PreOrderVo validatePreOrderRequest(PreOrderRequest request, User user) {
        PreOrderVo orderVo = new PreOrderVo();
        List<PreStoreOrderVo> storeOrderVoList = CollUtil.newArrayList();
        if (request.getPreOrderType().equals("shoppingCart")) {// 购物车购买
            storeOrderVoList = validatePreOrderShopping(request, user);
            List<Integer> cartIdList = request.getOrderDetails().stream().map(PreOrderDetailRequest::getShoppingCartId).distinct().collect(Collectors.toList());
            orderVo.setCartIdList(cartIdList);
        }
        if (request.getPreOrderType().equals("buyNow")) {// 立即购买
            // 立即购买只会有一条详情
            PreOrderDetailRequest detailRequest = request.getOrderDetails().get(0);
            // 普通商品
            if (ObjectUtil.isNull(detailRequest.getProductId())) {
                throw new CrmebException("Product ID cannot be empty");
            }
            if (ObjectUtil.isNull(detailRequest.getAttrValueId())) {
                throw new CrmebException("Product specification attribute value cannot be empty");
            }
            if (ObjectUtil.isNull(detailRequest.getProductNum()) || detailRequest.getProductNum() < 0) {
                throw new CrmebException("Purchase quantity must be greater than 0");
            }
            // 查询商品信息
            StoreProduct storeProduct = storeProductService.getById(detailRequest.getProductId());
            if (ObjectUtil.isNull(storeProduct)) {
                throw new CrmebException("Product information does not exist, please refresh and select again");
            }
            if (storeProduct.getIsDel()) {
                throw new CrmebException("The item has been deleted, please refresh and select again");
            }
            if (!storeProduct.getIsShow() || storeProduct.getIsForced()) {
                throw new CrmebException("The product has been removed, please refresh and select again");
            }
            if (storeProduct.getStock() < detailRequest.getProductNum()) {
                throw new CrmebException("Insufficient product stock, please refresh and select again");
            }
            // 查询商品规格属性值信息
            StoreProductAttrValue attrValue = attrValueService.getByIdAndProductIdAndType(detailRequest.getAttrValueId(), detailRequest.getProductId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
            if (ObjectUtil.isNull(attrValue)) {
                throw new CrmebException("The product specification information does not exist, please refresh and select again");
            }
            if (attrValue.getStock() < detailRequest.getProductNum()) {
                throw new CrmebException("Insufficient stock of product specifications, please refresh and select again");
            }
            Merchant merchant = merchantService.getByIdException(storeProduct.getMerId());
            if (!merchant.getIsSwitch()) {
                throw new CrmebException("The store is closed, please refresh and select again");
            }

            PreStoreOrderVo storeOrderVo = new PreStoreOrderVo();
            storeOrderVo.setMerId(merchant.getId());
            storeOrderVo.setMerName(merchant.getName());
            PreStoreOrderInfoVo infoVo = new PreStoreOrderInfoVo();
            infoVo.setProductId(storeProduct.getId());
            infoVo.setProductName(storeProduct.getStoreName());
            infoVo.setAttrValueId(attrValue.getId());
            infoVo.setImage(StrUtil.isNotBlank(attrValue.getImage()) ? attrValue.getImage() : storeProduct.getImage());
            infoVo.setSku(attrValue.getSku());
            infoVo.setPrice(attrValue.getPrice());
            infoVo.setPayNum(detailRequest.getProductNum());
            infoVo.setVolume(attrValue.getVolume());
            infoVo.setWeight(attrValue.getWeight());
            infoVo.setPostage(storeProduct.getPostage());
            List<PreStoreOrderInfoVo> infoList = CollUtil.newArrayList();
            infoList.add(infoVo);
            storeOrderVo.setOrderInfoList(infoList);
            storeOrderVoList.add(storeOrderVo);
        }
        orderVo.setOrderList(storeOrderVoList);
        return orderVo;
    }

    /**
     * 购物车预下单校验
     * @param request 请求参数
     * @param user 用户
     * @return List<OrderInfoDetailVo>
     */
    private List<PreStoreOrderVo> validatePreOrderShopping(PreOrderRequest request, User user) {
        List<PreStoreOrderVo> storeOrderVoList = CollUtil.newArrayList();
        request.getOrderDetails().forEach(e -> {
            if (ObjectUtil.isNull(e.getShoppingCartId())) {
                throw new CrmebException("Cart ID cannot be empty");
            }
            StoreCart storeCart = storeCartService.getByIdAndUid(e.getShoppingCartId(), user.getUid());
            if (ObjectUtil.isNull(storeCart)) {
                throw new CrmebException("No corresponding shopping cart information found");
            }
            // 查询商品信息
            StoreProduct storeProduct = storeProductService.getById(storeCart.getProductId());
            if (ObjectUtil.isNull(storeProduct)) {
                throw new CrmebException("Product information does not exist, please refresh and select again");
            }
            if (storeProduct.getIsDel()) {
                throw new CrmebException("The item has been deleted, please refresh and select again");
            }
            if (!storeProduct.getIsShow() || storeProduct.getIsForced()) {
                throw new CrmebException("The product has been removed, please refresh and select again");
            }
            if (storeProduct.getStock() < storeCart.getCartNum()) {
                throw new CrmebException("Insufficient product stock, please refresh and select again");
            }
            // 判断门店信息
            Merchant merchant = merchantService.getByIdException(storeProduct.getMerId());
            if (!merchant.getIsSwitch()) {
                throw new CrmebException("The product store is closed, please refresh and select again");
            }
            // 查询商品规格属性值信息
            StoreProductAttrValue attrValue = attrValueService.getByIdAndProductIdAndType(storeCart.getProductAttrUnique(), storeCart.getProductId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
            if (ObjectUtil.isNull(attrValue)) {
                throw new CrmebException("The product specification information does not exist, please refresh and select again");
            }
            if (attrValue.getStock() < storeCart.getCartNum()) {
                throw new CrmebException("Insufficient stock of product specifications, please refresh and select again");
            }

            if (storeOrderVoList.stream().anyMatch(o -> o.getMerId().equals(merchant.getId()))) {
                for (PreStoreOrderVo orderVo : storeOrderVoList) {
                    if (orderVo.getMerId().equals(merchant.getId())) {
                        PreStoreOrderInfoVo infoVo = new PreStoreOrderInfoVo();
                        infoVo.setProductId(storeProduct.getId());
                        infoVo.setProductName(storeProduct.getStoreName());
                        infoVo.setAttrValueId(attrValue.getId());
                        infoVo.setImage(StrUtil.isNotBlank(attrValue.getImage()) ? attrValue.getImage() : storeProduct.getImage());
                        infoVo.setSku(attrValue.getSku());
                        infoVo.setPrice(attrValue.getPrice());
                        infoVo.setPayNum(storeCart.getCartNum());
                        infoVo.setVolume(attrValue.getVolume());
                        infoVo.setWeight(attrValue.getWeight());
                        infoVo.setPostage(storeProduct.getPostage());
                        orderVo.getOrderInfoList().add(infoVo);
                        break;
                    }
                }
            } else {
                PreStoreOrderVo storeOrderVo = new PreStoreOrderVo();
                storeOrderVo.setMerId(merchant.getId());
                storeOrderVo.setMerName(merchant.getName());
                PreStoreOrderInfoVo infoVo = new PreStoreOrderInfoVo();
                infoVo.setProductId(storeProduct.getId());
                infoVo.setProductName(storeProduct.getStoreName());
                infoVo.setAttrValueId(attrValue.getId());
                infoVo.setImage(StrUtil.isNotBlank(attrValue.getImage()) ? attrValue.getImage() : storeProduct.getImage());
                infoVo.setSku(attrValue.getSku());
                infoVo.setPrice(attrValue.getPrice());
                infoVo.setPayNum(storeCart.getCartNum());
                infoVo.setVolume(attrValue.getVolume());
                infoVo.setWeight(attrValue.getWeight());
                infoVo.setPostage(storeProduct.getPostage());
                List<PreStoreOrderInfoVo> infoList = CollUtil.newArrayList();
                infoList.add(infoVo);
                storeOrderVo.setOrderInfoList(infoList);
                storeOrderVoList.add(storeOrderVo);
            }
        });
        return storeOrderVoList;
    }

    private StoreOrder getByOrderNoException(String orderNo) {
        StoreOrder storeOrder = storeOrderService.getByOderNo(orderNo);
        if (ObjectUtil.isNull(storeOrder)) {
            throw new CrmebException("order does not exist");
        }
        return storeOrder;
    }

    /**
     * 计算订单运费
     */
    private BigDecimal getFreightFee(List<PreStoreOrderInfoVo> orderInfoList) {
        // 运费 = 单件商品运费 * 商品数量
        BigDecimal storePostage = BigDecimal.ZERO;
        for (PreStoreOrderInfoVo detailVo : orderInfoList) {
            storePostage = storePostage.add(detailVo.getPostage().multiply(new BigDecimal(detailVo.getPayNum())));
        }
        return storePostage;
    }

    private ComputedOrderPriceResponse computedPrice(OrderComputedPriceRequest request, PreOrderVo preOrderVo, User user) {
        // 计算各种价格
        ComputedOrderPriceResponse priceResponse = new ComputedOrderPriceResponse();
        priceResponse.setProTotalFee(preOrderVo.getProTotalFee());
        // 计算运费
        List<PreStoreOrderInfoVo> orderInfoList = new ArrayList<>();
        for (PreStoreOrderVo orderVo : preOrderVo.getOrderList()) {
            orderInfoList.addAll(orderVo.getOrderInfoList());
        }
        preOrderVo.setFreightFee(getFreightFee(orderInfoList));
        priceResponse.setFreightFee(preOrderVo.getFreightFee());
        // 计算优惠券金额
        if (ObjectUtil.isNull(request.getCouponId()) || request.getCouponId() <= 0) {
            priceResponse.setCouponFee(BigDecimal.ZERO);
        } else {
            // 判断优惠券是否可以使用
            StoreCouponUser storeCouponUser = storeCouponUserService.getById(request.getCouponId());
            if (ObjectUtil.isNull(storeCouponUser) || !storeCouponUser.getUid().equals(user.getUid())) {
                throw new CrmebException("Coupon claim record does not exist！");
            }
            if (storeCouponUser.getStatus().equals(CouponConstants.STORE_COUPON_USER_STATUS_USED)) {
                throw new CrmebException("This coupon has already been used！");
            }

            if (storeCouponUser.getStatus().equals(CouponConstants.STORE_COUPON_USER_STATUS_LAPSED)) {
                throw new CrmebException("This coupon has expired！");
            }
            //判断是否在使用时间内
            Date date = DateUtil.nowDateTime();
            if (storeCouponUser.getStartTime().compareTo(date) > 0) {
                throw new CrmebException("This coupon has not expired within the validity period！");
            }
            if (date.compareTo(storeCouponUser.getEndTime()) > 0) {
                throw new CrmebException("This coupon has expired");
            }
            //检测优惠券信息
            if (storeCouponUser.getUseType().equals(CouponConstants.COUPON_USE_TYPE_PRODUCT)) {
                // 商品券
                List<Integer> productIdList = orderInfoList.stream().map(PreStoreOrderInfoVo::getProductId).collect(Collectors.toList());
                if (productIdList.size() < 1) {
                    throw new CrmebException("No item found product");
                }
                //设置优惠券所提供的集合
                List<Integer> primaryKeyIdList = CrmebUtil.stringToArray(storeCouponUser.getPrimaryKey());
                //取两个集合的交集，如果是false则证明没有相同的值
                //oldList.retainAll(newList)返回值代表oldList是否保持原样，如果old和new完全相同，那old保持原样并返回false。
                //交集：listA.retainAll(listB) ——listA内容变为listA和listB都存在的对象；listB不变
                primaryKeyIdList.retainAll(productIdList);
                if (CollUtil.isEmpty(primaryKeyIdList)) {
                    throw new CrmebException("This coupon is a commodity coupon, please use it after purchasing related products！");
                }
                List<PreStoreOrderInfoVo> infoList = orderInfoList.stream().filter(info -> primaryKeyIdList.contains(info.getProductId())).collect(Collectors.toList());
                if (CollUtil.isEmpty(infoList)) {
                    throw new CrmebException("This coupon is a commodity coupon, please use it after purchasing related products！");
                }
                BigDecimal proTotalPrice = infoList.stream().map(e -> e.getPrice().multiply(new BigDecimal(e.getPayNum()))).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (storeCouponUser.getMinPrice().compareTo(proTotalPrice) > 0) {
                    throw new CrmebException("The total amount is less than the minimum coupon usage amount");
                }
                if (proTotalPrice.compareTo(storeCouponUser.getMoney()) > 0) {
                    priceResponse.setCouponFee(storeCouponUser.getMoney());
                } else {
                    priceResponse.setCouponFee(proTotalPrice);
                }
            }
            if (storeCouponUser.getUseType().equals(CouponConstants.COUPON_USE_TYPE_MERCHANT)) {
                // 商家券
                List<Integer> merIdList = preOrderVo.getOrderList().stream().map(PreStoreOrderVo::getMerId).collect(Collectors.toList());
                if (!merIdList.contains(storeCouponUser.getMerId())) {
                    throw new CrmebException("This coupon is a merchant coupon, please use it after purchasing related products！");
                }
                preOrderVo.getOrderList().forEach(e -> {
                    if (e.getMerId().equals(storeCouponUser.getMerId())) {
                        List<PreStoreOrderInfoVo> infoList = e.getOrderInfoList();
                        BigDecimal proTotalPrice = infoList.stream().map(i -> i.getPrice().multiply(new BigDecimal(i.getPayNum()))).reduce(BigDecimal.ZERO, BigDecimal::add);
                        if (storeCouponUser.getMinPrice().compareTo(proTotalPrice) > 0) {
                            throw new CrmebException("The total amount is less than the minimum coupon usage amount");
                        }
                        if (proTotalPrice.compareTo(storeCouponUser.getMoney()) > 0) {
                            priceResponse.setCouponFee(storeCouponUser.getMoney());
                        } else {
                            priceResponse.setCouponFee(proTotalPrice);
                        }
                    }
                });
            }
        }

        if (priceResponse.getProTotalFee().compareTo(priceResponse.getCouponFee()) <= 0) {
            throw new CrmebException("The discount amount cannot be greater than the total price of the product");
        }
        BigDecimal payPrice = priceResponse.getProTotalFee().add(priceResponse.getFreightFee()).subtract(priceResponse.getCouponFee());
        priceResponse.setPayFee(payPrice);
        return priceResponse;
    }
}
