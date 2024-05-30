package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.order.OrderLogistics;
import com.zbkj.service.dao.OrderLogisticsDao;
import com.zbkj.service.service.OrderLogisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * OrderLogisticsServiceImpl 接口实现
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
public class OrderLogisticsServiceImpl extends ServiceImpl<OrderLogisticsDao, OrderLogistics> implements OrderLogisticsService {

    @Resource
    private OrderLogisticsDao dao;

    /**
     * 获取所有派送中的记录
     * @return List
     */
    @Override
    public List<OrderLogistics> findAllDelivery() {
        LambdaQueryWrapper<OrderLogistics> lqw = Wrappers.lambdaQuery();
        lqw.lt(OrderLogistics::getState, 3);
        lqw.ge(OrderLogistics::getState, 0);
        return dao.selectList(lqw);
    }

    /**
     * 查询订单物流信息
     * @param orderNo 订单编号
     * @return OrderLogistics
     */
    @Override
    public OrderLogistics getByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderLogistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrderLogistics::getOrderNo, orderNo);
        lqw.last(" limit 1");
        return dao.selectOne(lqw);
    }
}

