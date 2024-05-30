package com.zbkj.service.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.StrUtil;
import com.zbkj.common.constants.*;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.bill.Bill;
import com.zbkj.common.model.bill.MerchantBill;
import com.zbkj.common.model.coupon.StoreCouponUser;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreOrderInfo;
import com.zbkj.common.model.order.StoreRefundOrder;
import com.zbkj.common.model.product.StoreProductAttrValue;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.service.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * StoreOrderTaskService实现类
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
public class StoreOrderTaskServiceImpl implements StoreOrderTaskService {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(StoreOrderTaskServiceImpl.class);

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private StoreOrderInfoService storeOrderInfoService;

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private StoreOrderStatusService storeOrderStatusService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private StoreProductAttrValueService attrValueService;

    @Autowired
    private StoreCouponUserService couponUserService;

    @Autowired
    private MasterOrderService masterOrderService;
    @Autowired
    private BillService billService;
    @Autowired
    private MerchantBillService merchantBillService;

    /**
     * 用户取消订单
     */
    @Override
    public Boolean cancelByUser(MasterOrder masterOrder) {
        return transactionTemplate.execute(e -> {
            commonCancel(masterOrder);
            return Boolean.TRUE;
        });
    }

    /**
     * 订单取消公共操作
     * @param masterOrder 主订单
     */
    private void commonCancel(MasterOrder masterOrder) {
        //写订单日志
        List<StoreOrder> orderList = storeOrderService.getListByMasterNo(masterOrder.getOrderNo());
        orderList.forEach(order -> {
            storeOrderStatusService.createLog(order.getOrderNo(), OrderConstants.ORDER_LOG_CANCEL, OrderConstants.ORDER_LOG_MESSAGE_CANCEL);
        });
        // 退优惠券
        if (masterOrder.getCouponId() > 0 ) {
            StoreCouponUser couponUser = couponUserService.getById(masterOrder.getCouponId());
            couponUser.setStatus(CouponConstants.STORE_COUPON_USER_STATUS_USABLE);
            couponUserService.updateById(couponUser);
        }
        // 回滚库存
        String orderNoStr = orderList.stream().map(StoreOrder::getOrderNo).collect(Collectors.joining(","));
        rollbackStock(orderNoStr);
    }

//    /**
//     * 完成订单
//     * @author Mr.Zhang
//     * @since 2020-07-09
//     */
//    @Override
//    @Transactional(rollbackFor = {RuntimeException.class, Error.class, CrmebException.class})
//    public Boolean complete(StoreOrder storeOrder) {
//        /*
//         * 1、修改订单状态 （用户操作的时候已处理）
//         * 2、写订单日志
//         * */
//        try{
//            storeOrderStatusService.createLog(storeOrder.getId(), OrderConstants.ORDER_LOG_OVER, OrderConstants.ORDER_LOG_MESSAGE_OVER);
//            return true;
//        }catch (Exception e){
//            return false;
//        }
//    }

    /**
     * 回滚库存
     * @param orderNoStr 订单号字符串（英文逗号分隔）
     */
    private Boolean rollbackStock(String orderNoStr) {
        try{
            // 查找出商品详情
            List<StoreOrderInfo> orderInfoList = storeOrderInfoService.getListByOrderNoList(CrmebUtil.stringToArrayStr(orderNoStr));
            if(null == orderInfoList || orderInfoList.size() < 1){
                return true;
            }
            // 正常商品回滚销量库存
            for (StoreOrderInfo orderInfoVo : orderInfoList) {
                storeProductService.operationStock(orderInfoVo.getProductId(), orderInfoVo.getPayNum(), "add");
                StoreProductAttrValue attrValue = attrValueService.getById(orderInfoVo.getAttrValueId());
                attrValueService.operationStock(attrValue.getId(), orderInfoVo.getPayNum(), "add", ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL, attrValue.getVersion());
            }
        } catch (Exception e){
            throw new CrmebException("回滚库存失败，error = " + e.getMessage());
        }
        return true;
    }

    /**
     * 订单退款处理
     */
    @Override
    public Boolean refundOrder(StoreRefundOrder refundOrder) {
        /**
         * 1、回滚库存
         * 2、退优惠券
         * 3、生成帐单记录
         */
        StoreOrder storeOrder = storeOrderService.getByOderNo(refundOrder.getMerOrderNo());

        // 帐单记录
        Bill bill = refundBillInit(refundOrder);
        MerchantBill merchantBill= refundMerchantBillInit(refundOrder);

        Boolean execute = transactionTemplate.execute(e -> {
            // 回滚库存
            rollbackStock(refundOrder.getMerOrderNo());
            // 退优惠券
            if (storeOrder.getCouponId() > 0 ) {
                StoreCouponUser couponUser = couponUserService.getById(storeOrder.getCouponId());
                couponUser.setStatus(CouponConstants.STORE_COUPON_USER_STATUS_USABLE);
                couponUserService.updateById(couponUser);
            }
            // 帐单记录
            billService.save(bill);
            merchantBill.setPid(bill.getId());
            merchantBillService.save(merchantBill);
            return Boolean.TRUE;
        });
        return execute;
    }

    private MerchantBill refundMerchantBillInit(StoreRefundOrder refundOrder) {
        MerchantBill bill = new MerchantBill();
        bill.setUid(refundOrder.getUid());
        bill.setMerId(refundOrder.getMerId());
        bill.setLinkId(refundOrder.getId());
        bill.setOrderNo(refundOrder.getRefundOrderNo());
        bill.setPm(1);
        bill.setAmount(refundOrder.getRefundPrice());
        bill.setType(BillConstants.USER_BILL_TYPE_PAY_ORDER);
        bill.setMark("订单退款" + refundOrder.getRefundPrice() + "元");
        return bill;
    }

    private Bill refundBillInit(StoreRefundOrder refundOrder) {
        Bill bill = new Bill();
        bill.setUid(refundOrder.getUid());
        bill.setLinkId(refundOrder.getId());
        bill.setOrderNo(refundOrder.getRefundOrderNo());
        bill.setPm(1);
        bill.setAmount(refundOrder.getRefundPrice());
        bill.setType(BillConstants.USER_BILL_TYPE_REFUND_ORDER);
        bill.setMark("订单退款" + refundOrder.getRefundPrice() + "元");
        return bill;
    }

    /**
     * 超时未支付系统自动取消
     */
    @Override
    public Boolean autoCancel(MasterOrder masterOrder) {
        // 判断订单是否支付
        if (masterOrder.getPaid()) {
            return Boolean.TRUE;
        }
        if (masterOrder.getIsCancel()) {
            return Boolean.TRUE;
        }
        // 获取过期时间
        String cancelStr = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_ORDER_CANCEL_TIME);
        if (StrUtil.isBlank(cancelStr)) {
            cancelStr = "1";
        }
        DateTime cancelTime = cn.hutool.core.date.DateUtil.offset(masterOrder.getCreateTime(), DateField.HOUR_OF_DAY, Integer.parseInt(cancelStr));
        long between = cn.hutool.core.date.DateUtil.between(cancelTime, cn.hutool.core.date.DateUtil.date(), DateUnit.SECOND, false);
        if (between < 0) {// 未到过期时间继续循环
            return Boolean.FALSE;
        }

        return transactionTemplate.execute(e -> {
            masterOrderService.cancel(masterOrder.getOrderNo());
            storeOrderService.cancelByMasterNo(masterOrder.getOrderNo(), false);
            commonCancel(masterOrder);
            return Boolean.TRUE;
        });
    }

}
