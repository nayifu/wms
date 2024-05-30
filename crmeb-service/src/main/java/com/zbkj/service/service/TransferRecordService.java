package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.transfer.TransferRecord;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.TransferRecordRequest;
import com.zbkj.common.response.TransferRecordInfoResponse;
import com.zbkj.common.response.TransferRecordMerchantInfoResponse;
import com.zbkj.common.response.TransferRecordPlatformPageResponse;

/**
*  TransferRecordService 接口
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
public interface TransferRecordService extends IService<TransferRecord> {

    /**
     * 转账记录分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<TransferRecord> getTransferRecordPageList(TransferRecordRequest request, PageParamRequest pageParamRequest);

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
     * 商户端转账记录详情
     * @param id 记录id
     * @return TransferRecordMerchantInfoResponse
     */
    TransferRecordMerchantInfoResponse getMerchantTransferRecordInfo(Integer id);
}