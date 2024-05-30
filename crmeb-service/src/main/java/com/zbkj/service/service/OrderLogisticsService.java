package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.order.OrderLogistics;

import java.util.List;

/**
 * OrderLogisticsService 接口
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
public interface OrderLogisticsService extends IService<OrderLogistics> {

    /**
     * 获取所有派送中的记录
     * @return List
     */
    List<OrderLogistics> findAllDelivery();

    /**
     * 查询订单物流信息
     * @param orderNo 订单编号
     * @return OrderLogistics
     */
    OrderLogistics getByOrderNo(String orderNo);
}