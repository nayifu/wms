package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.NotifyConstants;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.constants.UserConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.email.EmailTemplate;
import com.zbkj.common.model.system.SystemNotification;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.service.service.EmailService;
import com.zbkj.service.service.EmailTemplateService;
import com.zbkj.service.service.SystemConfigService;
import com.zbkj.service.service.SystemNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * EmailService 接口实现类
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
@Service
public class EmailServiceImpl implements EmailService {

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private SystemNotificationService systemNotificationService;

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return Boolean
     */
    @Override
    public Boolean sendEmailCode(String email) {
        SystemNotification notification = systemNotificationService.getByMark(NotifyConstants.CAPTCHA_MARK);
        if (ObjectUtil.isNull(notification)) {
            throw new CrmebException("Please configure message notification first");
        }
        if (!notification.getIsEmail().equals(1)) {
            throw new CrmebException("Please enable email notification configuration first");
        }
        EmailTemplate emailTemplate = emailTemplateService.getDetail(notification.getEmailId());

        List<String> to = CollUtil.newArrayList();
        to.add(email);
        // 生成验证码
        Integer code = CrmebUtil.randomCount(111111, 999999);
        String subject = emailTemplate.getSubject();
        String text = emailTemplate.getText().replace("{code}", code.toString());
        try {
            sendSimpleMail(subject, to, text);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
        // 将验证码存入redis
        String codeExpireStr = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_SMS_CODE_EXPIRE);
        if (StrUtil.isBlank(codeExpireStr) || Integer.parseInt(codeExpireStr) == 0) {
            codeExpireStr = Constants.NUM_FIVE + "";// 默认5分钟过期
        }
        redisUtil.set(getValidateCodeRedisKey(email), code, Long.valueOf(codeExpireStr), TimeUnit.MINUTES);
        return Boolean.TRUE;
    }

