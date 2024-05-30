package com.zbkj.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.service.service.MasterOrderService;
import com.zbkj.service.service.StripeService;
import com.zbkj.service.service.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Stripe支付服务实现类
 *
 * @Author 指缝de阳光
 * @Date 2022/6/1 17:13
 * @Version 1.0
 */
@Service
public class StripeServiceImpl implements StripeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StripeServiceImpl.class);

    @Autowired
    private CrmebConfig crmebConfig;
    @Autowired
    private MasterOrderService masterOrderService;
    @Autowired
    private SystemConfigService systemConfigService;

    private Gson gson = new Gson();

    @Override
    public PaymentIntent paymentIntents(MasterOrder masterOrder) {
        configureApiKey();
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(masterOrder.getPayPrice().multiply(new BigDecimal("100")).longValue()) // 最小单位，美元格式为美分
                .setCurrency("usd") //美元
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .setDescription(crmebConfig.getPaypalBrandName())
                .build();

        // Create a PaymentIntent with the order amount and currency
        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.create(params);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new CrmebException("stripe payment intent error");
        }
        return paymentIntent;
    }

    private void configureApiKey() {
        String stripeApiKey = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_STRIPE_API_KEY);
        if (StrUtil.isBlank(stripeApiKey)) {
            throw new CrmebException("请先配置stripe apiKey");
        }
        Stripe.apiKey = stripeApiKey;
    }

    /**
     * 检索payment
     *
     * @param outTradeNo stripeId
     * @return PaymentIntent
     */
    @Override
    public PaymentIntent retrieve(String outTradeNo) {
        configureApiKey();
        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.retrieve(outTradeNo);
        } catch (StripeException e) {
            e.printStackTrace();
            throw new CrmebException("stripe payment retrieve error");
        }
        return paymentIntent;
    }

    @Override
    public Boolean refund(StoreOrder storeOrder) {
        configureApiKey();
        MasterOrder masterOrder = masterOrderService.getByOrderNo(storeOrder.getMasterOrderNo());
        RefundCreateParams params = RefundCreateParams
                .builder()
                .setPaymentIntent(masterOrder.getOutTradeNo())
                .setAmount(storeOrder.getPayPrice().multiply(new BigDecimal("100")).longValue())
                .build();
        Refund refund = null;
        try {
            refund = Refund.create(params);
        } catch (StripeException e) {
            e.printStackTrace();
            LOGGER.error("stripe refund exception : " + e.getMessage());
            return Boolean.FALSE;
        }
        LOGGER.info("stripe refund : " + gson.toJson(refund));
        return Boolean.TRUE;
    }
}
