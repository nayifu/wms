package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.transfer.TransferRecord;
import com.zbkj.common.response.TransferRecordPlatformPageResponse;

import java.util.List;
import java.util.Map;

/**
 * 转账记录表 Mapper 接口
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
public interface TransferRecordDao extends BaseMapper<TransferRecord> {

    /**
     * 平台端转账记录分页列表
     */
    List<TransferRecordPlatformPageResponse> getPlatformTransferRecordList(Map<String, Object> map);
}
