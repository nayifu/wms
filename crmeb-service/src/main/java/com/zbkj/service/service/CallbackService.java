package com.zbkj.service.service;

/**
 * 订单支付回调 service
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
public interface CallbackService {

    Integer stripe(String request);

    /**
     * 微信支付回调
     *
     * @param request   微信回调请求体
     * @param timestamp 微信回调时间戳
     * @param nonce     微信回调随机串
     * @param signature 微信回调签名
     * @param serial    微信回调
     */
    Boolean wechat(String request, String timestamp, String nonce, String signature, String serial);

    /**
     * 微信退款支付回调
     *
     * @param request   微信回调请求体
     * @param timestamp 微信回调时间戳
     * @param nonce     微信回调随机串
     * @param signature 微信回调签名
     * @param serial    微信回调
     */
    Boolean weChatRefund(String request, String timestamp, String nonce, String signature, String serial);
}
