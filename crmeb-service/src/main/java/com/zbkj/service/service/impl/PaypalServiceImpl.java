package com.zbkj.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.payments.CapturesRefundRequest;
import com.paypal.payments.RefundRequest;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.PayPalCheckoutConstant;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.service.service.MasterOrderService;
import com.zbkj.service.service.PaypalService;
import com.zbkj.service.service.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 指缝de阳光
 * @Date 2022/1/19 23:15
 * @Version 1.0
 */
@Service
public class PaypalServiceImpl implements PaypalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaypalServiceImpl.class);

    @Autowired
    private MasterOrderService masterOrderService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private CrmebConfig crmebConfig;

    /**
     * 获取PayPal客户端
     */
    private PayPalHttpClient getClient() {
        String clientId = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_PAYPAL_CLIENT_ID);
        if (StrUtil.isBlank(clientId)) {
            throw new CrmebException("请先配置PayPal支付信息");
        }
        String clientSecret = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_PAYPAL_CLIENT_SECRET);
        if (StrUtil.isBlank(clientSecret)) {
            throw new CrmebException("请先配置PayPal支付信息");
        }
        String mode = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_PAYPAL_MODE);
        if (!"live".equals(mode) && !"sandbox".equals(mode)) {
            throw new CrmebException("PayPal mode 配置不正确");
        }
        PayPalEnvironment environment = mode.equals("live") ? new PayPalEnvironment.Live(clientId, clientSecret) : new PayPalEnvironment.Sandbox(clientId, clientSecret);
        return new PayPalHttpClient(environment);
    }

    /**
     * 生成paypal订单
     * @param masterOrder 订单信息
     * @return 用户授权地址
     */
    @Override
    public String createPayPalOrder(MasterOrder masterOrder) {
        OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");
        request.requestBody(buildRequestBody(masterOrder));
        //3. Call PayPal to set up a transaction
        HttpResponse<Order> response = null;
        PayPalHttpClient payPalHttpClient = getClient();
        try {
            response = payPalHttpClient.execute(request);
        } catch (IOException e) {
            try {
                LOGGER.error("第1次调用paypal订单创建失败");
                response = payPalHttpClient.execute(request);
            } catch (Exception e1) {
                try {
                    LOGGER.error("第2次调用paypal订单创建失败");
                    response = payPalHttpClient.execute(request);
                } catch (Exception e2) {
                    LOGGER.error("第3次调用paypal订单创建失败，失败原因：{}", e2.getMessage());
                    LOGGER.error(e2.getMessage(), e2);
                }
            }
        }
        String approveUrl = "";
        if (response.statusCode() == 201) {
            LOGGER.info("Status Code = {}, Status = {}, OrderID = {}, Intent = {}", response.statusCode(), response.result().status(), response.result().id(), response.result().checkoutPaymentIntent());
            for (LinkDescription link : response.result().links()) {
                LOGGER.info("Links-{}: {}    \tCall Type: {}", link.rel(), link.href(), link.method());
                if (link.rel().equals("approve")) {
                    // 客户付款登陆地址【判断币种CNY无法交易】
                    //  String paymentId = payment.getId();
                    //  orderPaymMap.put(orderId, paymentId);

                    // 保存本地OrderNo和paymentId对应关系
                    masterOrderService.addOutTradeNo(masterOrder.getOrderNo(), response.result().id());

                    approveUrl = link.href();
//                    log.info("创建支付订单返回paymentId : " + paymentId);
//                    log.info("支付订单状态state : " + payment.getState());
//                    log.info("支付订单创建时间create_time : " + payment.getCreateTime());
                    LOGGER.info("=========================================================================================paypal订单创建成功");
//                    return "redirect:" + approve;
                }
            }
            LOGGER.info("Total Amount: " + response.result().purchaseUnits().get(0).amountWithBreakdown().currencyCode()
                    + " " + response.result().purchaseUnits().get(0).amountWithBreakdown().value());
        }
        return approveUrl;
    }

    /**
     * 核销PayPal订单
     */
    @Override
    public void captureOrder(MasterOrder masterOrder) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(masterOrder.getOutTradeNo());
        request.requestBody(buildRequestBody(masterOrder));
        //3. Call PayPal to capture an order
        HttpResponse<Order> response = getClient().execute(request);
        //4. Save the capture ID to your database. Implement logic to save capture to your database for future reference.
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Status: " + response.result().status());
        System.out.println("Order ID: " + response.result().id());
        System.out.println("Links: ");
        for (LinkDescription link : response.result().links()) {
            System.out.println("\t" + link.rel() + ": " + link.href());
        }
        System.out.println("Capture ids:");
        for (PurchaseUnit purchaseUnit : response.result().purchaseUnits()) {
            for (Capture capture : purchaseUnit.payments().captures()) {
                System.out.println("\t" + capture.id());
            }
        }
        System.out.println("Buyer: ");
        Payer buyer = response.result().payer();
        System.out.println("\tEmail Address: " + buyer.email());
        System.out.println("\tName: " + buyer.name().fullName());
    }

    /**
     * 订单退款
     * @param storeOrder 订单
     * @return Boolean
     */
    @Override
    public Boolean refund(StoreOrder storeOrder) {
        MasterOrder masterOrder = masterOrderService.getByOrderNo(storeOrder.getMasterOrderNo());
        OrdersGetRequest ordersGetRequest = new OrdersGetRequest(masterOrder.getOutTradeNo());
        HttpResponse<com.paypal.orders.Order> ordersGetResponse = null;
        PayPalHttpClient payPalHttpClient = getClient();
        try {
            ordersGetResponse = payPalHttpClient.execute(ordersGetRequest);
        } catch (Exception e) {
            try {
                LOGGER.error("第1次调用paypal订单查询失败");
                ordersGetResponse = payPalHttpClient.execute(ordersGetRequest);
            } catch (Exception e2) {
                try {
                    LOGGER.error("第2次调用paypal订单查询失败");
                    ordersGetResponse = payPalHttpClient.execute(ordersGetRequest);
                } catch (Exception e3) {
                    LOGGER.error("第3次调用paypal订单查询失败，失败原因：{}", e3.getMessage());
                }
            }
        }
        String captureId = ordersGetResponse.result().purchaseUnits().get(0).payments().captures().get(0).id();
        CapturesRefundRequest request = new CapturesRefundRequest(captureId);
        request.prefer("return=representation");
        request.requestBody(buildRefundRequestBody(storeOrder));
        HttpResponse<com.paypal.payments.Refund> response = null;
        try {
            response = payPalHttpClient.execute(request);
        } catch (IOException e) {
            try {
                LOGGER.error("第1次调用paypal退款申请失败");
                response = payPalHttpClient.execute(request);
            } catch (Exception e1) {
                try {
                    LOGGER.error("第2次调用paypal退款申请失败");
                    response = payPalHttpClient.execute(request);
                } catch (Exception e2) {
                    LOGGER.error("第3次调用paypal退款申请失败，失败原因 {}", e2.getMessage());
                }
            }
        }
        LOGGER.info("Status Code = {}, Status = {}, RefundID = {}", response.statusCode(), response.result().status(), response.result().id());
        for (com.paypal.payments.LinkDescription link : response.result().links()) {
            LOGGER.info("Links-{}: {}    \tCall Type: {}", link.rel(), link.href(), link.method());
        }
        String json = null;
        try {
            json = JSONObject.toJSONString(response.result());
//            json = new JSONObject(new Json().serialize(response.result())).toString(4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LOGGER.info("refundOrder response body: {}", json);
        if("COMPLETED".equals(response.result().status())) {
        /*  CANCELLED. The refund was cancelled.
            PENDING. The refund is pending. For more information, see status_details.reason.
            COMPLETED. The funds for this transaction were debited to the customer's account. */
            //进行数据库操作，修改状态为已退款（配合回调和退款查询确定退款成功）
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private RefundRequest buildRefundRequestBody(StoreOrder storeOrder) {
        RefundRequest refundRequest = new RefundRequest();
        com.paypal.payments.Money money = new com.paypal.payments.Money();

        money.currencyCode("USD");
        money.value(storeOrder.getPayPrice().toString());
        refundRequest.amount(money);
        refundRequest.invoiceId(storeOrder.getMasterOrderNo());
        refundRequest.noteToPayer("7天无理由退款");
        return refundRequest;
    }

    private OrderRequest buildRequestBody(MasterOrder masterOrder) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent(PayPalCheckoutConstant.CAPTURE);

        String paypalReturnUri;
        if (masterOrder.getPayChannel().equals("pc")) {
            paypalReturnUri = crmebConfig.getPaypalPcReturnUri();
        } else {
            paypalReturnUri = crmebConfig.getPaypalReturnUri();
        }

        ApplicationContext context = new ApplicationContext()
                // 商家名称，展示在支付界面
                .brandName(crmebConfig.getPaypalBrandName())
                .landingPage(PayPalCheckoutConstant.LANDINGPAGE)
                .cancelUrl(paypalReturnUri + "?status=0&local_order_no="+ masterOrder.getOrderNo()) // 取消返回地址
                .returnUrl(paypalReturnUri + "?status=1") // 支付成功返回地址
                .userAction(PayPalCheckoutConstant.USERACTION)
                .shippingPreference(PayPalCheckoutConstant.SHIPPINGPREFERENCE);
        orderRequest.applicationContext(context);

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<PurchaseUnitRequest>();
        PurchaseUnitRequest unitRequest = new PurchaseUnitRequest()
                .customId(masterOrder.getOrderNo())
                // 发票标识
                .invoiceId(masterOrder.getOrderNo())
                .amountWithBreakdown(
                        new AmountWithBreakdown()
                                .currencyCode("USD")
                                .value(masterOrder.getPayPrice().toString())
                );
        purchaseUnitRequests.add(unitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequests);
        return orderRequest;
    }
}
