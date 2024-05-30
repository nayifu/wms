package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.dao.MasterOrderDao;
import com.zbkj.service.service.MasterOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
*  MasterOrderServiceImpl 接口实现
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
public class MasterOrderServiceImpl extends ServiceImpl<MasterOrderDao, MasterOrder> implements MasterOrderService {

    @Resource
    private MasterOrderDao dao;

    /**
     * 获取订单
     * @param orderNo 订单号
     * @return MasterOrder
     */
    @Override
    public MasterOrder getByOrderNo(String orderNo) {
        LambdaQueryWrapper<MasterOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(MasterOrder::getOrderNo, orderNo);
        lqw.last(" limit 1");
        return dao.selectOne(lqw);
    }

    /**
     * 保存本地OrderNo和paymentId对应关系
     * @param orderNo 订单号
     * @param paymentId paypal的paymentId
     */
    @Override
    public Boolean addOutTradeNo(String orderNo, String paymentId) {
        LambdaUpdateWrapper<MasterOrder> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(MasterOrder::getOutTradeNo, paymentId);
        wrapper.eq(MasterOrder::getOrderNo, orderNo);
        return update(wrapper);
    }

    /**
     * 根据订单标识获取订单
     * @param outTradeNo 订单标识
     * @return MasterOrder
     */
    @Override
    public MasterOrder getByOutTradeNo(String outTradeNo) {
        LambdaQueryWrapper<MasterOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(MasterOrder::getOutTradeNo, outTradeNo);
        lqw.last(" limit 1");
        return dao.selectOne(lqw);
    }

    /**
     * 更新支付结果
     * @param orderNo 订单编号
     */
    @Override
    public Boolean updatePaid(String orderNo) {
        LambdaUpdateWrapper<MasterOrder> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(MasterOrder::getPaid, true);
        wrapper.set(MasterOrder::getPayTime, DateUtil.nowDateTime());
        wrapper.eq(MasterOrder::getOrderNo, orderNo);
        wrapper.eq(MasterOrder::getPaid, false);
        return update(wrapper);
    }

    /**
     * 取消订单
     * @param orderNo 订单号
     * @return Boolean
     */
    @Override
    public Boolean cancel(String orderNo) {
        LambdaUpdateWrapper<MasterOrder> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(MasterOrder::getIsCancel, true);
        wrapper.eq(MasterOrder::getOrderNo, orderNo);
        wrapper.eq(MasterOrder::getPaid, false);
        return update(wrapper);
    }

    /**
     * 获取某一天的所有支付订单
     * @param date 日期：年月日
     * @return List
     */
    @Override
    public List<MasterOrder> findPayByDate(String date) {
        LambdaQueryWrapper<MasterOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(MasterOrder::getPaid, 1);
        lqw.apply("date_format(pay_time, '%Y-%m-%d') = {0}", date);
        return dao.selectList(lqw);
    }

    /**
     * 获取某一月的所有支付订单
     * @param month 日期：年-月
     * @return List
     */
    @Override
    public List<MasterOrder> findPayByMonth(String month) {
        LambdaQueryWrapper<MasterOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(MasterOrder::getPaid, 1);
        lqw.apply("date_format(pay_time, '%Y-%m') = {0}", month);
        return dao.selectList(lqw);
    }

    /**
     * 待支付订单列表
     * @param uid 用户uid
     * @param pageRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<MasterOrder> getAwaitPayList(Integer uid, PageParamRequest pageRequest) {
        Page<MasterOrder> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit());
        LambdaQueryWrapper<MasterOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(MasterOrder::getUid, uid);
        lqw.eq(MasterOrder::getPaid, 0);
        lqw.eq(MasterOrder::getIsCancel, 0);
        lqw.orderByDesc(MasterOrder::getId);
        List<MasterOrder> orderList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, orderList);
    }

    /**
     * 待支付订单数量
     * @param uid 用户uid
     * @return Integer
     */
    @Override
    public Integer getAwaitPayCount(Integer uid) {
        LambdaQueryWrapper<MasterOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(MasterOrder::getUid, uid);
        lqw.eq(MasterOrder::getPaid, 0);
        lqw.eq(MasterOrder::getIsCancel, 0);
        return dao.selectCount(lqw);
    }
}

