package com.zbkj.front.controller;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.order.OrderLogistics;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.vo.PreOrderVo;
import com.zbkj.service.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
@Slf4j
@RestController("StoreOrderFrontController")
@RequestMapping("api/front/order")
@Api(tags = "订单")
public class StoreOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "预下单")
    @RequestMapping(value = "/pre/order", method = RequestMethod.POST)
    public CommonResult<Map<String, Object>> preOrder(@RequestBody @Validated PreOrderRequest request) {
        return CommonResult.success(orderService.preOrder(request));
    }

    @ApiOperation(value = "加载预下单")
    @RequestMapping(value = "load/pre/{preOrderNo}", method = RequestMethod.GET)
    public CommonResult<PreOrderVo> loadPreOrder(@PathVariable String preOrderNo) {
        return CommonResult.success(orderService.loadPreOrder(preOrderNo));
    }

    @ApiOperation(value = "计算订单价格")
    @RequestMapping(value = "/computed/price", method = RequestMethod.POST)
    public CommonResult<ComputedOrderPriceResponse> computedPrice(@Validated @RequestBody OrderComputedPriceRequest request) {
        return CommonResult.success(orderService.computedOrderPrice(request));
    }

    @ApiOperation(value = "创建订单")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public CommonResult<Map<String, Object>> createOrder(@Validated @RequestBody CreateOrderRequest orderRequest) {
        return CommonResult.success(orderService.createOrder(orderRequest));
    }

    @ApiOperation(value = "待支付订单列表")
    @RequestMapping(value = "/await/pay/list", method = RequestMethod.GET)
    public CommonResult<PageInfo<MasterOrderAwaitPayResponse>> awaitPayList(@ModelAttribute PageParamRequest pageRequest) {
        return CommonResult.success(orderService.awaitPayList(pageRequest));
    }

    @ApiOperation(value = "主订单详情")
    @RequestMapping(value = "/master/detail/{orderNo}", method = RequestMethod.GET)
    public CommonResult<MasterOrderDetailResponse> masterOrderDetail(@PathVariable String orderNo) {
        return CommonResult.success(orderService.masterOrderDetail(orderNo));
    }

    @ApiOperation(value = "订单列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiImplicitParams ({
        @ApiImplicitParam(name = "type", value = "订单类型|1=待发货,2=待收货,3=已完成", required = true)
    })
    public CommonResult<PageInfo<OrderDetailResponse>> orderList(@RequestParam(name = "type") Integer type,
                                                                 @ModelAttribute PageParamRequest pageRequest) {
        return CommonResult.success(orderService.list(type, pageRequest));
    }

    @ApiOperation(value = "商户订单详情")
    @RequestMapping(value = "/detail/{orderNo}", method = RequestMethod.GET)
    public CommonResult<StoreOrderDetailInfoResponse> orderDetail(@PathVariable String orderNo) {
        return CommonResult.success(orderService.detailOrder(orderNo));
    }

    @ApiOperation(value = "订单数量信息")
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public CommonResult<OrderDataResponse> orderData() {
        return CommonResult.success(orderService.orderData());
    }

    @ApiOperation(value = "个人中心订单数量信息")
    @RequestMapping(value = "/center/data", method = RequestMethod.GET)
    public CommonResult<CenterOrderDataResponse> getCenterOrderData() {
        return CommonResult.success(orderService.getCenterOrderData());
    }

    @ApiOperation(value = "删除已完成订单")
    @RequestMapping(value = "/delete/{orderNo}", method = RequestMethod.POST)
    public CommonResult<Boolean> delete(@PathVariable String orderNo) {
        if( orderService.delete(orderNo)) {
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "订单评价")
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public CommonResult<Boolean> comment(@RequestBody @Validated StoreProductReplyAddRequest request) {
        if(orderService.reply(request)) {
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "订单收货")
    @RequestMapping(value = "/take/{orderNo}", method = RequestMethod.POST)
    public CommonResult<Boolean> take(@PathVariable("orderNo") String orderNo) {
        if(orderService.take(orderNo)) {
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "订单取消")
    @RequestMapping(value = "/cancel/{orderNo}", method = RequestMethod.POST)
    public CommonResult<Boolean> cancel(@PathVariable(value = "orderNo") String orderNo) {
        if(orderService.cancel(orderNo)) {
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "获取申请订单退款信息")
    @RequestMapping(value = "/apply/refund/{orderNo}", method = RequestMethod.GET)
    public CommonResult<ApplyRefundOrderInfoResponse> refundApplyOrder(@PathVariable String orderNo) {
        return CommonResult.success(orderService.applyRefundOrderInfo(orderNo));
    }

    @ApiOperation(value = "订单退款申请")
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public CommonResult<Boolean> refundApply(@RequestBody @Validated OrderRefundApplyRequest request) {
        if(orderService.refundApply(request)) {
            return CommonResult.success();
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "订单退款理由（商家提供）")
    @RequestMapping(value = "/refund/reason", method = RequestMethod.GET)
    public CommonResult<List<String>> refundReason() {
        return CommonResult.success(orderService.getRefundReason());
    }

    @ApiOperation(value = "物流信息查询")
    @RequestMapping(value = "/express/{orderNo}", method = RequestMethod.GET)
    public CommonResult<OrderLogistics> getExpressInfo(@PathVariable String orderNo) {
        return CommonResult.success(orderService.expressOrder(orderNo));
    }

    @ApiOperation(value = "主订单详情(游客订单查询)")
    @RequestMapping(value = "/visitor/master/detail", method = RequestMethod.GET)
    public CommonResult<MasterOrderDetailResponse> visitorMasterOrderDetail(@RequestParam(name = "orderNo") String orderNo,
                                                                            @RequestParam(name = "identity") String identity) {
        return CommonResult.success(orderService.visitorMasterOrderDetail(orderNo, identity));
    }

    @ApiOperation(value = "分订单详情(游客订单查询)")
    @RequestMapping(value = "/visitor/detail", method = RequestMethod.GET)
    public CommonResult<StoreOrderDetailInfoResponse> visitorStoreOrderDetail(@RequestParam(name = "orderNo") String orderNo,
                                                                  @RequestParam(name = "identity") String identity) {
        return CommonResult.success(orderService.visitorStoreOrderDetail(orderNo, identity));
    }

    @ApiOperation(value = "退款订单详情")
    @RequestMapping(value = "/refund/detail/{orderNo}", method = RequestMethod.GET)
    public CommonResult<RefundOrderInfoResponse> refundOrderDetail(@PathVariable String orderNo) {
        return CommonResult.success(orderService.refundOrderDetail(orderNo));
    }

    @ApiOperation(value = "退款订单列表")
    @RequestMapping(value = "/refund/list", method = RequestMethod.GET)
    public CommonResult<PageInfo<RefundOrderResponse>> refundOrderList(@ModelAttribute PageParamRequest pageRequest) {
        return CommonResult.success(orderService.getRefundOrderList(pageRequest));
    }

    @ApiOperation(value = "订单商品评论列表")
    @RequestMapping(value = "/reply/list", method = RequestMethod.GET)
    public CommonResult<PageInfo<InfoReplyResponse>> replyList(@ModelAttribute PageParamRequest pageRequest) {
        return CommonResult.success(orderService.replyList(pageRequest));
    }

}
