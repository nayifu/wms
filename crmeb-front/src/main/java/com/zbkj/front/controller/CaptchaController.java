package com.zbkj.front.controller;

import com.zbkj.common.response.CommonResult;
import com.zbkj.front.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码管理
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
@Slf4j
@RestController
@RequestMapping("api/front/captcha")
@Api(tags = "验证码管理")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    /**
     * 手机号修改密码验证码
     */
    @ApiOperation(value="手机号修改密码验证码")
    @RequestMapping(value = "/phone/password", method = RequestMethod.POST)
    public CommonResult<Boolean> phoneUpdatePassword() {
        return CommonResult.success(captchaService.phoneUpdatePassword());
    }


    /**
     * 邮箱修改密码验证码
     */
    @ApiOperation(value="邮箱修改密码验证码")
    @RequestMapping(value = "/email/password", method = RequestMethod.POST)
    public CommonResult<Boolean> emailUpdatePassword() {
        return CommonResult.success(captchaService.emailUpdatePassword());
    }

    /**
     * 商户入驻发送邮箱验证码
     */
    @ApiOperation(value="商户入驻发送邮箱验证码")
    @RequestMapping(value = "/email/merchant/settled", method = RequestMethod.POST)
    public CommonResult<Boolean> emailMerchantSettled(@RequestBody String email) {
        return CommonResult.success(captchaService.emailMerchantSettled(email));
    }

}
