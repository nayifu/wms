package com.zbkj.front.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * wxpay pay properties.
 *
 * @author Binary Wang
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
    /**
     * 设置微信公众号或者小程序等的appid
     */
    private String appId;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * APIv3密钥
     */
    private String apiV3key;
    /**
     * p12证书的位置，可以指定绝对路径，也可以指定类路径（以classpath:开头） 商户私钥
     */
    private String privateKeyPath;

    /**
     * 同上，apiclient_cert.pem证书文件
     */
    private String privateCertPath;

    /**
     * 商户证书序列号
     */
    private String serialNo;

    /**
     * 支付回调通知地址
     */
    private String notifyUrl;

    /**
     * 退款回调通知地址
     */
    private String refundNotifyUrl;
}
