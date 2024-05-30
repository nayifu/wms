package com.zbkj.service.service;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.sms.SmsRecord;
import com.zbkj.common.request.PageParamRequest;

import java.math.BigDecimal;

/**
 * SmsService 接口
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
public interface SmsService {

    /**
     * 发送支付成功短信
     * @param countryCode 国标区号
     * @param phone 手机号
     * @param payPrice 支付金额
     * @param orderNo 订单编号
     */
    Boolean sendPaySuccess(String countryCode, String phone, BigDecimal payPrice, String orderNo);

    /**
     * 发送订单发货提醒短信
     * @param countryCode 国标区号
     * @param phone 手机号
     * @param orderNo 订单编号
     */
    Boolean sendOrderDeliverNotice(String countryCode, String phone, String orderNo);

    /**
     * 发送验证码
     * @param phone 手机号
     * @param countryCode 国标区号
     */
    Boolean sendVerificationCode(String phone, String countryCode);

    /**
     * 短信发送记录
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<SmsRecord> record(PageParamRequest pageParamRequest);

}
