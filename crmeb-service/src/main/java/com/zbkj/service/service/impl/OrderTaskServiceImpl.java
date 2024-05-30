package com.zbkj.service.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zbkj.common.constants.OrderConstants;
import com.zbkj.common.constants.TaskConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.OrderLogistics;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreRefundOrder;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.common.vo.LogisticsApiResponseVo;
import com.zbkj.service.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * StoreOrderServiceImpl 接口实现
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
public class OrderTaskServiceImpl implements OrderTaskService {
    //日志
    private static final Logger logger = LoggerFactory.getLogger(OrderTaskServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StoreOrderTaskService storeOrderTaskService;

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private StoreOrderStatusService storeOrderStatusService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private OrderLogisticsService orderLogisticsService;

    @Autowired
    private LogisticService logisticService;

    @Autowired
    private MasterOrderService masterOrderService;

    @Autowired
    private StoreRefundOrderService refundOrderService;

    @Autowired
    private StoreOrderInfoService storeOrderInfoService;

    /**
     * 用户取消订单
     */
    @Override
    public void cancelByUser() {
        String redisKey = TaskConstants.ORDER_TASK_REDIS_KEY_AFTER_CANCEL_BY_USER;
        Long size = redisUtil.getListSize(redisKey);
        logger.info("OrderTaskServiceImpl.cancelByUser | size:" + size);
        if (size < 1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            //如果10秒钟拿不到一个数据，那么退出循环
            Object data = redisUtil.getRightPop(redisKey, 10L);
            if (null == data) {
                continue;
            }
            try {
                MasterOrder masterOrder = masterOrderService.getByOrderNo(String.valueOf(data));
                boolean result = storeOrderTaskService.cancelByUser(masterOrder);
                if (!result) {
                    redisUtil.lPush(redisKey, data);
                }
            } catch (Exception e) {
                redisUtil.lPush(redisKey, data);
            }
        }
    }

    /**
     * 执行 用户退款申请
     * @author Mr.Zhang
     * @since 2020-07-09
     */
    @Override
    public void refundApply() {
        String redisKey = TaskConstants.ORDER_TASK_REDIS_KEY_AFTER_REFUND_BY_USER;
        Long size = redisUtil.getListSize(redisKey);
        logger.info("OrderTaskServiceImpl.refundApply | size:" + size);
        if (size < 1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            //如果10秒钟拿不到一个数据，那么退出循环
            Object data = redisUtil.getRightPop(redisKey, 10L);
            if (null == data) {
                continue;
            }
            try {
                StoreRefundOrder refundOrder = refundOrderService.getByRefundOrderNo(String.valueOf(data));
                if (ObjectUtil.isNull(refundOrder)) {
                    throw new CrmebException("退款订单不存在,refundOrderNo = " + data);
                }
                boolean result = storeOrderTaskService.refundOrder(refundOrder);
                if (!result) {
                    logger.error("订单退款后续错误：result = " + result);
                    redisUtil.lPush(redisKey, data);
                }
            } catch (Exception e) {
                logger.error("订单退款后续错误：" + e.getMessage());
                redisUtil.lPush(redisKey, data);
            }
        }
    }

    /**
     * 订单支付成功后置处理
     */
    @Override
    public void orderPaySuccessAfter() {
        String redisKey = TaskConstants.ORDER_TASK_PAY_SUCCESS_AFTER;
        Long size = redisUtil.getListSize(redisKey);
        logger.info("OrderTaskServiceImpl.orderPaySuccessAfter | size:" + size);
        if (size < 1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            //如果10秒钟拿不到一个数据，那么退出循环
            Object data = redisUtil.getRightPop(redisKey, 10L);
            if (ObjectUtil.isNull(data)) {
                continue;
            }
            try {
                MasterOrder masterOrder = masterOrderService.getByOrderNo(String.valueOf(data));
                if (ObjectUtil.isNull(masterOrder)) {
                    logger.error("OrderTaskServiceImpl.orderPaySuccessAfter | 订单不存在，orderNo: " + data);
                    throw new CrmebException("订单不存在，orderNo: " + data);
                }
                boolean result = orderPayService.paySuccess(masterOrder);
                if (!result) {
                    redisUtil.lPush(redisKey, data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                redisUtil.lPush(redisKey, data);
            }
        }
    }

    /**
     * 自动取消未支付订单
     */
    @Override
    public void autoCancel() {
        String redisKey = TaskConstants.ORDER_TASK_REDIS_KEY_AUTO_CANCEL_KEY;
        Long size = redisUtil.getListSize(redisKey);
        logger.info("OrderTaskServiceImpl.autoCancel | size:" + size);
        if (size < 1) {
            return;
        }
        for (int i = 0; i < size; i++) {
            //如果10秒钟拿不到一个数据，那么退出循环
            Object data = redisUtil.getRightPop(redisKey, 10L);
            if (null == data) {
                continue;
            }
            try {
                MasterOrder masterOrder = masterOrderService.getByOrderNo(String.valueOf(data));
                if (ObjectUtil.isNull(masterOrder)) {
                    logger.error("OrderTaskServiceImpl.autoCancel | 订单不存在，orderNo: " + data);
                    throw new CrmebException("订单不存在，orderNo: " + data);
                }
                boolean result = storeOrderTaskService.autoCancel(masterOrder);
                if (!result) {
                    redisUtil.lPush(redisKey, data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                redisUtil.lPush(redisKey, data);
            }
        }
    }

    /**
     * 订单自动收货
     */
    @Override
    public void autoReceiving() {
        // 查找物流记录中需要查询的订单信息
        List<OrderLogistics> logisticsList = orderLogisticsService.findAllDelivery();
        if (CollUtil.isEmpty(logisticsList)) {
            return ;
        }
        logger.info("OrderTaskServiceImpl.autoReceiving | size:0");
        // 依次更新物流轨迹
        for (int i = 0; i < logisticsList.size();) {
            OrderLogistics logistics = logisticsList.get(i);
            String expNo = logistics.getExpNo();
            if (logistics.getExpCode().equals("SF")) {
                StoreOrder order = storeOrderService.getByOderNo(logistics.getOrderNo());
                String userPhone = order.getUserPhone();
                userPhone = userPhone.substring(userPhone.length() - 4);
                expNo = expNo.concat(":").concat(userPhone);
            }
            LogisticsApiResponseVo responseVo = logisticService.query(expNo, logistics.getExpCode());
            if (ObjectUtil.isNull(responseVo)) {
                logisticsList.remove(i);
                continue ;
            }
            logistics.setLogisticsInfo(responseVo.getTraces());
            logistics.setState(Integer.valueOf(responseVo.getState()));
            logistics.setTrackTime(responseVo.getUpdateTime());
            if (StrUtil.isNotBlank(responseVo.getCourier())) {
                logistics.setCourier(responseVo.getCourier());
            }
            if (StrUtil.isNotBlank(responseVo.getCourierPhone())) {
                logistics.setCourierPhone(responseVo.getCourierPhone());
            }
            if (StrUtil.isNotBlank(responseVo.getReason())) {
                logistics.setReason(responseVo.getReason());
            }
            logistics.setUpdateTime(DateUtil.nowDateTime());
            i++;
        }
//        for (OrderLogistics logistics : logisticsList) {
//            String expNo = logistics.getExpNo();
//            if (logistics.getExpCode().equals("SF")) {
//                StoreOrder order = storeOrderService.getByOderNo(logistics.getOrderNo());
//                String userPhone = order.getUserPhone();
//                userPhone = userPhone.substring(userPhone.length() - 4);
//                expNo = expNo.concat(":").concat(userPhone);
//            }
//            LogisticsApiResponseVo responseVo = logisticService.query(expNo, logistics.getExpCode());
//            if (ObjectUtil.isNull(responseVo)) {
//                continue ;
//            }
//            logistics.setLogisticsInfo(responseVo.getTraces());
//            logistics.setState(Integer.valueOf(responseVo.getState()));
//            logistics.setTrackTime(responseVo.getUpdateTime());
//            if (StrUtil.isNotBlank(responseVo.getCourier())) {
//                logistics.setCourier(responseVo.getCourier());
//            }
//            if (StrUtil.isNotBlank(responseVo.getCourierPhone())) {
//                logistics.setCourierPhone(responseVo.getCourierPhone());
//            }
//            if (StrUtil.isNotBlank(responseVo.getReason())) {
//                logistics.setReason(responseVo.getReason());
//            }
//            logistics.setUpdateTime(DateUtil.nowDateTime());
//        }

        // 获取可以执行自动收货的订单
        List<String> orderNoList = logisticsList.stream().filter(e -> e.getState().equals(3)).map(OrderLogistics::getOrderNo).collect(Collectors.toList());
        if (CollUtil.isEmpty(orderNoList)) {
            orderLogisticsService.updateBatchById(logisticsList, 100);
            logger.info("订单自动收货完成，更新{}条物流", logisticsList.size());
            return ;
        }
        List<StoreOrder> orderList = storeOrderService.findByOrderNoList(orderNoList);
        if (CollUtil.isEmpty(orderList)) {
            orderLogisticsService.updateBatchById(logisticsList, 100);
            logger.info("订单自动收货完成，更新{}条物流", logisticsList.size());
            return ;
        }
        for (int i = 0; i < orderList.size();) {
            StoreOrder storeOrder = orderList.get(i);
            if (storeOrder.getStatus().equals(OrderConstants.ORDER_STATUS_OVER)) {
                logger.warn("订单号{}的订单已完成", storeOrder.getOrderNo());
                orderList.remove(i);
                continue;
            }
            storeOrder.setStatus(OrderConstants.ORDER_STATUS_OVER);
            i++;
        }
        Boolean execute = transactionTemplate.execute(e -> {
            orderLogisticsService.updateBatchById(logisticsList, 100);
            storeOrderService.updateBatchById(orderList, 100);
            orderList.forEach(order -> {
                storeOrderInfoService.orderReceipt(order.getOrderNo());
            });
            return Boolean.TRUE;
        });
        if (!execute) {
            logger.error("订单自动收货事务失败");
        }
        orderList.forEach(e -> {
            // 日志
            storeOrderStatusService.createLog(e.getOrderNo(), OrderConstants.ORDER_LOG_RECEIVING, OrderConstants.ORDER_LOG_MESSAGE_RECEIVING);
        });
        logger.info("订单自动收货完成，更新{}条物流", logisticsList.size());
        logger.info("订单自动收货完成，更新{}单订单", orderList.size());
    }
}
