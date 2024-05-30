package com.zbkj.admin.controller.merchant;

import com.zbkj.common.annotation.LogControllerAnnotation;
import com.zbkj.common.enums.MethodType;
import com.zbkj.common.model.order.OrderLogistics;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.OrderRemarkRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreOrderSearchRequest;
import com.zbkj.common.request.StoreOrderSendRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.MerchantOrderPageResponse;
import com.zbkj.common.response.StoreOrderCountItemResponse;
import com.zbkj.common.response.StoreOrderInfoResponse;
import com.zbkj.service.service.StoreOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 订单表 前端控制器
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
@Slf4j
@RestController
@RequestMapping("api/admin/merchant/order")
@Api(tags = "商户侧订单控制器") //配合swagger使用
public class StoreOrderController {

    @Autowired
    private StoreOrderService storeOrderService;

    @PreAuthorize("hasAuthority('merchant:order:page:list')")
    @ApiOperation(value = "商户端订单分页列表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MerchantOrderPageResponse>> getList(@Validated StoreOrderSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(storeOrderService.getMerchantAdminPage(request, pageParamRequest)));
    }

    /**
     * 获取订单各状态数量
     */
    @PreAuthorize("hasAuthority('merchant:order:status:num')")
    @ApiOperation(value = "获取订单各状态数量")
    @RequestMapping(value = "/status/num", method = RequestMethod.GET)
    public CommonResult<StoreOrderCountItemResponse> getOrderStatusNum(@RequestParam(value = "dateLimit", defaultValue = "") String dateLimit) {
        return CommonResult.success(storeOrderService.getMerchantOrderStatusNum(dateLimit));
    }

//    /**
//     * 获取订单统计数据
//     */
//    @PreAuthorize("hasAuthority('admin:order:list:data')")
//    @ApiOperation(value = "获取订单统计数据")
//    @RequestMapping(value = "/list/data", method = RequestMethod.GET)
//    public CommonResult<StoreOrderTopItemResponse> getOrderData(@RequestParam(value = "dateLimit", defaultValue = "")String dateLimit) {
//        return CommonResult.success(storeOrderService.getOrderData(dateLimit));
//    }

    @LogControllerAnnotation(intoDB = true, methodType = MethodType.DELETE, description = "删除订单")
    @PreAuthorize("hasAuthority('merchant:order:delete')")
    @ApiOperation(value = "订单删除")
    @RequestMapping(value = "/delete/{orderNo}", method = RequestMethod.POST)
    public CommonResult<String> delete(@PathVariable(name = "orderNo") String orderNo) {
        if (storeOrderService.delete(orderNo)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "商户备注订单")
    @PreAuthorize("hasAuthority('merchant:order:mark')")
    @ApiOperation(value = "商户备注订单")
    @RequestMapping(value = "/mark", method = RequestMethod.POST)
    public CommonResult<String> mark(@RequestBody @Validated OrderRemarkRequest request) {
        if (storeOrderService.mark(request)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @PreAuthorize("hasAuthority('merchant:order:info')")
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<StoreOrderInfoResponse> info(@RequestParam(value = "orderNo") String orderNo) {
        return CommonResult.success(storeOrderService.info(orderNo));
    }

    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "订单发货")
    @PreAuthorize("hasAuthority('merchant:order:send')")
    @ApiOperation(value = "发货")
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public CommonResult<Boolean> send(@RequestBody @Validated StoreOrderSendRequest request) {
        if (storeOrderService.send(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('merchant:order:logistics:info')")
    @ApiOperation(value = "订单物流详情")
    @RequestMapping(value = "/getLogisticsInfo", method = RequestMethod.GET)
    public CommonResult<OrderLogistics> getLogisticsInfo(@RequestParam(value = "orderNo") String orderNo) {
        return CommonResult.success(storeOrderService.getLogisticsInfo(orderNo));
    }

}



