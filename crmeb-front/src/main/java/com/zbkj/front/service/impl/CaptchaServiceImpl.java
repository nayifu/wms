package com.zbkj.front.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.UserConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.user.User;
import com.zbkj.common.utils.ValidateFormUtil;
import com.zbkj.front.service.CaptchaService;
import com.zbkj.service.service.EmailService;
import com.zbkj.service.service.SmsService;
import com.zbkj.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 验证码Service Impl
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
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    /**
     * 手机号用户修改密码验证码
     */
    @Override
    public Boolean phoneUpdatePassword() {
        User user = userService.getInfoException();
        if (!user.getUserType().equals(UserConstants.USER_LOGIN_TYPE_PHONE)) {
            throw new CrmebException("Only mobile phone users can change the password through the mobile phone number");
        }
        return smsService.sendVerificationCode(user.getPhone(), user.getCountryCode());
    }

    /**
     * 邮箱修改密码验证码
     */
    @Override
    public Boolean emailUpdatePassword() {
        User user = userService.getInfoException();
        if (!user.getUserType().equals(UserConstants.USER_LOGIN_TYPE_EMAIL)) {
            throw new CrmebException("Only email users can change their passwords through email");
        }
        return emailService.sendEmailCode(user.getEmail());
    }

    /**
     * 商户入驻邮箱验证码
     * @param email 邮箱
     * @return Boolean
     */
    @Override
    public Boolean emailMerchantSettled(String email) {
        if (StrUtil.isEmpty(email)) {
            throw new CrmebException("E-mail can not be empty");
        }
        ValidateFormUtil.isEmailException(email);
        return emailService.sendEmailCode(email);
    }
}
