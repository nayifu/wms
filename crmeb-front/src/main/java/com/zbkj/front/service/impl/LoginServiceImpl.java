package com.zbkj.front.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.SmsConstants;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.constants.UserConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.user.User;
import com.zbkj.common.request.*;
import com.zbkj.common.response.LoginMethodResponse;
import com.zbkj.common.response.LoginResponse;
import com.zbkj.common.token.FrontTokenComponent;
import com.zbkj.common.utils.*;
import com.zbkj.common.vo.MyRecord;
import com.zbkj.front.service.LoginService;
import com.zbkj.front.util.TwitterUtil;
import com.zbkj.service.service.EmailService;
import com.zbkj.service.service.SmsService;
import com.zbkj.service.service.SystemConfigService;
import com.zbkj.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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
@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private FrontTokenComponent tokenComponent;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TwitterUtil twitterUtil;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 检测手机验证码
     *
     * @param phone 手机号
     * @param code  验证码
     */
    private void checkValidateCode(String phone, String code) {
        Object validateCode = redisUtil.get(SmsConstants.SMS_VALIDATE_PHONE + phone);
        if (ObjectUtil.isNull(validateCode)) {
            throw new CrmebException("Verification code has expired");
        }
        if (!validateCode.toString().equals(code)) {
            throw new CrmebException("Verification code error");
        }
        //删除验证码
        redisUtil.delete(SmsConstants.SMS_VALIDATE_PHONE + phone);
    }

    /**
     * 退出登录
     *
     * @param request HttpServletRequest
     */
    @Override
    public void loginOut(HttpServletRequest request) {
        tokenComponent.logout(request);
    }

    /**
     * Google登录
     *
     * @param idTokenStr Google前端获取的idToken
     */
    @Override
    public LoginResponse googleLogin(String idTokenStr) {
        if (StrUtil.isEmpty(idTokenStr)) {
            throw new CrmebException("Google idToken can not be empty ");
        }

        String googleOpen = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_GOOGLE_OPEN);
        if (googleOpen.equals(Constants.COMMON_SWITCH_CLOSE_TYPE_ONE)) {
            throw new CrmebException("Please turn on the Google switch first");
        }

        System.out.println("idTokenStr = " + idTokenStr);
        MyRecord myRecord = googleVerify1(idTokenStr);
        String identity = myRecord.getStr("sub");
        String email = myRecord.getStr("email");
        User user = userService.getByEmailAndIdentityAndType(email, identity, UserConstants.USER_LOGIN_TYPE_GOOGLE);
        if (ObjectUtil.isNull(user)) {
            // 用户不存在走注册流程
            user = userService.commonRegister(identity, email, myRecord.getStr("name"),
                    myRecord.getStr("picture"), myRecord.getStr("locale"), UserConstants.USER_LOGIN_TYPE_GOOGLE);
        } else {
            // 用户存在走登录流程
            if (!user.getStatus()) {
                throw new CrmebException("The current account is disabled, please contact the administrator！");
            }
            // 记录最后一次登录时间
            user.setLastLoginTime(DateUtil.nowDateTime());
            if (userService.updateById(user)) {
                logger.error("When the user logs in, there is an error recording the last login time,uid = " + user.getUid());
            }
        }
        //生成token
        LoginResponse loginResponse = new LoginResponse();
        String token = tokenComponent.createToken(user);
        loginResponse.setToken(token);
        loginResponse.setIdentity(user.getIdentity());
        loginResponse.setNikeName(user.getNickname());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setUid(user.getUid());
        loginResponse.setType(user.getUserType());
        return loginResponse;
    }

    private MyRecord googleVerify1(String idTokenStr) {
        String url = "https://oauth2.googleapis.com/tokeninfo";
        JSONObject jsonObject = null;
        try {
            jsonObject = restTemplateUtil.getData(url + "?id_token=" + idTokenStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CrmebException("Failed to get Google info，idToken = " + idTokenStr);
        }
        MyRecord myRecord = new MyRecord();
        myRecord.set("sub", jsonObject.getString("sub"));
        myRecord.set("email", jsonObject.getString("email"));
        myRecord.set("email_verified", jsonObject.getString("email_verified"));
        myRecord.set("name", jsonObject.getString("name"));
        myRecord.set("picture", jsonObject.getString("picture"));
        myRecord.set("locale", jsonObject.getString("locale"));
        return myRecord;
    }

    /**
     * 邮箱登录
     *
     * @param loginRequest 登录对象
     * @return LoginResponse
     */
    @Override
    public LoginResponse emailLogin(EmailLoginRequest loginRequest) {
        User user = userService.getByEmailAndIdentityAndType(loginRequest.getEmail(), "", UserConstants.USER_LOGIN_TYPE_EMAIL);
        if (ObjectUtil.isNull(user)) {
            throw new CrmebException("User does not exist, please register first!");
        }
        String password = CrmebUtil.encryptPassword(loginRequest.getPassword(), user.getIdentity());
        if (!password.equals(user.getPwd())) {
            throw new CrmebException("Mail or password is incorrect");
        }
        LoginResponse loginResponse = new LoginResponse();
        String token = tokenComponent.createToken(user);
        loginResponse.setToken(token);
        loginResponse.setIdentity(user.getIdentity());
        loginResponse.setNikeName(user.getNickname());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setUid(user.getUid());
        loginResponse.setType(user.getUserType());
        // 记录最后一次登录时间
        user.setLastLoginTime(DateUtil.nowDateTime());
        userService.updateById(user);
        return loginResponse;
    }

    /**
     * 邮箱注册登录
     *
     * @param registerRequest 邮箱注册对象
     * @return LoginResponse
     */
    @Override
    public LoginResponse emailRegister(EmailRegisterRequest registerRequest) {
        //检测验证码
        emailService.checkEmailValidateCode(registerRequest.getEmail(), registerRequest.getCaptcha());
        User user = userService.getByEmailAndIdentityAndType(registerRequest.getEmail(), "", UserConstants.USER_LOGIN_TYPE_EMAIL);
        if (ObjectUtil.isNotNull(user)) {
            throw new CrmebException("Email already exists!");
        }
        user = userService.emailRegister(registerRequest.getEmail(), registerRequest.getPassword(), UserConstants.USER_LOGIN_TYPE_EMAIL);
        LoginResponse loginResponse = new LoginResponse();
        String token = tokenComponent.createToken(user);
        loginResponse.setToken(token);
        loginResponse.setIdentity(user.getIdentity());
        loginResponse.setNikeName(user.getNickname());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setUid(user.getUid());
        loginResponse.setType(user.getUserType());
        return loginResponse;
    }

    /**
     * 发送邮箱注册验证码
     *
     * @param email 邮箱
     * @return Boolean
     */
    @Override
    public Boolean sendEmailCode(String email) {
        User user = userService.getByEmailAndIdentityAndType(email, "", UserConstants.USER_LOGIN_TYPE_EMAIL);
        if (ObjectUtil.isNotNull(user)) {
            throw new CrmebException("Email already exists!");
        }
        return emailService.sendEmailCode(email);
    }

    /**
     * Facebook登录
     */
    @Override
    public LoginResponse facebookLogin(FacebookLoginRequest loginRequest) {
        String googleOpen = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_FACEBOOK_OPEN);
        if (googleOpen.equals(Constants.COMMON_SWITCH_CLOSE_TYPE_ONE)) {
            throw new CrmebException("Please turn on the Facebook switch first");
        }

        String email = Optional.ofNullable(loginRequest.getEmail()).orElse("");
        // 判断用户是否存在
        User user = userService.getByEmailAndIdentityAndType(email, loginRequest.getId(), UserConstants.USER_LOGIN_TYPE_FACEBOOK);
        if (ObjectUtil.isNull(user)) {
            // 用户不存在走注册流程
            user = userService.commonRegister(loginRequest.getId(), loginRequest.getEmail(),
                    loginRequest.getName(), loginRequest.getPicture(), "", UserConstants.USER_LOGIN_TYPE_FACEBOOK);
        } else {
            // 用户存在走登录流程
            if (!user.getStatus()) {
                throw new CrmebException("The current account is disabled, please contact the administrator！");
            }
            // 记录最后一次登录时间
            user.setLastLoginTime(DateUtil.nowDateTime());
            if (userService.updateById(user)) {
                logger.error("When the user logs in, there is an error recording the last login time,uid = " + user.getUid());
            }
        }
        //生成token
        LoginResponse loginResponse = new LoginResponse();
        String token = tokenComponent.createToken(user);
        loginResponse.setToken(token);
        loginResponse.setIdentity(user.getIdentity());
        loginResponse.setNikeName(user.getNickname());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setUid(user.getUid());
        loginResponse.setType(user.getUserType());
        return loginResponse;
    }

    /**
     * twitter登录获取requestToken
     *
     * @return requestToken
     */
    @Override
    public RequestToken getTwitterRequestToken(String end) {
        return twitterUtil.getRequestToken(end);
    }

    /**
     * twitter登录
     *
     * @param loginRequest 登录请求信息
     * @return LoginResponse
     */
    @Override
    public LoginResponse twitterLogin(TwitterLoginRequest loginRequest) {
        RequestToken requestToken = new RequestToken(loginRequest.getOauthToken(), loginRequest.getOauthTokenSecret());
        AccessToken accessToken = twitterUtil.getOAuth1AccessToken(requestToken, loginRequest.getOauthVerifier());
        long tokenUserId = accessToken.getUserId();
        String screenName = accessToken.getScreenName();
        // 判断用户是否存在
        User user = userService.getByEmailAndIdentityAndType("", String.valueOf(tokenUserId), UserConstants.USER_LOGIN_TYPE_TWITTER);
        if (ObjectUtil.isNull(user)) {
            // 用户不存在走注册流程
            user = userService.commonRegister(String.valueOf(tokenUserId), "",
                    screenName, "", "", UserConstants.USER_LOGIN_TYPE_TWITTER);
        } else {
            // 用户存在走登录流程
            if (!user.getStatus()) {
                throw new CrmebException("The current account is disabled, please contact the administrator！");
            }
            // 记录最后一次登录时间
            user.setLastLoginTime(DateUtil.nowDateTime());
            if (userService.updateById(user)) {
                logger.error("When the user logs in, there is an error recording the last login time,uid = " + user.getUid());
            }
        }
        //生成token
        LoginResponse loginResponse = new LoginResponse();
        String token = tokenComponent.createToken(user);
        loginResponse.setToken(token);
        loginResponse.setIdentity(user.getIdentity());
        loginResponse.setNikeName(user.getNickname());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setUid(user.getUid());
        loginResponse.setType(user.getUserType());
        return loginResponse;
    }

    /**
     * 发送短信验证码
     *
     * @param phone       手机号
     * @param countryCode 国标区号
     * @return Boolean
     */
    @Override
    public Boolean sendLoginCode(String phone, String countryCode) {
        return smsService.sendVerificationCode(phone, countryCode);
    }

    /**
     * 手机号注册登录
     *
     * @param loginRequest 注册信息
     * @return LoginResponse
     */
    @Override
    public LoginResponse phoneRegister(LoginMobileRequest loginRequest) {
        if (StrUtil.isBlank(loginRequest.getCaptcha())) {
            throw new CrmebException("Mobile number verification code cannot be empty");
        }
        if (StrUtil.isBlank(loginRequest.getPassword())) {
            throw new CrmebException("password cannot be empty");
        }
//        if (!loginRequest.getPassword().matches(RegularConstants.PASSWORD)) {
//           throw new CrmebException("The password is 8-20 characters long and contains at least any three of uppercase letters, lowercase letters, numbers or special symbols");
//        }
        // 国内手机号
        if (loginRequest.getCountryCode().equals("+86") || loginRequest.getCountryCode().equals("86")) {
            ValidateFormUtil.isPhone(loginRequest.getPhone(), "wrong mobile number");
        }
        //检测验证码
        checkValidateCode(loginRequest.getCountryCode() + loginRequest.getPhone(), loginRequest.getCaptcha());
        //查询用户信息
        User user = userService.getByPhoneAndType(loginRequest.getPhone(), loginRequest.getCountryCode(), UserConstants.USER_LOGIN_TYPE_PHONE);
        if (ObjectUtil.isNotNull(user)) {
            throw new CrmebException("Mobile number is registered");
        }
        // 新用户注册流程
        user = userService.registerPhone(loginRequest.getPhone(), loginRequest.getCountryCode(), loginRequest.getPassword());
        //生成token
        LoginResponse loginResponse = new LoginResponse();
        String token = tokenComponent.createToken(user);
        loginResponse.setToken(token);
        loginResponse.setUid(user.getUid());
        loginResponse.setNikeName(user.getNickname());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setIdentity(user.getIdentity());
        loginResponse.setType(user.getUserType());
        return loginResponse;
    }

    /**
     * 手机号验证码登录
     *
     * @param loginRequest 登录信息
     * @return LoginResponse
     */
    @Override
    public LoginResponse phoneCaptchaLogin(LoginMobileRequest loginRequest) {
        if (StrUtil.isBlank(loginRequest.getCaptcha())) {
            throw new CrmebException("Mobile number verification code cannot be empty");
        }
        // 国内手机号
        if (loginRequest.getCountryCode().equals("+86") || loginRequest.getCountryCode().equals("86")) {
            ValidateFormUtil.isPhone(loginRequest.getPhone(), "Wrong format of phone number");
        }
        //检测验证码
        checkValidateCode(loginRequest.getCountryCode() + loginRequest.getPhone(), loginRequest.getCaptcha());
        //查询用户信息
        User user = userService.getByPhoneAndType(loginRequest.getPhone(), loginRequest.getCountryCode(), UserConstants.USER_LOGIN_TYPE_PHONE);
        if (ObjectUtil.isNull(user)) {// 此用户不存在，走新用户注册流程
            throw new CrmebException("Account does not exist");
        }
        if (!user.getStatus()) {
            throw new CrmebException("The current account is disabled, please contact the administrator！");
        }
        // 记录最后一次登录时间
        user.setLastLoginTime(DateUtil.nowDateTime());
        boolean b = userService.updateById(user);
        if (!b) {
            logger.error("When the user logs in, there is an error recording the last login time,uid = " + user.getUid());
        }

        //生成token
        LoginResponse loginResponse = new LoginResponse();
        String token = tokenComponent.createToken(user);
        loginResponse.setToken(token);
        loginResponse.setUid(user.getUid());
        loginResponse.setNikeName(user.getNickname());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setIdentity(user.getIdentity());
        loginResponse.setType(user.getUserType());
        return loginResponse;
    }

    /**
     * 手机号密码登录
     *
     * @param loginRequest 登录信息
     * @return LoginResponse
     */
    @Override
    public LoginResponse phonePasswordLogin(LoginMobileRequest loginRequest) {
        if (StrUtil.isBlank(loginRequest.getPassword())) {
            throw new CrmebException("password can not be blank");
        }
        // 国内手机号
        if (loginRequest.getCountryCode().equals("+86") || loginRequest.getCountryCode().equals("86")) {
            ValidateFormUtil.isPhone(loginRequest.getPhone(), "wrong mobile number");
        }
        //查询用户信息
        User user = userService.getByPhoneAndType(loginRequest.getPhone(), loginRequest.getCountryCode(), UserConstants.USER_LOGIN_TYPE_PHONE);
        if (ObjectUtil.isNull(user)) {// 此用户不存在，走新用户注册流程
            throw new CrmebException("Incorrect username or password");
        }
        if (!CrmebUtil.encryptPassword(loginRequest.getPassword(), loginRequest.getCountryCode().concat(loginRequest.getPhone()))
                .equals(user.getPwd())) {
            throw new CrmebException("Incorrect username or password");
        }
        if (!user.getStatus()) {
            throw new CrmebException("The current account is disabled, please contact the administrator！");
        }
        // 记录最后一次登录时间
        user.setLastLoginTime(DateUtil.nowDateTime());
        boolean b = userService.updateById(user);
        if (!b) {
            logger.error("When the user logs in, there is an error recording the last login time,uid = " + user.getUid());
        }

        //生成token
        LoginResponse loginResponse = new LoginResponse();
        String token = tokenComponent.createToken(user);
        loginResponse.setToken(token);
        loginResponse.setUid(user.getUid());
        loginResponse.setNikeName(user.getNickname());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setIdentity(user.getIdentity());
        loginResponse.setType(user.getUserType());
        return loginResponse;
    }

    /**
     * 发送邮箱忘记密码验证码
     *
     * @param email 邮箱
     * @return Boolean
     */
    @Override
    public Boolean emailForgetPassword(String email) {
        User user = userService.getByEmailAndIdentityAndType(email, "", UserConstants.USER_LOGIN_TYPE_EMAIL);
        if (ObjectUtil.isNull(user)) {
            throw new CrmebException("Account does not exist!");
        }
        return emailService.sendEmailCode(email);
    }

    /**
     * 邮箱重置密码
     *
     * @param request 重置密码信息
     * @return Boolean
     */
    @Override
    public Boolean emailResetPassword(EmailResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getPasswordAgain())) {
            throw new CrmebException("The two entered passwords do not match");
        }
        //检测验证码
        emailService.checkEmailValidateCode(request.getEmail(), request.getCaptcha());
        User user = userService.getByEmailAndIdentityAndType(request.getEmail(), "", UserConstants.USER_LOGIN_TYPE_EMAIL);
        if (ObjectUtil.isNull(user)) {
            throw new CrmebException("Email does not exist");
        }
        String password = CrmebUtil.encryptPassword(request.getNewPassword(), user.getIdentity());
        user.setPwd(password);
        return userService.updateById(user);
    }

    /**
     * 游客注册
     */
    @Override
    public LoginResponse visitorRegister(VisitorRegisterRequest registerRequest) {
        if (StrUtil.isNotBlank(registerRequest.getPhone()) && StrUtil.isBlank(registerRequest.getCountryCode())) {
            throw new CrmebException("GB area code cannot be empty");
        }
        User user = userService.visitorRegister(registerRequest);
        LoginResponse loginResponse = new LoginResponse();
        String token = tokenComponent.createToken(user);
        loginResponse.setToken(token);
        loginResponse.setIdentity(user.getIdentity());
        loginResponse.setNikeName(user.getNickname());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setUid(user.getUid());
        loginResponse.setType(user.getUserType());
        return loginResponse;
    }

    /**
     * 获取登录方式信息
     */
    @Override
    public LoginMethodResponse getLoginMethod() {
        LoginMethodResponse response = new LoginMethodResponse();
        response.setGoogleOpen(false);
        response.setTwitterOpen(false);
        response.setFacebookOpen(false);
        String googleOpen = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_GOOGLE_OPEN);
        String twitterOpen = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_TWITTER_OPEN);
        String facebookOpen = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_FACEBOOK_OPEN);
        if (googleOpen.equals(Constants.COMMON_SWITCH_OPEN_TYPE_ONE)) {
            String googleClientId = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_GOOGLE_CLIENT_ID);
            if (StrUtil.isBlank(googleClientId)) {
                throw new CrmebException("Please configure Google first");
            }
            response.setGoogleOpen(true);
            response.setGoogleClientId(googleClientId);
        }
        if (twitterOpen.equals(Constants.COMMON_SWITCH_OPEN_TYPE_ONE)) {
            response.setTwitterOpen(true);
        }
        if (facebookOpen.equals(Constants.COMMON_SWITCH_OPEN_TYPE_ONE)) {
            String facebookAppId = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_FACEBOOK_APPID);
            if (StrUtil.isBlank(facebookAppId)) {
                throw new CrmebException("Please configure Facebook first");
            }
            response.setFacebookOpen(true);
            response.setFacebookAppId(facebookAppId);
        }
        String visitorOpen = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_VISITOR_OPEN);
        response.setVisitorOpen(visitorOpen.equals(Constants.COMMON_SWITCH_OPEN_TYPE_ONE));
        response.setAgreement(systemConfigService.getUserRegisterAgreement());
        return response;
    }

    /**
     * 获取PC登录页图片
     */
    @Override
    public Map<String, Object> getPcLoginPic() {
        String loginLeftImage = systemConfigService.getValueByKey(SysConfigConstants.PC_LOGIN_LEFT_IMAGE);
        Map<String, Object> map = CollUtil.newHashMap();
        map.put("loginLeftImage", loginLeftImage);
        return map;
    }

    /**
     * 校验token是否有效
     * @return true 有效， false 无效
     */
    @Override
    public Boolean tokenIsExist() {
        Integer userId = userService.getUserId();
        return userId > 0;
    }

    /**
     * Google验证
     *
     * @param idTokenStr Google前端获取的idToken
     * @return GoogleIdToken
     */
    private GoogleIdToken googleVerify(String idTokenStr) {
        String googleOpen = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_GOOGLE_OPEN);
        if (googleOpen.equals(Constants.COMMON_SWITCH_CLOSE_TYPE_ONE)) {
            throw new CrmebException("请先开启GooGle开关");
        }
        String googleClientId = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_GOOGLE_CLIENT_ID);
        if (StrUtil.isBlank(googleClientId)) {
            throw new CrmebException("请先进行GooGle配置");
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(googleClientId))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenStr);
        } catch (GeneralSecurityException e) {
            System.out.println("验证时出现GeneralSecurityException异常");
            e.printStackTrace();
            throw new CrmebException("验证时出现GeneralSecurityException异常");
        } catch (IOException e) {
            System.out.println("验证时出现IOException异常");
            e.printStackTrace();
            throw new CrmebException("验证时出现IOException异常");
        }
        if (ObjectUtil.isNull(idToken)) {
            System.out.println("验证失败,Invalid ID token.");
            throw new CrmebException("验证失败,Invalid ID token.");
        }
        // 用户信息打印，之后删除
        GoogleIdToken.Payload payload = idToken.getPayload();
        // Print user identifier
        String userId = payload.getSubject();
        System.out.println("User ID: " + userId);
        // Get profile information from payload
        String email = payload.getEmail();
        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");
        System.out.println("email = " + email);
        System.out.println("emailVerified = " + emailVerified);
        System.out.println("name = " + name);
        System.out.println("pictureUrl = " + pictureUrl);
        System.out.println("locale = " + locale);
        System.out.println("familyName = " + familyName);
        System.out.println("givenName = " + givenName);
        System.out.println("payload = " + payload);
        return idToken;
    }
}
