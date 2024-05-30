package com.zbkj.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Crmeb 基础配置
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
@Configuration
@ConfigurationProperties(prefix = "crmeb")
public class CrmebConfig {
    // 当前代码版本
    private String version;
    // 待部署域名
    private String  domain;
    // #请求微信接口中专服务器
    private String wechatApiUrl;
    // #微信js api系列是否开启调试模式
    private Boolean wechatJsApiDebug;
    // #微信js api是否是beta版本
    private Boolean wechatJsApiBeta;
    // #是否同步config表数据到redis
    private Boolean asyncConfig;
    // #是否同步小程序公共模板库
    private Boolean asyncWeChatProgramTempList;
    // 本地图片路径配置
    private String imagePath;
    // 是否开启行为验证码
    private Boolean captchaOn;

    private String paypalBrandName;

    private String paypalReturnUri;

    private String paypalPcReturnUri;

    @Value("${wx.pay.notifyUrl}")
    private String wxpayNotifyUrl;

    @Value("${wx.pay.refundNotifyUrl}")
    private String wxpayRefundNotifyUrl;

    public String getWxpayNotifyUrl() {
        return wxpayNotifyUrl;
    }

    public void setWxpayNotifyUrl(String wxpayNotifyUrl) {
        this.wxpayNotifyUrl = wxpayNotifyUrl;
    }

    public String getWxpayRefundNotifyUrl() {
        return wxpayRefundNotifyUrl;
    }

    public void setWxpayRefundNotifyUrl(String wxpayRefundNotifyUrl) {
        this.wxpayRefundNotifyUrl = wxpayRefundNotifyUrl;
    }

    public String getPaypalPcReturnUri() {
        return paypalPcReturnUri;
    }

    public void setPaypalPcReturnUri(String paypalPcReturnUri) {
        this.paypalPcReturnUri = paypalPcReturnUri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPaypalBrandName() {
        return paypalBrandName;
    }

    public void setPaypalBrandName(String paypalBrandName) {
        this.paypalBrandName = paypalBrandName;
    }

    public String getPaypalReturnUri() {
        return paypalReturnUri;
    }

    public void setPaypalReturnUri(String paypalReturnUri) {
        this.paypalReturnUri = paypalReturnUri;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getWechatApiUrl() {
        return wechatApiUrl;
    }

    public void setWechatApiUrl(String wechatApiUrl) {
        this.wechatApiUrl = wechatApiUrl;
    }

    public Boolean getWechatJsApiDebug() {
        return wechatJsApiDebug;
    }

    public void setWechatJsApiDebug(Boolean wechatJsApiDebug) {
        this.wechatJsApiDebug = wechatJsApiDebug;
    }

    public Boolean getWechatJsApiBeta() {
        return wechatJsApiBeta;
    }

    public void setWechatJsApiBeta(Boolean wechatJsApiBeta) {
        this.wechatJsApiBeta = wechatJsApiBeta;
    }

    public Boolean getAsyncConfig() {
        return asyncConfig;
    }

    public void setAsyncConfig(Boolean asyncConfig) {
        this.asyncConfig = asyncConfig;
    }

    public Boolean getAsyncWeChatProgramTempList() {
        return asyncWeChatProgramTempList;
    }

    public void setAsyncWeChatProgramTempList(Boolean asyncWeChatProgramTempList) {
        this.asyncWeChatProgramTempList = asyncWeChatProgramTempList;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getCaptchaOn() {
        return captchaOn;
    }

    public void setCaptchaOn(Boolean captchaOn) {
        this.captchaOn = captchaOn;
    }

    public Boolean isAsyncConfig() {
        return asyncConfig;
    }

    @Override
    public String toString() {
        return "CrmebConfig{" +
                "version='" + version + '\'' +
                ", domain='" + domain + '\'' +
                ", wechatApiUrl='" + wechatApiUrl + '\'' +
                ", wechatJsApiDebug=" + wechatJsApiDebug +
                ", wechatJsApiBeta=" + wechatJsApiBeta +
                ", asyncConfig=" + asyncConfig +
                ", asyncWeChatProgramTempList=" + asyncWeChatProgramTempList +
                ", imagePath='" + imagePath + '\'' +
                ", captchaOn=" + captchaOn +
                ", paypalBrandName='" + paypalBrandName + '\'' +
                ", paypalReturnUri='" + paypalReturnUri + '\'' +
                ", paypalPcReturnUri='" + paypalPcReturnUri + '\'' +
                '}';
    }
}
