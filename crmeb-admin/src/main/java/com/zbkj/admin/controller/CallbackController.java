package com.zbkj.admin.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Response;
import com.zbkj.service.service.CallbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 支付回调
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
@RequestMapping("api/admin/payment/callback")
@Api(tags = "支付回调")
public class CallbackController {

    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    private CallbackService callbackService;

    @ApiOperation(value = "stripe支付回调")
    @RequestMapping(value = "/stripe/webhooks", method = RequestMethod.POST)
    public String stripeWebhooks(@RequestBody String request, HttpServletResponse response) {
        System.out.println("Stripe支付回调 request ===> " + request);
        Integer stripeStatus = callbackService.stripe(request);
        logger.info("stripe webhooks response status : " + stripeStatus);
        response.setStatus(stripeStatus);
        return "";
    }

    /**
     * 微信支付回调
     */
    @ApiOperation(value = "微信支付回调")
    @RequestMapping(value = "/wechat/pay", method = RequestMethod.POST)
    public String weChat(@RequestBody String request,
                         @RequestHeader(value = "Wechatpay-Timestamp", required = false) String timestamp,
                         @RequestHeader(value = "Wechatpay-Nonce", required = false) String nonce,
                         @RequestHeader(value = "Wechatpay-Signature", required = false) String signature,
                         @RequestHeader(value = "Wechatpay-Serial", required = false) String serial,
                         HttpServletResponse response) {
        System.out.println("微信支付回调  request ===> " + request);
        System.out.println("微信支付回调  timestamp ===> " + timestamp);
        System.out.println("微信支付回调  nonce ===> " + nonce);
        System.out.println("微信支付回调  signature ===> " + signature);
        System.out.println("微信支付回调  serial ===> " + serial);
        Boolean wechated = callbackService.wechat(request, timestamp, nonce, signature, serial);
        if (wechated) {
            return WxPayNotifyV3Response.success("成功");
        }
        response.setStatus(400);
        return WxPayNotifyV3Response.fail("失败");
    }

    @ApiOperation(value = "微信退款回调")
    @RequestMapping(value = "/wechat/refund", method = RequestMethod.POST)
    public String weChatRefund(@RequestBody String request,
                               @RequestHeader(value = "Wechatpay-Timestamp", required = false) String timestamp,
                               @RequestHeader(value = "Wechatpay-Nonce", required = false) String nonce,
                               @RequestHeader(value = "Wechatpay-Signature", required = false) String signature,
                               @RequestHeader(value = "Wechatpay-Serial", required = false) String serial,
                               HttpServletResponse response) {
        System.out.println("微信支付回调  request ===> " + request);
        System.out.println("微信支付回调  timestamp ===> " + timestamp);
        System.out.println("微信支付回调  nonce ===> " + nonce);
        System.out.println("微信支付回调  signature ===> " + signature);
        System.out.println("微信支付回调  serial ===> " + serial);
        Boolean wechated = callbackService.weChatRefund(request, timestamp, nonce, signature, serial);
        if (wechated) {
            return WxPayNotifyV3Response.success("成功");
        }
        response.setStatus(400);
        return WxPayNotifyV3Response.fail("失败");
    }
}



