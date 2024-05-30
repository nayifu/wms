package com.zbkj.front.service;

import com.zbkj.common.vo.MyRecord;

/**
 * 验证码Service
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
public interface CaptchaService {

    /**
     * 手机号用户修改密码验证码
     */
    Boolean phoneUpdatePassword();

    /**
     * 邮箱修改密码验证码
     */
    Boolean emailUpdatePassword();

    /**
     * 商户入驻邮箱验证码
     * @param email 邮箱
     * @return Boolean
     */
    Boolean emailMerchantSettled(String email);
}
