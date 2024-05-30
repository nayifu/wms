package com.zbkj.service.service;

import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.request.OrderPayRequest;
import com.zbkj.common.request.PayPalResultRequest;
import com.zbkj.common.response.OrderPayResultResponse;
import com.zbkj.common.response.PayMethodResponse;
import com.zbkj.common.response.PayPalResultResponse;
import com.zbkj.common.vo.MyRecord;

/**
 * 订单支付
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
public interface OrderPayService{

    /**
     * 支付成功处理
     * @param storeOrder 订单
     */
    Boolean paySuccess(MasterOrder masterOrder);

    /**
     * 订单支付
     * @param orderPayRequest 支付参数
     * @return OrderPayResultResponse
     */
    OrderPayResultResponse payment(OrderPayRequest orderPayRequest);

    /**
     * PayPal订单授权成功
     * @param request PayPal支付返回请求对象
     * @return PayPalResultResponse
     */
    PayPalResultResponse paypalSuccess(PayPalResultRequest request);

    /**
     * PayPal订单取消
     * @param request PayPal支付返回请求对象
     * @return Boolean
     */
    Boolean paypalCancel(PayPalResultRequest request);

    /**
     * 获取支付方式
     */
    PayMethodResponse getPayMethod();

    /**
     * 查询订单微信支付结果
     * @param orderNo 订单编号
     */
    Boolean queryWechatPayResult(String orderNo);
}
