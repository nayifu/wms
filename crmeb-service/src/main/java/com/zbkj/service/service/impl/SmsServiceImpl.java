package com.zbkj.service.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.NotifyConstants;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.sms.SmsRecord;
import com.zbkj.common.model.sms.SmsTemplate;
import com.zbkj.common.model.system.SystemNotification;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.common.utils.ValidateFormUtil;
import com.zbkj.service.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * SmsServiceImpl 接口实现
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
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SmsRecordService smsRecordService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private SystemNotificationService systemNotificationService;

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);


    /**
     * 发送支付成功短信
     * @param countryCode 国标区号
     * @param phone 手机号
     * @param payPrice 支付金额
     * @param orderNo 订单编号
     */
    @Override
    public Boolean sendPaySuccess(String countryCode, String phone, BigDecimal payPrice, String orderNo) {
        SystemNotification notification;
        notification = systemNotificationService.getByMark(NotifyConstants.PAY_SUCCESS_MARK);
        if (countryCode.equals("+86") || countryCode.equals("86")) {
            if (!notification.getIsSms().equals(1)) {
                logger.error("发送支付成功短信，未开启短信开关");
                return Boolean.FALSE;
            }
        } else {
            if (!notification.getIsOverseaSms().equals(1)) {
                logger.error("发送支付成功短信，未开启海外短信开关");
                return Boolean.FALSE;
            }
        }
        String signName = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_ALIYUN_SMS_SIGN_NAME);
        if (StrUtil.isBlank(signName)) {
            logger.error("发送支付成功短信，未配置阿里云短信签名");
            return Boolean.FALSE;
        }

        HashMap<String, Object> justPram = new HashMap<>();
        justPram.put("price", payPrice);
        justPram.put("orderNo", orderNo);

        SmsTemplate smsTemplate;
        if (countryCode.equals("+86") || countryCode.equals("86")) {
            smsTemplate = smsTemplateService.getDetail(notification.getSmsId());
        } else {
            smsTemplate = smsTemplateService.getDetail(notification.getOverseaSmsId());
        }
        String msgTempId = smsTemplate.getTempId();
        SendSmsResponse sendSmsResponse = aliyunSendSms(phone, countryCode, signName, msgTempId, justPram);
        saveRecord(countryCode + phone, msgTempId, justPram, sendSmsResponse);
        if (!sendSmsResponse.getBody().getCode().equals("OK")) {// 发送失败
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 发送订单发货提醒短信
     * @param countryCode 国标区号
     * @param phone 手机号
     * @param orderNo 订单编号
     */
    @Override
    public Boolean sendOrderDeliverNotice(String countryCode, String phone, String orderNo) {
        SystemNotification notification;
        notification = systemNotificationService.getByMark(NotifyConstants.DELIVER_GOODS_MARK);
        if (countryCode.equals("+86") || countryCode.equals("86")) {
            if (!notification.getIsSms().equals(1)) {
                logger.error("发送支付成功短信，未开启短信开关");
                return Boolean.FALSE;
            }
        } else {
            if (!notification.getIsOverseaSms().equals(1)) {
                logger.error("发送支付成功短信，未开启海外短信开关");
                return Boolean.FALSE;
            }
        }
        String signName = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_ALIYUN_SMS_SIGN_NAME);
        if (StrUtil.isBlank(signName)) {
            logger.error("发送支付成功短信，未配置阿里云短信签名");
            return Boolean.FALSE;
        }

        HashMap<String, Object> justPram = new HashMap<>();
        justPram.put("orderNo", orderNo);

        SmsTemplate smsTemplate;
        if (countryCode.equals("+86") || countryCode.equals("86")) {
            smsTemplate = smsTemplateService.getDetail(notification.getSmsId());
        } else {
            smsTemplate = smsTemplateService.getDetail(notification.getOverseaSmsId());
        }
        String msgTempId = smsTemplate.getTempId();
        SendSmsResponse sendSmsResponse = aliyunSendSms(phone, countryCode, signName, msgTempId, justPram);
        saveRecord(countryCode + phone, msgTempId, justPram, sendSmsResponse);
        if (!sendSmsResponse.getBody().getCode().equals("OK")) {// 发送失败
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 发送验证码
     * @param phone 手机号
     * @param countryCode 国标区号
     * @return Boolean
     */
    @Override
    public Boolean sendVerificationCode(String phone, String countryCode) {
        SystemNotification notification;
        // 国内手机号
        if (countryCode.equals("+86") || countryCode.equals("86")) {
            ValidateFormUtil.isPhoneException(phone,"wrong mobile number");
        }
        notification = systemNotificationService.getByMark(NotifyConstants.CAPTCHA_MARK);
        if (countryCode.equals("+86") || countryCode.equals("86")) {
            if (!notification.getIsSms().equals(1)) {
                throw new CrmebException("Please turn on the SMS switch first");
            }
        } else {
            if (!notification.getIsOverseaSms().equals(1)) {
                throw new CrmebException("Please turn on the overseas SMS switch first");
            }
        }
        String signName = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_ALIYUN_SMS_SIGN_NAME);
        if (StrUtil.isBlank(signName)) {
            throw new CrmebException("Please configure Alibaba Cloud SMS signature first");
        }

        //获取短信验证码过期时间
        String codeExpireStr = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_SMS_CODE_EXPIRE);
        if (StrUtil.isBlank(codeExpireStr) || Integer.parseInt(codeExpireStr) == 0) {
            codeExpireStr = Constants.NUM_FIVE + "";// 默认5分钟过期
        }
        Integer code = CrmebUtil.randomCount(111111, 999999);
        HashMap<String, Object> justPram = new HashMap<>();
        justPram.put("codeNo", code);

        SmsTemplate smsTemplate;
        if (countryCode.equals("+86") || countryCode.equals("86")) {
            smsTemplate = smsTemplateService.getDetail(notification.getSmsId());
        } else {
            smsTemplate = smsTemplateService.getDetail(notification.getOverseaSmsId());
        }
        String msgTempId = smsTemplate.getTempId();
        SendSmsResponse sendSmsResponse = aliyunSendSms(phone, countryCode, signName, msgTempId, justPram);
        saveRecord(countryCode + phone, msgTempId, justPram, sendSmsResponse);
        if (!sendSmsResponse.getBody().getCode().equals("OK")) {// 发送失败
            return Boolean.FALSE;
        }
        // 将验证码存入redis
        redisUtil.set(userService.getValidateCodeRedisKey(countryCode + phone), code, Long.valueOf(codeExpireStr), TimeUnit.MINUTES);
        return Boolean.TRUE;
    }

    /**
     * 短信发送记录
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<SmsRecord> record(PageParamRequest pageParamRequest) {
        return smsRecordService.pageList(pageParamRequest);
    }

    /**
     * 保存发送短信记录
     */
    private void saveRecord(String phone, String msgTempId, HashMap<String, Object> justPram, SendSmsResponse sendSmsResponse) {
        // 保存短信记录
        SmsRecord smsRecord = new SmsRecord();
        smsRecord.setPhone(phone);
        smsRecord.setContent(JSONObject.toJSONString(justPram));
        smsRecord.setCreateTime(DateUtil.date());
        smsRecord.setTemplate(msgTempId);
        SendSmsResponseBody body = sendSmsResponse.getBody();
        smsRecord.setCode(body.getCode());
        smsRecord.setMessage(body.getMessage());
        smsRecord.setAliRequestId(body.getRequestId());
        if (!body.getCode().equals("OK")) {// 发送失败
            logger.error(StrUtil.format("短信发送失败,code={},message={}", body.getCode(), body.getMessage()));
        }
        smsRecordService.save(smsRecord);
    }

    /**
     * 阿里云发送短信（单条）
     * @param phone 手机号
     * @param countryCode 国标区号
     * @param signName 签名
     * @param msgTempId 模板code
     * @param map 内容
     * @return SendSmsResponse,例:
     * {
     *   "RequestId": "BC6C19FD-EA00-5DEE-ADA9-1686835BC181",
     *   "Message": "OK",
     *   "BizId": "732620543007895761^0",
     *   "Code": "OK"
     * }
     */
    private SendSmsResponse aliyunSendSms(String phone, String countryCode, String signName, String msgTempId, HashMap<String, Object> map) {
        // 国内手机号
        if (!countryCode.equals("+86") && !countryCode.equals("86")) {
            phone = countryCode + phone;
        }
        String accessKeyId = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_ALIYUN_SMS_KEY_ID);
        if (StrUtil.isBlank(accessKeyId)) {
            throw new CrmebException("Please configure Alibaba Cloud SMS first");
        }
        String accessKeySecret = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_ALIYUN_SMS_KEY_SECRET);
        if (StrUtil.isBlank(accessKeySecret)) {
            throw new CrmebException("Please configure Alibaba Cloud SMS first");
        }

        Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client;
        try {
            client = new Client(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CrmebException("Error in initializing Alibaba Cloud client");
        }
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(signName)
                .setTemplateCode(msgTempId)
                .setTemplateParam(JSONObject.toJSONString(map));
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = client.sendSms(sendSmsRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CrmebException("Alibaba Cloud Client Sends SMS Abnormal");
        }
        return sendSmsResponse;
    }
}
