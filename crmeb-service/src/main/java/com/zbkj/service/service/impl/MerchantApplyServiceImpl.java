package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.MerchantConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.merchant.MerchantApply;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.LoginUserVo;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.MerchantApplyDao;
import com.zbkj.service.service.EmailService;
import com.zbkj.service.service.MerchantApplyService;
import com.zbkj.service.service.MerchantService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * StoreApplyServiceImpl 接口实现
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
public class MerchantApplyServiceImpl extends ServiceImpl<MerchantApplyDao, MerchantApply> implements MerchantApplyService {

    @Resource
    private MerchantApplyDao dao;

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private EmailService emailService;

    /**
     * 分页列表
     * @param searchRequest 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MerchantApply> getAdminPage(MerchantApplySearchRequest searchRequest, PageParamRequest pageParamRequest) {
        Page<MerchantApply> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<MerchantApply> lqw = Wrappers.lambdaQuery();
        if (ObjectUtil.isNotNull(searchRequest.getCategoryId())) {
            lqw.eq(MerchantApply::getCategoryId, searchRequest.getCategoryId());
        }
        if (ObjectUtil.isNotNull(searchRequest.getTypeId())) {
            lqw.eq(MerchantApply::getTypeId, searchRequest.getTypeId());
        }
        if (ObjectUtil.isNotNull(searchRequest.getAuditStatus())) {
            lqw.eq(MerchantApply::getAuditStatus, searchRequest.getAuditStatus());
        }
        if (StrUtil.isNotEmpty(searchRequest.getKeywords())) {
            lqw.and(i -> i.like(MerchantApply::getName, searchRequest.getKeywords())
                    .or().like(MerchantApply::getKeywords, searchRequest.getKeywords()));
        }
        if (StrUtil.isNotBlank(searchRequest.getDateLimit())) {
            dateLimitUtilVo dateLimitUtilVo = DateUtil.getDateLimit(searchRequest.getDateLimit());
            lqw.between(MerchantApply::getCreateTime, dateLimitUtilVo.getStartTime(), dateLimitUtilVo.getEndTime());
        }
        lqw.orderByDesc(MerchantApply::getId);
        List<MerchantApply> applyList = dao.selectList(lqw);
        if (CollUtil.isEmpty(applyList)) {
            return CommonPage.copyPageInfo(page, CollUtil.newArrayList());
        }
        return CommonPage.copyPageInfo(page, applyList);
    }

    /**
     * 入驻审核
     * @param request 审核参数
     * @return Boolean
     */
    @Override
    public Boolean audit(MerchantApplyAuditRequest request) {
        if (request.getAuditStatus().equals(MerchantConstants.AUDIT_STATUS_FAIL) && StrUtil.isEmpty(request.getDenialReason())) {
            throw new CrmebException("审核拒绝，拒绝原因不能为空");
        }
        MerchantApply merchantApply = getByIdException(request.getId());
        if (!merchantApply.getAuditStatus().equals(MerchantConstants.AUDIT_STATUS_WAIT)) {
            throw new CrmebException("申请已审核");
        }
        LoginUserVo loginUserVo = SecurityUtil.getLoginUserVo();
        // 审核拒绝
        if (request.getAuditStatus().equals(MerchantConstants.AUDIT_STATUS_FAIL)) {
            merchantApply.setAuditStatus(MerchantConstants.AUDIT_STATUS_FAIL);
            merchantApply.setDenialReason(request.getDenialReason());
            merchantApply.setAuditorId(loginUserVo.getUser().getId());
            return dao.updateById(merchantApply) > 0;
        }
        // 审核成功，初始化商户
        merchantApply.setAuditStatus(MerchantConstants.AUDIT_STATUS_SUCCESS);
        merchantApply.setAuditorId(loginUserVo.getUser().getId());
        String password = RandomUtil.randomString(8);
        Boolean execute = transactionTemplate.execute(e -> {
            dao.updateById(merchantApply);
            MerchantAddRequest merchantAddRequest = merchantInit(merchantApply);
            merchantAddRequest.setPassword(password);
            Boolean auditSuccess = merchantService.auditSuccess(merchantAddRequest, merchantApply.getAuditorId());
            if (!auditSuccess) {
                e.setRollbackOnly();
            }
            return Boolean.TRUE;
        });
        if (execute) {
            // 为申请用户发送邮件通知
            emailService.sendMerchantAuditSuccess(merchantApply.getEmail(), password);
        }
        return execute;
    }

    /**
     * 备注
     * @param request 备注参数
     * @return Boolean
     */
    @Override
    public Boolean remark(MerchantApplyRemarkRequest request) {
        MerchantApply merchantApply = getByIdException(request.getId());
        merchantApply.setRemark(request.getRemark());
        return dao.updateById(merchantApply) > 0;
    }

    /**
     * 商户入驻申请记录
     * @param uid 申请用户id
     * @param pageParamRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<MerchantApply> findSettledRecord(Integer uid, PageParamRequest pageParamRequest) {
        Page<MerchantApply> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<MerchantApply> lqw = Wrappers.lambdaQuery();
        lqw.eq(MerchantApply::getUid, uid);
        lqw.orderByDesc(MerchantApply::getId);
        List<MerchantApply> applyList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, applyList);
    }

    private MerchantAddRequest merchantInit(MerchantApply merchantApply) {
        MerchantAddRequest merchant = new MerchantAddRequest();
        BeanUtils.copyProperties(merchantApply, merchant);
//        merchant.setAccount(merchant.getEmail());
        return merchant;
    }

    private MerchantApply getByIdException(Integer id) {
        MerchantApply merchantApply = getById(id);
        if (ObjectUtil.isNull(merchantApply)) {
            throw new CrmebException("审核单不存在");
        }
        return merchantApply;
    }
}

