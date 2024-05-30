package com.zbkj.admin.service;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.bill.MerchantDailyStatement;
import com.zbkj.common.model.bill.MerchantMonthStatement;
import com.zbkj.common.model.bill.PlatformDailyStatement;
import com.zbkj.common.model.bill.PlatformMonthStatement;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.vo.TransferConfigVo;

import java.util.List;

/**
 *  财务服务
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
public interface FinanceService {

    /**
     * 资金监控分页列表
     * @param request          查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<MonitorResponse> getFundMonitoring(FundsMonitorRequest request, PageParamRequest pageParamRequest);

    /**
     * 资金监控详情
     * @param billId 帐单id
     * @return List
     */
    List<MonitorInfoResponse> getFundMonitoringInfo(Integer billId);

    /**
     * 商户端资金监控分页列表
     * @param request          查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<MonitorResponse> getMerchantFundMonitoring(FundsMonitorRequest request, PageParamRequest pageParamRequest);

    /**
     * 转账申请
     * @param request 申请参数
     * @return Boolean
     */
    Boolean transferApply(TransferApplyRequest request);

    /**
     * 平台端获取转账设置
     * @return TransferConfigVo
     */
    TransferConfigVo getTransferConfig();

    /**
     * 平台端编辑转账设置
     * @param request 编辑参数
     * @return Boolean
     */
    Boolean transferConfigEdit(TransferConfigVo request);

    /**
     * 商户端获取转账申请基础信息
     * @return TransferBaseInfoResponse
     */
    TransferBaseInfoResponse getTransferBaseInfo();

    /**
     * 商户端转账记录分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<TransferRecordPageResponse> getMerchantTransferRecordList(TransferRecordRequest request, PageParamRequest pageParamRequest);

    /**
     * 平台端转账记录分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<TransferRecordPlatformPageResponse> getPlatformTransferRecordList(TransferRecordRequest request, PageParamRequest pageParamRequest);

    /**
     * 平台端转账记录详情
     * @param id 记录id
     * @return TransferRecordInfoResponse
     */
    TransferRecordInfoResponse getPlatformTransferRecordInfo(Integer id);

    /**
     * 转账审核
     * @param request 审核参数
     * @return Boolean
     */
    Boolean transferAudit(TransferAuditRequest request);

    /**
     * 转账凭证
     * @param request 凭证参数
     * @return Boolean
     */
    Boolean transferProof(TransferProofRequest request);

    /**
     * 转账备注
     * @param request 备注参数
     * @return Boolean
     */
    Boolean transferRemark(TransferRemarkRequest request);

    /**
     * 商户端转账记录详情
     * @param id 记录id
     * @return TransferRecordMerchantInfoResponse
     */
    TransferRecordMerchantInfoResponse getMerchantTransferRecordInfo(Integer id);

    /**
     * 平台端日帐单分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<PlatformDailyStatement> getPlatformDailyStatementList(String dateLimit, PageParamRequest pageParamRequest);

    /**
     * 平台端月帐单分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<PlatformMonthStatement> getPlatformMonthStatementList(String dateLimit, PageParamRequest pageParamRequest);

    /**
     * 商户端日帐单分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<MerchantDailyStatement> getMerchantDailyStatementList(String dateLimit, PageParamRequest pageParamRequest);

    /**
     * 商户端月帐单分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<MerchantMonthStatement> getMerchantMonthStatementList(String dateLimit, PageParamRequest pageParamRequest);
}
