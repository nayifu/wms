package com.zbkj.front.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.service.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * @Author 指缝de阳光
 * @Date 2022/1/13 15:26
 * @Version 1.0
 */
@Component
public class TwitterUtil {

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 获取requestToken
     *
     * @return RequestToken
     */
    public RequestToken getRequestToken(String end) {
        String twitterOpen = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_TWITTER_OPEN);
        if (twitterOpen.equals(Constants.COMMON_SWITCH_CLOSE_TYPE_ONE)) {
            throw new CrmebException("Please turn on the twitter switch first");
        }
        String consumerKey = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_TWITTER_CONSUMER_KEY);
        String consumerSecret = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_TWITTER_CONSUMER_SECRET);
        if (StrUtil.isBlank(consumerKey) || StrUtil.isBlank(consumerSecret)) {
            throw new CrmebException("Please configure twitter first");
        }

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumerKey);
        builder.setOAuthConsumerSecret(consumerSecret);
        Configuration configuration = builder.build();
        TwitterFactory twitterFactory = new TwitterFactory(configuration);
        Twitter twitter = twitterFactory.getInstance();
        RequestToken requestToken = null;
        try {
            String h5CallBackUrl = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_TWITTER_CALLBACK_URL_H5);
            String pcCallbackUrl = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_TWITTER_CALLBACK_URL_PC);
            String resultCallBack = null;
            // 根据参数获取对应回调
            if ("h5".equals(end) && ObjectUtil.isNotNull(h5CallBackUrl)) {
                resultCallBack = h5CallBackUrl;
            }
            if ("pc".equals(end) && ObjectUtil.isNotNull(pcCallbackUrl)) {
                resultCallBack = pcCallbackUrl;
            }
            if (ObjectUtil.isNull(resultCallBack)) {
                throw new CrmebException("config user twitter callbackurl success");
            }
            requestToken = twitter.getOAuthRequestToken(resultCallBack);
            System.out.println("requestToken" + JSON.toJSONString(requestToken));
        } catch (TwitterException e) {
            System.out.println("Get requestToken exception");
            throw new CrmebException("twitter request token failed:" + e.getErrorMessage());
        }
        return requestToken;
    }

    /**
     * 官方SDK疑似获取不到userId
     *
     * @param requestToken  requestToken
     * @param oauthVerifier oauthVerifier
     * @return RequestToken
     */
    public AccessToken getOAuth1AccessToken(RequestToken requestToken, String oauthVerifier) {
        String twitterOpen = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_TWITTER_OPEN);
        if (twitterOpen.equals(Constants.COMMON_SWITCH_CLOSE_TYPE_ONE)) {
            throw new CrmebException("Please turn on the twitter switch first");
        }
        String consumerKey = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_TWITTER_CONSUMER_KEY);
        String consumerSecret = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_TWITTER_CONSUMER_SECRET);
        if (StrUtil.isBlank(consumerKey) || StrUtil.isBlank(consumerSecret)) {
            throw new CrmebException("Please configure twitter first");
        }

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumerKey);
        builder.setOAuthConsumerSecret(consumerSecret);
        Configuration configuration = builder.build();
        TwitterFactory twitterFactory = new TwitterFactory(configuration);
        Twitter twitter = twitterFactory.getInstance();
        AccessToken accessToken = null;
        try {
            accessToken = twitter.getOAuthAccessToken(requestToken, oauthVerifier);
        } catch (TwitterException e) {
            e.printStackTrace();
            System.out.println("Get accessToken exception");
        }
        return accessToken;
    }

}
