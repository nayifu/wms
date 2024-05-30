package com.zbkj.front.service;

import com.zbkj.common.request.*;
import com.zbkj.common.response.LoginMethodResponse;
import com.zbkj.common.response.LoginResponse;
import com.zbkj.common.vo.MyRecord;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 移动端登录服务类
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
public interface LoginService {

    /**
     * 推出登录
     * @param request HttpServletRequest
     */
    void loginOut(HttpServletRequest request);

    /**
     * Google登录
     * @param idTokenStr Google前端获取的idToken
     */
    LoginResponse googleLogin(String idTokenStr);

    /**
     * 邮箱登录
     * @param loginRequest 登录对象
     * @return LoginResponse
     */
    LoginResponse emailLogin(EmailLoginRequest loginRequest);

    /**
     * 邮箱注册登录
     * @param registerRequest 邮箱注册对象
     * @return LoginResponse
     */
    LoginResponse emailRegister(EmailRegisterRequest registerRequest);

    /**
     * 发送邮箱注册验证码
     * @param email 邮箱
     * @return Boolean
     */
    Boolean sendEmailCode(String email);

    /**
     * Facebook登录
     */
    LoginResponse facebookLogin(FacebookLoginRequest loginRequest);

    /**
     * twitter登录获取requestToken
     * @return requestToken
     */
    RequestToken getTwitterRequestToken(String end);

    /**
     * twitter登录
     * @param loginRequest 登录请求信息
     * @return LoginResponse
     */
    LoginResponse twitterLogin(TwitterLoginRequest loginRequest);

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @param countryCode 国标区号
     * @return Boolean
     */
    Boolean sendLoginCode(String phone, String countryCode);

    /**
     * 手机号注册登录
     * @param loginRequest 注册信息
     * @return LoginResponse
     */
    LoginResponse phoneRegister(LoginMobileRequest loginRequest);

    /**
     * 手机号验证码登录
     * @param loginRequest 登录信息
     * @return LoginResponse
     */
    LoginResponse phoneCaptchaLogin(LoginMobileRequest loginRequest);

    /**
     * 手机号密码登录
     * @param loginRequest 登录信息
     * @return LoginResponse
     */
    LoginResponse phonePasswordLogin(LoginMobileRequest loginRequest);

    /**
     * 发送邮箱忘记密码验证码
     * @param email 邮箱
     * @return Boolean
     */
    Boolean emailForgetPassword(String email);

    /**
     * 邮箱重置密码
     * @param request 重置密码信息
     * @return Boolean
     */
    Boolean emailResetPassword(EmailResetPasswordRequest request);

    /**
     * 游客注册
     */
    LoginResponse visitorRegister(VisitorRegisterRequest registerRequest);

    /**
     * 获取登录方式信息
     */
    LoginMethodResponse getLoginMethod();

    /**
     * 获取PC登录页图片
     */
    Map<String, Object> getPcLoginPic();

    /**
     * 校验token是否有效
     * @return true 有效， false 无效
     */
    Boolean tokenIsExist();
}
