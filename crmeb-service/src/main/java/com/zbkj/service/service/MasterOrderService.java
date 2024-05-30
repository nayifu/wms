package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.request.PageParamRequest;

import java.util.List;

/**
*  MasterOrderService 接口
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
public interface MasterOrderService extends IService<MasterOrder> {

    /**
     * 获取订单
     * @param orderNo 订单号
     * @return MasterOrder
     */
    MasterOrder getByOrderNo(String orderNo);

    /**
     * 保存本地OrderNo和paymentId对应关系
     * @param orderNo 订单号
     * @param paymentId paypal的paymentId
     */
    Boolean addOutTradeNo(String orderNo, String paymentId);

    /**
     * 根据订单标识获取订单
     * @param outTradeNo 订单标识
     * @return MasterOrder
     */
    MasterOrder getByOutTradeNo(String outTradeNo);

    /**
     * 更新支付结果
     * @param orderNo 订单编号
     */
    Boolean updatePaid(String orderNo);

    /**
     * 取消订单
     * @param orderNo 订单号
     * @return Boolean
     */
    Boolean cancel(String orderNo);

    /**
     * 获取某一天的所有支付订单
     * @param date 日期：年-月-日
     * @return List
     */
    List<MasterOrder> findPayByDate(String date);

    /**
     * 获取某一月的所有支付订单
     * @param month 日期：年-月
     * @return List
     */
    List<MasterOrder> findPayByMonth(String month);

    /**
     * 待支付订单列表
     * @param uid 用户uid
     * @param pageRequest 分页参数
     * @return List
     */
    PageInfo<MasterOrder> getAwaitPayList(Integer uid, PageParamRequest pageRequest);

    /**
     * 待支付订单数量
     * @param uid 用户uid
     * @return Integer
     */
    Integer getAwaitPayCount(Integer uid);
}