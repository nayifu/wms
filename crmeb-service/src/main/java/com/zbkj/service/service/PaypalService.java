package com.zbkj.service.service;

import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrder;

import java.io.IOException;

/**
 * @Author 指缝de阳光
 * @Date 2022/1/19 23:15
 * @Version 1.0
 */
public interface PaypalService {

    /**
     * 生成PayPal订单
     * @param masterOrder 订单信息
     * @return PayPal重定向地址
     */
    String createPayPalOrder(MasterOrder masterOrder);

    /**
     * 核销PayPal订单
     */
    void captureOrder(MasterOrder masterOrder) throws IOException;

    /**
     * 订单退款
     * @param storeOrder 订单
     * @return Boolean
     */
    Boolean refund(StoreOrder storeOrder);
}
