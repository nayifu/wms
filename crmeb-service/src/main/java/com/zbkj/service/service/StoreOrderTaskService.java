package com.zbkj.service.service;


import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreRefundOrder;

/**
 * 订单任务服务
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
public interface StoreOrderTaskService {

    /**
     * 取消订单后置操作
     * @param masterOrder 主订单
     * @return Boolean
     */
    Boolean cancelByUser(MasterOrder masterOrder);

//    Boolean complete(StoreOrder storeOrder);

    /**
     * 退款后续
     * @param refundOrder 退款单
     */
    Boolean refundOrder(StoreRefundOrder refundOrder);

    /**
     * 超时未支付系统自动取消
     */
    Boolean autoCancel(MasterOrder masterOrder);

}
