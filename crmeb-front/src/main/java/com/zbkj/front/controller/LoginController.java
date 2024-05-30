package com.zbkj.front.controller;


import com.zbkj.common.request.*;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.LoginMethodResponse;
import com.zbkj.common.response.LoginResponse;
import com.zbkj.front.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户登陆 前端控制器
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
@RequestMapping("api/front/login")
@Api(tags = "用户 -- 登录注册")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 手机号注册登录
     */
    @ApiOperation(value = "手机号注册登录")
    @RequestMapping(value = "/register/mobile", method = RequestMethod.POST)
    public CommonResult<LoginResponse> phoneRegister(@RequestBody @Validated LoginMobileRequest loginRequest) {
        return CommonResult.success(loginService.phoneRegister(loginRequest));
    }

    /**
     * 手机号验证码登录
     */
    @ApiOperation(value = "手机号验证码登录")
    @RequestMapping(value = "/mobile/captcha", method = RequestMethod.POST)
    public CommonResult<LoginResponse> phoneCaptchaLogin(@RequestBody @Validated LoginMobileRequest loginRequest) {
        return CommonResult.success(loginService.phoneCaptchaLogin(loginRequest));
    }

    /**
     * 手机号密码登录
     */
    @ApiOperation(value = "手机号密码登录")
    @RequestMapping(value = "/mobile/password", method = RequestMethod.POST)
    public CommonResult<LoginResponse> phonePasswordLogin(@RequestBody @Validated LoginMobileRequest loginRequest) {
        return CommonResult.success(loginService.phonePasswordLogin(loginRequest));
    }

    /**
     * 退出登录
     */
    @ApiOperation(value = "退出")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public CommonResult<String> loginOut(HttpServletRequest request) {
        loginService.loginOut(request);
        return CommonResult.success();
    }

    /**
     * 发送短信登录验证码
     */
    @ApiOperation(value = "发送短信登录验证码")
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone", value="手机号码", required = true),
            @ApiImplicitParam(name="countryCode", value="国标区号", required = true)
    })
    public CommonResult<Object> sendCode(@RequestParam String phone, @RequestParam String countryCode) {
        if (loginService.sendLoginCode(phone, countryCode)) {
            return CommonResult.success("Sent successfully");
        } else {
            return CommonResult.failed("Failed to send");
        }
    }

    /**
     * Google登录
     * @param idToken Google前端获取的idToken
     */
    @ApiOperation(value = "Google登录")
    @RequestMapping(value = "/google", method = RequestMethod.POST)
    public CommonResult<LoginResponse> googleLogin(@RequestBody String idToken) {
        return CommonResult.success(loginService.googleLogin(idToken));
    }

    /**
     * Facebook登录
     */
    @ApiOperation(value = "Facebook登录")
    @RequestMapping(value = "/facebook", method = RequestMethod.POST)
    public CommonResult<LoginResponse> facebookLogin(@RequestBody FacebookLoginRequest loginRequest) {
        return CommonResult.success(loginService.facebookLogin(loginRequest));
    }

    /**
     * 邮箱登录
     */
    @ApiOperation(value = "邮箱登录")
    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public CommonResult<LoginResponse> emailLogin(@RequestBody @Validated EmailLoginRequest loginRequest) {
        return CommonResult.success(loginService.emailLogin(loginRequest));
    }

    /**
     * 邮箱注册登录
     */
    @ApiOperation(value = "邮箱注册登录")
    @RequestMapping(value = "/register/email", method = RequestMethod.POST)
    public CommonResult<LoginResponse> emailRegister(@RequestBody @Validated EmailRegisterRequest registerRequest) {
        return CommonResult.success(loginService.emailRegister(registerRequest));
    }

    /**
     * 发送邮箱注册验证码
     * @param email 邮箱
     * @return 发送是否成功
     */
    @ApiOperation(value = "发送邮箱注册验证码")
    @RequestMapping(value = "/email/captcha", method = RequestMethod.POST)
    public CommonResult<Object> sendEmailCode(@RequestBody String email) {
        if (loginService.sendEmailCode(email)) {
            return CommonResult.success("Sent successfully");
        } else {
            return CommonResult.failed("Failed to send");
        }
    }

    /**
     * 发送邮箱忘记密码验证码
     * @param email 邮箱
     * @return 发送是否成功
     */
    @ApiOperation(value = "发送邮箱忘记密码验证码")
    @RequestMapping(value = "/email/forget/password", method = RequestMethod.POST)
    public CommonResult<Object> emailForgetPassword(@RequestBody String email) {
        if (loginService.emailForgetPassword(email)) {
            return CommonResult.success("Sent successfully");
        } else {
            return CommonResult.failed("Failed to send");
        }
    }

    /**
     * 邮箱重置密码
     */
    @ApiOperation(value = "邮箱重置密码")
    @RequestMapping(value = "/email/reset/password", method = RequestMethod.POST)
    public CommonResult<Object> emailResetPassword(@RequestBody @Validated EmailResetPasswordRequest request) {
        if (loginService.emailResetPassword(request)) {
            return CommonResult.success("reset successfully");
        } else {
            return CommonResult.failed("reset failed");
        }
    }

    /**
     * twitter登录获取requestToken
     */
    @ApiOperation(value = "twitter登录获取requestToken")
    @RequestMapping(value = "/twitter/request/token", method = RequestMethod.GET)
    public CommonResult<RequestToken> getTwitterRequestToken(@RequestParam(value = "end") @Valid String end) {
        return CommonResult.success(loginService.getTwitterRequestToken(end));
    }

    /**
     * twitter登录获取requestToken
     */
    @ApiOperation(value = "twitter登录")
    @RequestMapping(value = "/twitter", method = RequestMethod.POST)
    public CommonResult<LoginResponse> twitterLogin(@RequestBody @Validated TwitterLoginRequest loginRequest) {
        return CommonResult.success(loginService.twitterLogin(loginRequest));
    }

    /**
     * 游客注册
     */
    @ApiOperation(value = "游客注册")
    @RequestMapping(value = "/register/visitor", method = RequestMethod.POST)
    public CommonResult<LoginResponse> visitorRegister(@RequestBody @Validated VisitorRegisterRequest registerRequest) {
        return CommonResult.success(loginService.visitorRegister(registerRequest));
    }

    /**
     * 获取登录方式信息
     */
    @ApiOperation(value = "获取登录方式信息")
    @RequestMapping(value = "/method/info", method = RequestMethod.GET)
    public CommonResult<LoginMethodResponse> getLoginMethod() {
        return CommonResult.success(loginService.getLoginMethod());
    }

    @ApiOperation(value = "校验token是否有效")
    @RequestMapping(value = "/token/is/exist", method = RequestMethod.POST)
    public CommonResult<Boolean> tokenIsExist() {
        return CommonResult.success(loginService.tokenIsExist());
    }
}



