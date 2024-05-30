package com.zbkj.service.service;


import com.zbkj.common.vo.LogisticsApiResponseVo;

/**
* ExpressService 接口
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
public interface LogisticService {

    /**
     * 查询物流信息
     * @param expNo 快递单号
     * @param expCode 快递公司编号
     * @return LogisticsApiResponseVo
     */
    LogisticsApiResponseVo query(String expNo, String expCode);
}
