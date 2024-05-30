package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.order.StoreOrderRefundStatus;

/**
*  StoreOrderRefundStatusService 接口
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
public interface StoreOrderRefundStatusService extends IService<StoreOrderRefundStatus> {

    /**
     * 添加订单日志
     * @param refundOrderNo 退款订单号
     * @param type 类型
     * @param message 备注
     * @return Boolean
     */
    Boolean createLog(String refundOrderNo, String type, String message);
}