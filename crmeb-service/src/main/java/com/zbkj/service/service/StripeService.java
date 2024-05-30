package com.zbkj.service.service;

import com.stripe.model.PaymentIntent;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrder;

/**
 * Stripe支付服务
 * @Author 指缝de阳光
 * @Date 2022/6/1 17:11
 * @Version 1.0
 */
public interface StripeService {

    /**
     * 创建payment
     * @param masterOrder 主订单
     * @return PaymentIntent
     */
    PaymentIntent paymentIntents(MasterOrder masterOrder);

    /**
     * 检索payment
     * @param outTradeNo stripeId
     * @return PaymentIntent
     */
    PaymentIntent retrieve(String outTradeNo);

    Boolean refund(StoreOrder storeOrder);
}
