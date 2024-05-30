package com.zbkj.service.service;

import java.math.BigDecimal;

/**
 * EmailService 接口
 *  +----------------------------------------------------------------------
 *  | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 *  +----------------------------------------------------------------------
 *  | Copyright (c) 2016~2023 https://www.crmeb.com All rights reserved.
 *  +----------------------------------------------------------------------
 *  | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 *  +----------------------------------------------------------------------
 *  | Author: CRMEB Team <admin@crmeb.com>
 *  +----------------------------------------------------------------------
 */
public interface EmailService {

    /**
     * 发送邮箱验证码
     */
    Boolean sendEmailCode(String email);

    /**
     * 校验邮箱验证码
     * @param email 邮箱
     * @param code 验证码
     */
    void checkEmailValidateCode(String email, String code);

    /**
     * 忘记密码验证码
     * @param email 邮箱
     * @return Boolean
     */
    Boolean emailForgetPassword(String email);

    /**
     * 发送支付成功邮件
     * @param userType 用户类型
     * @param email 邮箱
     * @param payPrice 支付金额
     * @param orderNo 订单号
     * @param identity 用户标识
     * @return Boolean
     */
    Boolean sendPaySuccess(String userType, String email, BigDecimal payPrice, String orderNo, String identity);

    /**
     * 发送发货邮件
     * @param email 邮箱
     * @param orderNo 订单号
     */
    Boolean sendOrderDeliver(String email, String orderNo);

    /**
     * 发送商户申请成功邮件
     * @param email 邮箱
     * @param password 密码
     */
    Boolean sendMerchantAuditSuccess(String email, String password);
}
