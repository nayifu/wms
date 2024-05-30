package com.zbkj.front.controller;

import com.zbkj.common.request.OrderPayRequest;
import com.zbkj.common.request.PayPalResultRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.OrderPayResultResponse;
import com.zbkj.common.response.PayMethodResponse;
import com.zbkj.common.response.PayPalResultResponse;
import com.zbkj.service.service.OrderPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 微信缓存表 前端控制器
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
@Slf4j
@RestController
@RequestMapping("api/front/pay")
@Api(tags = "支付管理")
public class PayController {

    @Autowired
    private OrderPayService orderPayService;

    /**
     * 订单支付
     */
    @ApiOperation(value = "订单支付")
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public CommonResult<OrderPayResultResponse> payment(@RequestBody @Validated OrderPayRequest orderPayRequest) {
        return CommonResult.success(orderPayService.payment(orderPayRequest));
    }

    /**
     * PayPal订单授权成功
     */
    @ApiOperation(value = "PayPal订单授权成功")
    @RequestMapping(value = "/paypal/success", method = RequestMethod.POST)
    public CommonResult<PayPalResultResponse> paypalSuccess(@RequestBody @Validated PayPalResultRequest request) {
        return CommonResult.success(orderPayService.paypalSuccess(request));
    }

    /**
     * PayPal订单取消
     */
    @ApiOperation(value = "PayPal订单取消")
    @RequestMapping(value = "/paypal/cancel", method = RequestMethod.POST)
    public CommonResult<Boolean> paypalCancel(@RequestBody @Validated PayPalResultRequest request) {
        return CommonResult.success(orderPayService.paypalCancel(request));
    }

    @ApiOperation(value = "获取可用支付方式")
    @RequestMapping(value = "/method", method = RequestMethod.GET)
    public CommonResult<PayMethodResponse> getPayMethod() {
        return CommonResult.success(orderPayService.getPayMethod());
    }

    @ApiOperation(value = "查询订单微信支付结果")
    @RequestMapping(value = "/query/wechat/pay/result/{orderNo}", method = RequestMethod.GET)
    public CommonResult<Boolean> queryPayResult(@PathVariable(value = "orderNo") String orderNo) {
        return CommonResult.success(orderPayService.queryWechatPayResult(orderNo));
    }
}