    /**
     * 检测邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    public void checkEmailValidateCode(String email, String code) {
        String codeRedisKey = getValidateCodeRedisKey(email);
        Object validateCode = redisUtil.get(codeRedisKey);
        if (ObjectUtil.isNull(validateCode)) {
            throw new CrmebException("Verification code has expired");
        }
        if (!validateCode.toString().equals(code)) {
            throw new CrmebException("Verification code error");
        }
        //删除验证码
        redisUtil.delete(codeRedisKey);
    }

    /**
     * 忘记密码验证码
     * @param email 邮箱
     * @return Boolean
     */
    @Override
    public Boolean emailForgetPassword(String email) {
        List<String> to = CollUtil.newArrayList();
        to.add(email);
        // 生成验证码
        Integer code = CrmebUtil.randomCount(111111, 999999);
        String subject = code + "is your Crmeb forget password verification code";
        String text = " Please enter this verification code on Crmeb when prompted " + code;
        try {
            sendSimpleMail(subject, to, text);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
        // 将验证码存入redis
        // 验证码过期时间,默认5分钟
        String codeExpireStr = Constants.NUM_FIVE + "";
        redisUtil.set(getValidateCodeRedisKey(email), code, Long.valueOf(codeExpireStr), TimeUnit.MINUTES);
        return Boolean.TRUE;
    }

    /**
     * 发送支付成功邮件
     * @param userType 用户类型
     * @param email 邮箱
     * @param payPrice 支付金额
     * @param orderNo 订单号
     * @param identity 用户标识
     * @return Boolean
     */
    @Override
    public Boolean sendPaySuccess(String userType, String email, BigDecimal payPrice, String orderNo, String identity) {
        SystemNotification notification = systemNotificationService.getByMark(NotifyConstants.PAY_SUCCESS_MARK);
        if (ObjectUtil.isNull(notification)) {
            logger.error("发送支付成功邮件，未配置邮箱通知");
            return Boolean.FALSE;
        }
        if (!notification.getIsEmail().equals(1)) {
            logger.error("发送支付成功邮件，未开启邮箱开关");
            return Boolean.FALSE;
        }
        EmailTemplate emailTemplate = emailTemplateService.getDetail(notification.getEmailId());

        List<String> to = CollUtil.newArrayList();
        to.add(email);
        String subject = emailTemplate.getSubject();
        String text = emailTemplate.getText().replace("{price}", payPrice.toString()).replace("{orderNo}", orderNo);
        if (userType.equals(UserConstants.USER_LOGIN_TYPE_VISITOR)) {
            // 匿名下单，发送查询链接
            text = text.concat(StrUtil.format(", link is : https://app.betawm.java.crmeb.net/pages/users/visitOrder/index?orderNo={}&identity={}", orderNo, identity));
        }
        try {
            sendSimpleMail(subject, to, text);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 发送发货邮件
     * @param email 邮箱
     * @param orderNo 订单号
     */
    @Override
    public Boolean sendOrderDeliver(String email, String orderNo) {
        SystemNotification notification = systemNotificationService.getByMark(NotifyConstants.DELIVER_GOODS_MARK);
        if (ObjectUtil.isNull(notification)) {
            logger.error("发送发货邮件,未配置邮箱通知");
            return Boolean.FALSE;
        }
        if (!notification.getIsEmail().equals(1)) {
            logger.error("发送发货邮件，未开启邮箱开关");
            return Boolean.FALSE;
        }
        EmailTemplate emailTemplate = emailTemplateService.getDetail(notification.getEmailId());

        List<String> to = CollUtil.newArrayList();
        to.add(email);
        String subject = emailTemplate.getSubject();
        String text = emailTemplate.getText().replace("{orderNo}", orderNo);
        try {
            sendSimpleMail(subject, to, text);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 发送商户申请成功邮件
     * @param email 邮箱
     * @param password 密码
     */
    @Override
    public Boolean sendMerchantAuditSuccess(String email, String password) {
        SystemNotification notification = systemNotificationService.getByMark(NotifyConstants.MERCHANT_AUDIT_SUCCESS);
        if (ObjectUtil.isNull(notification)) {
            logger.error("发送商户申请成功邮件,未配置邮箱通知");
            return Boolean.FALSE;
        }
        if (!notification.getIsEmail().equals(1)) {
            logger.error("发送商户申请成功邮件，未开启邮箱开关");
            return Boolean.FALSE;
        }
        EmailTemplate emailTemplate = emailTemplateService.getDetail(notification.getEmailId());

        List<String> to = CollUtil.newArrayList();
        to.add(email);
        String subject = emailTemplate.getSubject();
        String text = emailTemplate.getText().replace("{password}", password);
        try {
            sendSimpleMail(subject, to, text);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 获取邮箱验证码RedisKey
     * @param email 邮箱
     * @return String
     */
    private String getValidateCodeRedisKey(String email) {
        return Constants.EMAIL_VERIFICATION_CODE + email;
    }

    /**
     * 发送简单邮件
     * @param subject 主题
     * @param to 接收人列表
     * @param text 内容
     */
    private void sendSimpleMail(String subject, List<String> to, String text) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject(subject);
        // 设置邮件发送者，这个跟application.yml中设置的要一致
        message.setFrom(from);
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // message.setTo("10*****16@qq.com","12****32*qq.com");
        message.setTo(String.join(",", to));
        // 设置邮件抄送人，可以有多个抄送人
//        message.setCc("12****32*qq.com");
        // 设置隐秘抄送人，可以有多个
//        message.setBcc("7******9@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText(text);
        // 发送邮件
        javaMailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     * @throws MessagingException
     */
    public void sendAttachFileMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        // true表示构建一个可以带附件的邮件对象
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

        helper.setSubject("这是一封测试邮件");
        helper.setFrom("79******9@qq.com");
        helper.setTo("10*****16@qq.com");
        //helper.setCc("37xxxxx37@qq.com");
        //helper.setBcc("14xxxxx098@qq.com");
        helper.setSentDate(new Date());
        helper.setText("这是测试邮件的正文");
        // 第一个参数是自定义的名称，后缀需要加上，第二个参数是文件的位置
        helper.addAttachment("资料.xlsx",new File("/Users/gamedev/Desktop/测试数据 2.xlsx"));
        javaMailSender.send(mimeMessage);
    }

    /**
     * 正文中带图片的邮件
     * @throws MessagingException
     */
    public void sendImgResMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom("79*****39@qq.com");
        helper.setTo("10****6@qq.com");
        //helper.setCc("37xxxxx37@qq.com");
        //helper.setBcc("14xxxxx098@qq.com");
        helper.setSentDate(new Date());
        // src='cid:p01' 占位符写法 ，第二个参数true表示这是一个html文本
        helper.setText("<p>hello 大家好，这是一封测试邮件，这封邮件包含两种图片，分别如下</p><p>第一张图片：</p><img src='cid:p01'/><p>第二张图片：</p><img src='cid:p02'/>",true);
        // 第一个参数指的是html中占位符的名字，第二个参数就是文件的位置
        helper.addInline("p01",new FileSystemResource(new File("/Users/gamedev/Desktop/压缩.jpeg")));
        helper.addInline("p02",new FileSystemResource(new File("/Users/gamedev/Desktop/瑞文.jpg")));
        javaMailSender.send(mimeMessage);
    }


}
