package com.zbkj.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.zbkj.admin.service.FinanceService;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.bill.*;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.merchant.MerchantInfo;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.model.transfer.TransferRecord;
import com.zbkj.common.model.user.User;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.TransferConfigVo;
import com.zbkj.service.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 财务服务实现类
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
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private BillService billService;
    @Autowired
    private UserService userService;
    @Autowired
    private MerchantBillService merchantBillService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private TransferRecordService transferRecordService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private SystemAdminService systemAdminService;
    @Autowired
    private SystemAttachmentService systemAttachmentService;
    @Autowired
    private PlatformDailyStatementService platformDailyStatementService;
    @Autowired
    private PlatformMonthStatementService platformMonthStatementService;
    @Autowired
    private MerchantDailyStatementService merchantDailyStatementService;
    @Autowired
    private MerchantMonthStatementService merchantMonthStatementService;

    /**
     * 资金监控分页列表
     * @param request          查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MonitorResponse> getFundMonitoring(FundsMonitorRequest request, PageParamRequest pageParamRequest) {
        PageInfo<Bill> billPageInfo = billService.getFundMonitoring(request, pageParamRequest);
        List<Bill> billList = billPageInfo.getList();
        if (CollUtil.isEmpty(billList)) {
            return CommonPage.copyPageInfo(billPageInfo, CollUtil.newArrayList());
        }
        List<Integer> uidList = billList.stream().filter(e -> e.getUid() > 0).map(Bill::getUid).collect(Collectors.toList());
        HashMap<Integer, User> userHashMap = userService.getMapListInUid(uidList);
        List<MonitorResponse> responseList = billList.stream().map(bill -> {
            MonitorResponse response = new MonitorResponse();
            BeanUtils.copyProperties(bill, response);
            if (ObjectUtil.isNotNull(userHashMap.get(bill.getUid()))) {
                response.setNickName(userHashMap.get(bill.getUid()).getNickname());
            } else {
                response.setNickName("");
            }
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(billPageInfo, responseList);
    }

    /**
     * 资金监控详情
     * @param billId 帐单id
     * @return List
     */
    @Override
    public List<MonitorInfoResponse> getFundMonitoringInfo(Integer billId) {
        Bill bill = billService.getById(billId);
        if (ObjectUtil.isNull(bill)) {
            throw new CrmebException("帐单不存在");
        }
        List<MerchantBill> merchantBillList = merchantBillService.getByParentId(billId);
        return merchantBillList.stream().map(b -> {
            MonitorInfoResponse response = new MonitorInfoResponse();
            BeanUtils.copyProperties(b, response);
            Merchant merchant = merchantService.getById(b.getMerId());
            response.setMerName(merchant.getName());
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 商户端资金监控分页列表
     * @param request          查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MonitorResponse> getMerchantFundMonitoring(FundsMonitorRequest request, PageParamRequest pageParamRequest) {
        PageInfo<MerchantBill> merchantBillPageInfo = merchantBillService.getFundMonitoring(request, pageParamRequest);
        List<MerchantBill> merchantBillList = merchantBillPageInfo.getList();
        if (CollUtil.isEmpty(merchantBillList)) {
            return CommonPage.copyPageInfo(merchantBillPageInfo, CollUtil.newArrayList());
        }
        List<Integer> uidList = merchantBillList.stream().filter(e -> e.getUid() > 0).map(MerchantBill::getUid).collect(Collectors.toList());
        HashMap<Integer, User> userHashMap = userService.getMapListInUid(uidList);
        List<MonitorResponse> responseList = merchantBillList.stream().map(bill -> {
            MonitorResponse response = new MonitorResponse();
            BeanUtils.copyProperties(bill, response);
            if (ObjectUtil.isNotNull(userHashMap.get(bill.getUid()))) {
                response.setNickName(userHashMap.get(bill.getUid()).getNickname());
            } else {
                response.setNickName("");
            }
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(merchantBillPageInfo, responseList);
    }

    /**
     * 转账申请
     * @param request 申请参数
     * @return Boolean
     */
    @Override
    public Boolean transferApply(TransferApplyRequest request) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Merchant merchant = merchantService.getByIdException(systemAdmin.getMerId());
        // 查询转账最低、最高金额进行判断
        String guaranteedAmountStr = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_GUARANTEED_AMOUNT);
        String transferMinAmount = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_TRANSFER_MIN_AMOUNT);
        String transferMaxAmount = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_TRANSFER_MAX_AMOUNT);
        if (merchant.getBalance().compareTo(new BigDecimal(guaranteedAmountStr)) < 1) {
            throw new CrmebException("商户余额不足提现保证金");
        }
        if (request.getAmount().compareTo(new BigDecimal(transferMinAmount)) < 0) {
            throw new CrmebException("提现金额小于最小提现金额");
        }
        if (request.getAmount().compareTo(new BigDecimal(transferMaxAmount)) > 0) {
            throw new CrmebException("提现金额大于最高提现金额");
        }

        MerchantInfo merchantInfo = merchantInfoService.getByMerId(merchant.getId());
        if (StrUtil.isEmpty(merchantInfo.getTransferName()) || StrUtil.isEmpty(merchantInfo.getTransferBank()) || StrUtil.isEmpty(merchantInfo.getTransferBankCard())) {
            throw new CrmebException("请先配置商户转账信息");
        }
        TransferRecord record = new TransferRecord();
        record.setMerId(merchant.getId());
        record.setAmount(request.getAmount());
        record.setTransferType(request.getTransferType());
        record.setTransferName(merchantInfo.getTransferName());
        record.setTransferBank(merchantInfo.getTransferBank());
        record.setTransferBankCard(merchantInfo.getTransferBankCard());
        if (StrUtil.isNotEmpty(request.getMark())) {
            record.setMark(request.getMark());
        }
        return transferRecordService.save(record);
    }

    /**
     * 平台端获取转账设置
     * 商户保证金额——指商户的余额至少大于该金额部分，才可以提现，设置为0时默认商户余额可以全部提现
     * 商户每笔最小转账额度——指商户的每次申请转账最小的金额；设置为0时默认不限制最小额度
     * 商户每笔最高转账额度——商户每次提现申请的最高额度，设置0时默认不限制
     * 商户余额冻结期——是指用户确认收货后多少天, 可自动分账到商户；设置为0，即无冻结期，默认显示15天。
     *
     * @return TransferConfigVo
     */
    @Override
    public TransferConfigVo getTransferConfig() {
        String guaranteedAmount = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_GUARANTEED_AMOUNT);
        String transferMinAmount = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_TRANSFER_MIN_AMOUNT);
        String transferMaxAmount = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_TRANSFER_MAX_AMOUNT);
        TransferConfigVo configVo = new TransferConfigVo();
        configVo.setGuaranteedAmount(new BigDecimal(guaranteedAmount));
        configVo.setTransferMinAmount(new BigDecimal(transferMinAmount));
        configVo.setTransferMaxAmount(new BigDecimal(transferMaxAmount));
        return configVo;
    }

    /**
     * 平台端编辑转账设置
     * @param request 编辑参数
     * @return Boolean
     */
    @Override
    public Boolean transferConfigEdit(TransferConfigVo request) {
        return transactionTemplate.execute(e -> {

            systemConfigService.updateOrSaveValueByName(SysConfigConstants.MERCHANT_GUARANTEED_AMOUNT, request.getGuaranteedAmount().toString());
            systemConfigService.updateOrSaveValueByName(SysConfigConstants.MERCHANT_TRANSFER_MIN_AMOUNT, request.getTransferMinAmount().toString());
            systemConfigService.updateOrSaveValueByName(SysConfigConstants.MERCHANT_TRANSFER_MAX_AMOUNT, request.getTransferMaxAmount().toString());
            return Boolean.TRUE;
        });
    }

    /**
     * 商户端获取转账申请基础信息
     * @return TransferBaseInfoResponse
     */
    @Override
    public TransferBaseInfoResponse getTransferBaseInfo() {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        // 商户转账配置信息
        MerchantInfo merchantInfo = merchantInfoService.getByMerId(systemAdmin.getMerId());
        if (StrUtil.isEmpty(merchantInfo.getTransferName())) {
            throw new CrmebException("请先在配置中进行商户转账信息");
        }
        TransferBaseInfoResponse response = new TransferBaseInfoResponse();
        response.setTransferType(merchantInfo.getTransferType());
        response.setTransferName(merchantInfo.getTransferName());
        response.setTransferBank(merchantInfo.getTransferBank());
        response.setTransferBankCard(merchantInfo.getTransferBankCard());

        Merchant merchant = merchantService.getByIdException(systemAdmin.getMerId());
        String guaranteedAmountStr = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_GUARANTEED_AMOUNT);
        String transferMinAmount = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_TRANSFER_MIN_AMOUNT);
        String transferMaxAmount = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_TRANSFER_MAX_AMOUNT);
        BigDecimal guaranteedAmount = new BigDecimal(guaranteedAmountStr);
        response.setGuaranteedAmount(guaranteedAmount);
        response.setTransferMinAmount(new BigDecimal(transferMinAmount));
        response.setTransferMaxAmount(new BigDecimal(transferMaxAmount));
        response.setBalance(merchant.getBalance());
        if (merchant.getBalance().compareTo(guaranteedAmount) < 1) {
            response.setTransferBalance(BigDecimal.ZERO);
        } else {
            response.setTransferBalance(merchant.getBalance().subtract(guaranteedAmount));
        }
        return response;
    }

    /**
     * 商户端转账记录分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<TransferRecordPageResponse> getMerchantTransferRecordList(TransferRecordRequest request, PageParamRequest pageParamRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        request.setMerId(systemAdmin.getMerId());
        PageInfo<TransferRecord> recordPageInfo = transferRecordService.getTransferRecordPageList(request, pageParamRequest);
        List<TransferRecord> recordList = recordPageInfo.getList();
        if (CollUtil.isEmpty(recordList)) {
            return CommonPage.copyPageInfo(recordPageInfo, CollUtil.newArrayList());
        }
        List<Integer> auditIdList = recordList.stream().map(TransferRecord::getAuditId).distinct().collect(Collectors.toList());
        Map<Integer, String> nameMap = systemAdminService.getNameMapByIdList(auditIdList);
        List<TransferRecordPageResponse> responseList = recordList.stream().map(record -> {
            TransferRecordPageResponse response = new TransferRecordPageResponse();
            BeanUtils.copyProperties(record, response);
            response.setAuditName(Optional.ofNullable(nameMap.get(record.getAuditId())).orElse(""));
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(recordPageInfo, responseList);
    }

    /**
     * 平台端转账记录分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<TransferRecordPlatformPageResponse> getPlatformTransferRecordList(TransferRecordRequest request, PageParamRequest pageParamRequest) {
        return transferRecordService.getPlatformTransferRecordList(request, pageParamRequest);
    }

    /**
     * 平台端转账记录详情
     * @param id 记录id
     * @return TransferRecordInfoResponse
     */
    @Override
    public TransferRecordInfoResponse getPlatformTransferRecordInfo(Integer id) {
        return transferRecordService.getPlatformTransferRecordInfo(id);
    }

    /**
     * 转账审核
     * @param request 审核参数
     * @return Boolean
     */
    @Override
    public Boolean transferAudit(TransferAuditRequest request) {
        if (request.getAuditStatus().equals(2) && StrUtil.isEmpty(request.getRefusalReason())) {
            throw new CrmebException("审核拒绝原因不能为空");
        }
        TransferRecord transferRecord = transferRecordService.getById(request.getId());
        if (ObjectUtil.isNull(transferRecord)) {
            throw new CrmebException("转账记录不存在!");
        }
        if (!transferRecord.getAuditStatus().equals(0)) {
            throw new CrmebException("转账审核状态异常!");
        }
        if (request.getAuditStatus().equals(1)) {
            Merchant merchant = merchantService.getById(transferRecord.getMerId());
            String guaranteedAmount = systemConfigService.getValueByKey(SysConfigConstants.MERCHANT_GUARANTEED_AMOUNT);
            if (new BigDecimal(guaranteedAmount).compareTo(merchant.getBalance().subtract(transferRecord.getAmount())) > 0) {
                throw new CrmebException("商户可提现余额不足，审核无法成功");
            }
        }
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        TransferRecord record = new TransferRecord();
        record.setId(request.getId());
        record.setAuditStatus(request.getAuditStatus());
        record.setAuditId(systemAdmin.getId());
        record.setAuditTime(DateUtil.date());
        if (StrUtil.isNotEmpty(request.getRefusalReason())) {
            record.setRefusalReason(request.getRefusalReason());
        }
        return transactionTemplate.execute(e -> {
            transferRecordService.updateById(record);
            if (request.getAuditStatus().equals(1)) {
                merchantService.operationBalance(transferRecord.getMerId(), transferRecord.getAmount(), Constants.OPERATION_TYPE_SUBTRACT);
            }
            return Boolean.TRUE;
        });
    }

    /**
     * 转账凭证
     * @param request 凭证参数
     * @return Boolean
     */
    @Override
    public Boolean transferProof(TransferProofRequest request) {
        TransferRecord transferRecord = transferRecordService.getById(request.getId());
        if (ObjectUtil.isNull(transferRecord)) {
            throw new CrmebException("转账记录不存在!");
        }
        if (!transferRecord.getAuditStatus().equals(1)) {
            throw new CrmebException("转账审核状态异常!");
        }
        transferRecord.setTransferProof(systemAttachmentService.clearPrefix(request.getTransferProof()));
        transferRecord.setTransferStatus(1);
        transferRecord.setTransferTime(DateUtil.date());
        return transferRecordService.updateById(transferRecord);
    }

    /**
     * 转账备注
     * @param request 备注参数
     * @return Boolean
     */
    @Override
    public Boolean transferRemark(TransferRemarkRequest request) {
        TransferRecord transferRecord = transferRecordService.getById(request.getId());
        if (ObjectUtil.isNull(transferRecord)) {
            throw new CrmebException("转账记录不存在!");
        }
        transferRecord.setPlatformMark(request.getRemark());
        return transferRecordService.updateById(transferRecord);
    }

    /**
     * 商户端转账记录详情
     * @param id 记录id
     * @return TransferRecordMerchantInfoResponse
     */
    @Override
    public TransferRecordMerchantInfoResponse getMerchantTransferRecordInfo(Integer id) {
        return transferRecordService.getMerchantTransferRecordInfo(id);
    }

    /**
     * 平台端日帐单分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<PlatformDailyStatement> getPlatformDailyStatementList(String dateLimit, PageParamRequest pageParamRequest) {
       return platformDailyStatementService.getPageList(dateLimit, pageParamRequest);
    }

    /**
     * 平台端月帐单分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<PlatformMonthStatement> getPlatformMonthStatementList(String dateLimit, PageParamRequest pageParamRequest) {
        return platformMonthStatementService.getPageList(dateLimit, pageParamRequest);
    }

    /**
     * 商户端日帐单分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MerchantDailyStatement> getMerchantDailyStatementList(String dateLimit, PageParamRequest pageParamRequest) {
        return merchantDailyStatementService.getPageList(dateLimit, pageParamRequest);
    }

    /**
     * 商户端月帐单分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MerchantMonthStatement> getMerchantMonthStatementList(String dateLimit, PageParamRequest pageParamRequest) {
        return merchantMonthStatementService.getPageList(dateLimit, pageParamRequest);
    }
}
