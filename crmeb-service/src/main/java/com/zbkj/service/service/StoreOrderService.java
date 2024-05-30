package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.order.OrderLogistics;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * StoreOrderService 接口
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
public interface StoreOrderService extends IService<StoreOrder> {

    /**
     * H5订单列表
     * @param uid 用户uid
     * @param status 评价等级|0=未支付,1=待发货,2=待收货,3=待评价,4=已完成,-3=售后/退款
     * @param pageParamRequest 分页参数
     * @return 订单结果列表
     */
    PageInfo<StoreOrder> getUserOrderList(Integer uid, Integer status, PageParamRequest pageParamRequest);

    /**
     * h5 top data 工具方法
     * @param status 状态参数
     * @return 查询到的订单结果
     */
    Integer getTopDataUtil(Integer status, Integer userId);

    List<StoreOrder> getOrderGroupByDate(String dateLimit, int lefTime);

    /**
     * 订单详情（PC）
     * @param orderNo 订单编号
     * @return StoreOrderInfoResponse
     */
    StoreOrderInfoResponse info(String orderNo);

    /**
     * 发货
     * @param request 发货参数
     * @return Boolean
     */
    Boolean send(StoreOrderSendRequest request);

    /**
     * 订单备注
     * @return Boolean
     */
    Boolean mark(OrderRemarkRequest request);

    /**
     * 获取订单快递信息
     * @param orderNo 订单编号
     * @return LogisticsResultVo
     */
    OrderLogistics getLogisticsInfo(String orderNo);

    StoreOrder getByOderNo(String orderNo);

    /**
     * 更新支付结果
     * @param masterNo 订单编号
     */
    Boolean updatePaidByMasterNo(String masterNo);

    /**
     * 根据用户uid查询所有已支付订单
     * @param userId 用户uid
     * @param pageParamRequest 分页参数
     * @return List<StoreOrder>
     */
    PageInfo<ExpensesRecordResponse> getExpensesRecord(Integer userId, PageParamRequest pageParamRequest);

    /**
     * 获取订单总数量
     * @param uid 用户uid
     * @return Integer
     */
    Integer getOrderCountByUid(Integer uid);

    /**
     * 获取用户总消费金额
     * @param userId 用户uid
     * @return BigDecimal
     */
    BigDecimal getSumPayPriceByUid(Integer userId);

    /**
     * 获取订单数量(时间)
     * @param uid 用户uid
     * @return Integer
     */
    Integer getOrderCountByUidAndDate(Integer uid, String date);

    /**
     * 获取用户消费金额(时间)
     * @param userId 用户uid
     * @return BigDecimal
     */
    BigDecimal getSumPayPriceByUidAndDate(Integer userId, String date);

    /**
     * 订单删除
     * @param orderNo 订单编号
     * @return Boolean
     */
    Boolean delete(String orderNo);

    /**
     * 通过日期获取商品交易件数
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    Integer getOrderProductNumByDate(String date);

    /**
     * 通过日期获取商品交易成功件数
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    Integer getOrderSuccessProductNumByDate(String date);

    /**
     * 通过日期获取订单数量
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    Integer getOrderNumByDate(Integer merId, String date);

    /**
     * 通过日期获取支付订单数量
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    Integer getPayOrderNumByDate(String date);

    /**
     * 通过日期获取支付订单金额
     * @param date 日期，yyyy-MM-dd格式
     * @return BigDecimal
     */
    BigDecimal getPayOrderAmountByDate(Integer merId, String date);

    /**
     * 通过日期获取支付订单金额
     * @param startDate 日期
     * @param endDate 日期
     * @return BigDecimal
     */
    BigDecimal getPayOrderAmountByPeriod(String startDate, String endDate);

    /**
     * 获取累计消费金额
     * @return BigDecimal
     */
    BigDecimal getTotalPrice();

    /**
     * 根据日期获取下单用户数量
     * @param date 日期
     * @return Integer
     */
    Integer getOrderUserNumByDate(String date);

    /**
     * 根据日期获取下单用户数量
     * @param startDate 日期
     * @param endDate 日期
     * @return Integer
     */
    Integer getOrderUserNumByPeriod(String startDate, String endDate);

    /**
     * 根据日期获取成交用户数量
     * @param date 日期
     * @return Integer
     */
    Integer getOrderPayUserNumByDate(String date);

    /**
     * 根据日期获取成交用户数量
     * @param startDate 日期
     * @param endDate 日期
     * @return Integer
     */
    Integer getOrderPayUserNumByPeriod(String startDate, String endDate);

    /**
     * 根据用户uid列表获取成交用户数量
     * @param uidList 用户列表
     * @return Integer
     */
    Integer getOrderPayUserNumByUidList(List<Integer> uidList);

    /**
     * 根据用户uid列表获取支付金额
     * @param uidList 用户列表
     * @return BigDecimal
     */
    BigDecimal getPayOrderAmountByUidList(List<Integer> uidList);

    /**
     * 获取待发货订单数量
     * @return Integer
     */
    Integer getNotShippingNum(Integer merId);

    /**
     * 获取退款中订单数量
     */
    Integer getRefundingNum(Integer merId);

    /**
     * 通过订单号列表获取订单列表
     */
    List<StoreOrder> findByOrderNoList(List<String> orderNoList);

    /**
     * 取消订单
     * @param masterNo 主订单号
     * @param isUser 是否用户取消
     * @return Boolean
     */
    Boolean cancelByMasterNo(String masterNo, Boolean isUser);

    /**
     * 根据主订单号获取订单列表
     * @param masterNo 主订单号
     * @return List
     */
    List<StoreOrder> getListByMasterNo(String masterNo);

    /**
     * 商户端后台分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<MerchantOrderPageResponse> getMerchantAdminPage(StoreOrderSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 获取商户端订单各状态数量
     * @param dateLimit 时间参数
     */
    StoreOrderCountItemResponse getMerchantOrderStatusNum(String dateLimit);

    /**
     * 平台端后台分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<PlatformOrderPageResponse> getPlatformAdminPage(PlatformOrderSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 获取平台端订单各状态数量
     * @param dateLimit 时间参数
     */
    StoreOrderCountItemResponse getPlatformOrderStatusNum(String dateLimit);

    /**
     * 订单备注
     * @return Boolean
     */
    Boolean platformMark(OrderRemarkRequest request);

    /**
     * 订单详情（平台）
     * @param orderNo 订单编号
     * @return StoreOrderInfoResponse
     */
    PlatformOrderInfoResponse platformInfo(String orderNo);

    /**
     * 根据日期获取支付订单数量
     * @param merId 商户id，0为全部商户
     * @param date 日期：年-月-日
     * @return Integer
     */
    Integer findPayNumByDate(Integer merId, String date);

    /**
     * 根据日期获取所有支付订单
     * @param merId 商户id，0为全部商户
     * @param date 日期：年-月-日
     * @return List
     */
    List<StoreOrder> findPayByDate(Integer merId, String date);

    /**
     * 根据月份获取支付订单数量
     * @param merId 商户id，0为全部商户
     * @param month 日期：年-月
     * @return Integer
     */
    Integer findPayNumByMonth(Integer merId, String month);

    /**
     * 根据日期获取所有支付订单
     * @param merId 商户id，0为全部商户
     * @param month 日期：年-月
     * @return List
     */
    List<StoreOrder> findPayByMonth(Integer merId, String month);
}
