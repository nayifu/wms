package com.zbkj.service.service;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.order.OrderLogistics;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.vo.MyRecord;
import com.zbkj.common.vo.PreOrderVo;

import java.util.List;

/**
 * H5端订单操作
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
public interface OrderService {

    /**
     * 订单列表
     * @param type 类型
     * @param pageRequest 分页
     * @return 订单集合
     */
    PageInfo<OrderDetailResponse> list(Integer type, PageParamRequest pageRequest);

    /**
     * 订单详情
     * @param orderNo 订单id
     */
    StoreOrderDetailInfoResponse detailOrder(String orderNo);

    /**
     * 订单状态数量
     * @return 订单状态数据量
     */
    OrderDataResponse orderData();

    /**
     * 查询退款理由
     * @return 退款理由集合
     */
    List<String> getRefundReason();

    /**
     * 订单删除
     * @param orderNo 商户订单号
     * @return Boolean
     */
    Boolean delete(String orderNo);

    /**
     * 创建订单商品评价
     * @param request 请求参数
     * @return Boolean
     */
    Boolean reply(StoreProductReplyAddRequest request);

    /**
     * 订单收货
     * @param orderNo 商户订单号
     * @return Boolean
     */
    Boolean take(String orderNo);

    /**
     * 订单取消
     * @param orderNo 主订单号
     * @return Boolean
     */
    Boolean cancel(String orderNo);

    /**
     * 订单退款申请
     * @param request 申请参数
     * @return Boolean
     */
    Boolean refundApply(OrderRefundApplyRequest request);

    /**
     * 订单物流查看
     */
    OrderLogistics expressOrder(String orderNo);

    /**
     * 获取申请订单退款信息
     * @param orderNo 商户订单编号
     * @return ApplyRefundOrderInfoResponse
     */
    ApplyRefundOrderInfoResponse applyRefundOrderInfo(String orderNo);

    /**
     * 订单预下单
     * @param request 预下单请求参数
     * @return PreOrderResponse
     */
    MyRecord preOrder(PreOrderRequest request);

    /**
     * 加载预下单信息
     * @param preOrderNo 预下单号
     * @return 预下单信息
     */
    PreOrderVo loadPreOrder(String preOrderNo);

    /**
     * 计算订单价格
     * @param request 计算订单价格请求对象
     * @return ComputedOrderPriceResponse
     */
    ComputedOrderPriceResponse computedOrderPrice(OrderComputedPriceRequest request);

    /**
     * 创建订单
     * @param orderRequest 创建订单请求参数
     * @return MyRecord 订单编号
     */
    MyRecord createOrder(CreateOrderRequest orderRequest);

    /**
     * 游客订单详情
     * @param orderNo 订单号
     * @param identity 用户标识
     * @return StoreOrderDetailInfoResponse
     */
    StoreOrderDetailInfoResponse visitorStoreOrderDetail(String orderNo, String identity);

    /**
     * 退款订单详情
     * @param orderNo 退款订单号
     * @return RefundOrderInfoResponse
     */
    RefundOrderInfoResponse refundOrderDetail(String orderNo);

    /**
     * 退款订单列表
     * @param pageRequest 分页参数
     * @return List
     */
    PageInfo<RefundOrderResponse> getRefundOrderList(PageParamRequest pageRequest);

    /**
     * 订单商品评论列表
     * @param pageRequest 分页参数
     * @return List
     */
    PageInfo<InfoReplyResponse> replyList(PageParamRequest pageRequest);

    /**
     * 待支付订单列表
     * @param pageRequest 分页参数
     * @return List
     */
    PageInfo<MasterOrderAwaitPayResponse> awaitPayList(PageParamRequest pageRequest);

    /**
     * 主订单详情
     * @param orderNo 主订单订单号
     * @return MasterOrderDetailResponse
     */
    MasterOrderDetailResponse masterOrderDetail(String orderNo);

    /**
     * 主订单详情(游客订单查询)
     * @param orderNo 主订单号
     * @param identity 用户标识
     * @return MasterOrderDetailResponse
     */
    MasterOrderDetailResponse visitorMasterOrderDetail(String orderNo, String identity);

    /**
     * 个人中心订单数量
     * @return CenterOrderDataResponse
     */
    CenterOrderDataResponse getCenterOrderData();
}
