package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.order.StoreRefundOrder;
import com.zbkj.common.request.*;
import com.zbkj.common.response.MerchantRefundOrderPageResponse;
import com.zbkj.common.response.PlatformRefundOrderPageResponse;
import com.zbkj.common.response.RefundOrderCountItemResponse;

import java.math.BigDecimal;
import java.util.List;

/**
*  StoreRefundOrderService 接口
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
public interface StoreRefundOrderService extends IService<StoreRefundOrder> {

    /**
     * 商户端退款订单分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<MerchantRefundOrderPageResponse> getMerchantAdminPage(RefundOrderSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 获取商户端退款订单各状态数量
     * @param dateLimit 时间参数
     * @return RefundOrderCountItemResponse
     */
    RefundOrderCountItemResponse getMerchantOrderStatusNum(String dateLimit);

    /**
     * 备注退款单
     * @param request 备注参数
     * @return Boolean
     */
    Boolean mark(RefundOrderRemarkRequest request);

    /**
     * 退款
     * @param request 退款参数
     * @return Boolean
     */
    Boolean refund(StoreOrderRefundRequest request);

    /**
     * 拒绝退款
     * @param request 拒绝退款参数
     * @return Boolean
     */
    Boolean refundRefuse(StoreOrderRefundRequest request);

    /**
     * 获取退款单
     * @param refundOrderNo 退款单号
     * @return StoreRefundOrder
     */
    StoreRefundOrder getByRefundOrderNo(String refundOrderNo);

    /**
     * 平台端退款订单分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<PlatformRefundOrderPageResponse> getPlatformAdminPage(PlatformRefundOrderSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 获取平台端退款订单各状态数量
     * @param dateLimit 时间参数
     * @return RefundOrderCountItemResponse
     */
    RefundOrderCountItemResponse getPlatformOrderStatusNum(String dateLimit);

    /**
     * 平台备注退款单
     * @param request 备注参数
     * @return Boolean
     */
    Boolean platformMark(RefundOrderRemarkRequest request);

    /**
     * 获取某一天的所有已退款的退款单
     * @param merId 商户id，0为所有商户
     * @param date 日期：年-月-日
     * @return List
     */
    List<StoreRefundOrder> findRefundedByDate(Integer merId, String date);

    /**
     * 获取退款单
     * @param orderNo 商户订单号
     * @return StoreRefundOrder
     */
    StoreRefundOrder getByMerchantOrderNo(String orderNo);

    /**
     * 退款订单列表
     * @param pageRequest 分页参数
     * @return List
     */
    PageInfo<StoreRefundOrder> getH5List(PageParamRequest pageRequest);

    /**
     * 根据日期获取退款订单数量
     * @param date 日期
     * @return Integer
     */
    Integer getRefundOrderNumByDate(String date);

    /**
     * 根据日期获取退款订单金额
     * @param date 日期
     * @return Integer
     */
    BigDecimal getRefundOrderAmountByDate(String date);

    /**
     * 待退款订单数量
     * @return Integer
     */
    Integer getAwaitAuditNum(Integer merId);

    /**
     * 待退款订单数量(用户)
     * @param userId 用户uid
     * @return Integer
     */
    Integer getAwaitNumByUid(Integer userId);
}