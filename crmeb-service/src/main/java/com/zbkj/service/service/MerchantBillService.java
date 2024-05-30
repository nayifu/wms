package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.bill.MerchantBill;
import com.zbkj.common.request.FundsMonitorRequest;
import com.zbkj.common.request.PageParamRequest;

import java.util.List;

/**
*  MerchantBillService 接口
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
public interface MerchantBillService extends IService<MerchantBill> {

    /**
     * 通过pid获取商户帐单列表
     * @param pid 父级id
     * @return List
     */
    List<MerchantBill> getByParentId(Integer pid);

    /**
     * 资金监控
     * @param request          查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<MerchantBill> getFundMonitoring(FundsMonitorRequest request, PageParamRequest pageParamRequest);
}