package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.order.StoreOrderInfo;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.StoreOrderInfoVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * StoreOrderInfoService 接口
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
public interface StoreOrderInfoService extends IService<StoreOrderInfo> {

    /**
     * 获取订单详情vo列表
     * @param orderNo 订单号
     * @return List<StoreOrderInfoVo>
     */
    List<StoreOrderInfoVo> getVoListByOrderNo(String orderNo);

    /**
     * 获取订单详情-订单编号
     * @param orderNo 订单编号
     * @return List
     */
    List<StoreOrderInfo> getListByOrderNo(String orderNo);

    /**
     * 根据时间、商品id获取销售件数
     * @param date 时间，格式'yyyy-MM-dd'
     * @param proId 商品id
     * @return Integer
     */
    Integer getSalesNumByDateAndProductId(String date, Integer proId);

    /**
     * 根据时间、商品id获取销售额
     * @param date 时间，格式'yyyy-MM-dd'
     * @param proId 商品id
     * @return BigDecimal
     */
    BigDecimal getSalesByDateAndProductId(String date, Integer proId);

    /**
     * 通过订单号字符串查询
     * @param orderNoList 订单号列表
     * @return List
     */
    List<StoreOrderInfo> getListByOrderNoList(List<String> orderNoList);

    /**
     * 查询订单详情未评论数量
     * @param merOrderNo 商户订单号
     */
    Integer getNotReplyNumByOrderNo(String merOrderNo);

    /**
     * 订单商品评论列表
     * @param pageRequest 分页参数
     * @return List
     */
    PageInfo<StoreOrderInfo> getReplyList(Integer userId, PageParamRequest pageRequest);

    /**
     * 订单收货
     * @param orderNo 订单号
     * @return Boolean
     */
    Boolean orderReceipt(String orderNo);

    /**
     * 获取待评论数量
     * @param userId 用户uid
     * @return Integer
     */
    Integer getAwaitNumByUid(Integer userId);
}
