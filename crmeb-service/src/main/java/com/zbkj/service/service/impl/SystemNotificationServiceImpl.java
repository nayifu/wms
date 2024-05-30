package com.zbkj.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.email.EmailTemplate;
import com.zbkj.common.model.sms.SmsTemplate;
import com.zbkj.common.model.system.SystemNotification;
import com.zbkj.common.request.NotificationInfoRequest;
import com.zbkj.common.request.NotificationSearchRequest;
import com.zbkj.common.request.NotificationUpdateRequest;
import com.zbkj.common.response.NotificationInfoResponse;
import com.zbkj.service.dao.SystemNotificationDao;
import com.zbkj.service.service.EmailTemplateService;
import com.zbkj.service.service.SmsTemplateService;
import com.zbkj.service.service.SystemNotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * SystemNotificationServiceImpl 接口实现
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
public class SystemNotificationServiceImpl extends ServiceImpl<SystemNotificationDao, SystemNotification> implements SystemNotificationService {

    @Resource
    private SystemNotificationDao dao;

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private EmailTemplateService emailTemplateService;

    /**
     * 系统通知列表
     * @param request 查询对象
     * @return List
     */
    @Override
    public List<SystemNotification> getList(NotificationSearchRequest request) {
        LambdaQueryWrapper<SystemNotification> lqw = Wrappers.lambdaQuery();
        if (ObjectUtil.isNotNull(request.getSendType())) {
            lqw.eq(SystemNotification::getSendType, request.getSendType());
        }
        return dao.selectList(lqw);
    }

    /**
     * 发送短信开关
     * @param id 通知id
     * @return Boolean
     */
    @Override
    public Boolean smsSwitch(Integer id) {
        SystemNotification systemNotification = getByIdException(id);
        if (systemNotification.getIsSms().equals(0)) {
            throw new CrmebException("通知没有配置短信");
        }
        LambdaUpdateWrapper<SystemNotification> luw = Wrappers.lambdaUpdate();
        luw.set(SystemNotification::getIsSms, systemNotification.getIsSms().equals(1) ? 2 : 1);
        luw.eq(SystemNotification::getId, id);
        return update(luw);
    }

    /**
     * 通知详情
     * @param request 详情请求参数
     * @return NotificationInfoResponse
     */
    @Override
    public NotificationInfoResponse getDetail(NotificationInfoRequest request) {
        SystemNotification notification = getByIdException(request.getId());
        NotificationInfoResponse response = new NotificationInfoResponse();
        if (request.getDetailType().equals(Constants.NOTIFICATION_DETAIL_TYPE_SMS)) {
            if (notification.getIsSms().equals(0)) {
                throw new CrmebException("请先配置短信模板");
            }
            SmsTemplate smsTemplate = smsTemplateService.getDetail(notification.getSmsId());
            BeanUtils.copyProperties(smsTemplate, response);
            response.setStatus(notification.getIsSms());
        }
        if (request.getDetailType().equals(Constants.NOTIFICATION_DETAIL_TYPE_EMAIL)) {
            if (notification.getIsEmail().equals(0)) {
                throw new CrmebException("请先配置邮件模板");
            }
            EmailTemplate emailTemplate = emailTemplateService.getDetail(notification.getEmailId());
            BeanUtils.copyProperties(emailTemplate, response);
            response.setStatus(notification.getIsSms());
        }
        if (request.getDetailType().equals(Constants.NOTIFICATION_DETAIL_TYPE_OVERSEA_SMS)) {
            if (notification.getIsOverseaSms().equals(0)) {
                throw new CrmebException("请先配置海外短信模板");
            }
            SmsTemplate smsTemplate = smsTemplateService.getDetail(notification.getOverseaSmsId());
            BeanUtils.copyProperties(smsTemplate, response);
            response.setStatus(notification.getIsOverseaSms());
        }
        return response;
    }

    /**
     * 根据标识查询信息
     * @param mark 标识
     * @return SystemNotification
     */
    @Override
    public SystemNotification getByMark(String mark) {
        LambdaQueryWrapper<SystemNotification> lqw = Wrappers.lambdaQuery();
        lqw.eq(SystemNotification::getMark, mark);
        return dao.selectOne(lqw);
    }

    /**
     * 修改通知
     * @param request 请求参数
     * @return Boolean
     */
    @Override
    public Boolean modify(NotificationUpdateRequest request) {
        if (!request.getDetailType().equals(Constants.NOTIFICATION_DETAIL_TYPE_SMS) && StrUtil.isEmpty(request.getTempId())) {
            throw new CrmebException("模板id不能为空");
        }
        SystemNotification notification = getByIdException(request.getId());
        if (request.getDetailType().equals(Constants.NOTIFICATION_DETAIL_TYPE_SMS) && !notification.getIsSms().equals(request.getStatus())) {
            notification.setIsSms(request.getStatus());
            return transactionTemplate.execute(e -> {
                if (StrUtil.isNotBlank(request.getTempId())) {
                    SmsTemplate smsTemplate = smsTemplateService.getDetail(notification.getSmsId());
                    smsTemplate.setTempId(request.getTempId());
                    smsTemplateService.updateById(smsTemplate);
                }
                updateById(notification);
                return Boolean.TRUE;
            });
        }
        if (request.getDetailType().equals(Constants.NOTIFICATION_DETAIL_TYPE_OVERSEA_SMS) && !notification.getIsOverseaSms().equals(request.getStatus())) {
            notification.setIsOverseaSms(request.getStatus());
            return transactionTemplate.execute(e -> {
                if (StrUtil.isNotBlank(request.getTempId())) {
                    SmsTemplate smsTemplate = smsTemplateService.getDetail(notification.getOverseaSmsId());
                    smsTemplate.setTempId(request.getTempId());
                    smsTemplateService.updateById(smsTemplate);
                }
                updateById(notification);
                return Boolean.TRUE;
            });
        }
        if (request.getDetailType().equals(Constants.NOTIFICATION_DETAIL_TYPE_EMAIL) && !notification.getIsEmail().equals(request.getStatus())) {
            notification.setIsEmail(request.getStatus());
            return updateById(notification);
        }
        return true;
    }

    /**
     * 发送邮箱开关
     * @param id 通知id
     * @return Boolean
     */
    @Override
    public Boolean emailSwitch(Integer id) {
        SystemNotification systemNotification = getByIdException(id);
        if (systemNotification.getIsEmail().equals(0)) {
            throw new CrmebException("通知没有配置邮箱");
        }
        LambdaUpdateWrapper<SystemNotification> luw = Wrappers.lambdaUpdate();
        luw.set(SystemNotification::getIsEmail, systemNotification.getIsEmail().equals(1) ? 2 : 1);
        luw.eq(SystemNotification::getId, id);
        return update(luw);
    }

    /**
     * 发送海外短信开关
     * @param id 通知id
     * @return Boolean
     */
    @Override
    public Boolean overseaSmsSwitch(Integer id) {
        SystemNotification systemNotification = getByIdException(id);
        if (systemNotification.getIsOverseaSms().equals(0)) {
            throw new CrmebException("通知没有配置海外短信");
        }
        LambdaUpdateWrapper<SystemNotification> luw = Wrappers.lambdaUpdate();
        luw.set(SystemNotification::getIsOverseaSms, systemNotification.getIsOverseaSms().equals(1) ? 2 : 1);
        luw.eq(SystemNotification::getId, id);
        return update(luw);
    }

    private SystemNotification getByIdException(Integer id) {
        SystemNotification notification = getById(id);
        if (ObjectUtil.isNull(notification)) {
            throw new CrmebException("系统通知不存在");
        }
        return notification;
    }
}

