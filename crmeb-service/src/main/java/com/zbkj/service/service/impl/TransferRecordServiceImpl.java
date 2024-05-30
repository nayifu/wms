package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.transfer.TransferRecord;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.TransferRecordRequest;
import com.zbkj.common.response.TransferRecordInfoResponse;
import com.zbkj.common.response.TransferRecordMerchantInfoResponse;
import com.zbkj.common.response.TransferRecordPlatformPageResponse;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.TransferRecordDao;
import com.zbkj.service.service.MerchantService;
import com.zbkj.service.service.TransferRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
*  TransferRecordServiceImpl 接口实现
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
public class TransferRecordServiceImpl extends ServiceImpl<TransferRecordDao, TransferRecord> implements TransferRecordService {

    @Resource
    private TransferRecordDao dao;

    @Autowired
    private MerchantService merchantService;

    /**
     * 转账记录分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<TransferRecord> getTransferRecordPageList(TransferRecordRequest request, PageParamRequest pageParamRequest) {
        Page<TransferRecord> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<TransferRecord> lqw = Wrappers.lambdaQuery();
        if (ObjectUtil.isNotNull(request.getMerId())) {
            lqw.eq(TransferRecord::getMerId, request.getMerId());
        }
        if (ObjectUtil.isNotNull(request.getAuditStatus())) {
            lqw.eq(TransferRecord::getAuditStatus, request.getAuditStatus());
        }
        if (ObjectUtil.isNotNull(request.getTransferStatus())) {
            lqw.eq(TransferRecord::getTransferStatus, request.getTransferStatus());
        }
        if (StrUtil.isNotEmpty(request.getDateLimit())) {
            dateLimitUtilVo dateLimit = com.zbkj.common.utils.DateUtil.getDateLimit(request.getDateLimit());
            lqw.between(TransferRecord::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        lqw.orderByDesc(TransferRecord::getId);
        List<TransferRecord> recordList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, recordList);
    }

    /**
     * 平台端转账记录分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<TransferRecordPlatformPageResponse> getPlatformTransferRecordList(TransferRecordRequest request, PageParamRequest pageParamRequest) {
        Map<String, Object> map = CollUtil.newHashMap();
        if (ObjectUtil.isNotNull(request.getMerId())) {
            map.put("merId", request.getMerId());
        }
        if (ObjectUtil.isNotNull(request.getAuditStatus())) {
            map.put("auditStatus", request.getAuditStatus());
        }
        if (ObjectUtil.isNotNull(request.getTransferStatus())) {
            map.put("transferStatus", request.getTransferStatus());
        }
        if (StrUtil.isNotEmpty(request.getDateLimit())) {
            dateLimitUtilVo dateLimit = com.zbkj.common.utils.DateUtil.getDateLimit(request.getDateLimit());
            map.put("startTime", dateLimit.getStartTime());
            map.put("endTime", dateLimit.getEndTime());
        }
        Page<TransferRecord> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<TransferRecordPlatformPageResponse> list = dao.getPlatformTransferRecordList(map);
        return CommonPage.copyPageInfo(page, list);
    }

    /**
     * 平台端转账记录详情
     * @param id 记录id
     * @return TransferRecordInfoResponse
     */
    @Override
    public TransferRecordInfoResponse getPlatformTransferRecordInfo(Integer id) {
        TransferRecord record = getByIdException(id);
        Merchant merchant = merchantService.getByIdException(record.getMerId());
        TransferRecordInfoResponse response = new TransferRecordInfoResponse();
        BeanUtils.copyProperties(record, response);
        response.setMerName(merchant.getName());
        response.setBalance(merchant.getBalance());
        return response;
    }

    /**
     * 商户端转账记录详情
     * @param id 记录id
     * @return TransferRecordMerchantInfoResponse
     */
    @Override
    public TransferRecordMerchantInfoResponse getMerchantTransferRecordInfo(Integer id) {
        TransferRecord record = getByIdException(id);
        TransferRecordMerchantInfoResponse response = new TransferRecordMerchantInfoResponse();
        BeanUtils.copyProperties(record, response);
        return response;
    }

    private TransferRecord getByIdException(Integer id) {
        TransferRecord record = getById(id);
        if (ObjectUtil.isNull(record)) {
            throw new CrmebException("转账记录不存在");
        }
        return record;
    }
}

