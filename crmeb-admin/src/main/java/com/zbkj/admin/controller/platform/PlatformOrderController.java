package com.zbkj.admin.controller.platform;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.annotation.LogControllerAnnotation;
import com.zbkj.common.enums.MethodType;
import com.zbkj.common.model.order.OrderLogistics;
import com.zbkj.common.request.OrderRemarkRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.PlatformOrderSearchRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.PlatformOrderInfoResponse;
import com.zbkj.common.response.PlatformOrderPageResponse;
import com.zbkj.common.response.StoreOrderCountItemResponse;
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
@RequestMapping("api/admin/platform/order")
@Api(tags = "平台端订单控制器") //配合swagger使用
public class PlatformOrderController {

    @Autowired
    private StoreOrderService storeOrderService;

    @PreAuthorize("hasAuthority('platform:order:page:list')")
    @ApiOperation(value = "平台端订单分页列表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<PageInfo<PlatformOrderPageResponse>> getList(@Validated PlatformOrderSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(storeOrderService.getPlatformAdminPage(request, pageParamRequest));
    }

    /**
     * 获取订单各状态数量
     */
    @PreAuthorize("hasAuthority('platform:order:status:num')")
    @ApiOperation(value = "平台端获取订单各状态数量")
    @RequestMapping(value = "/status/num", method = RequestMethod.GET)
    public CommonResult<StoreOrderCountItemResponse> getOrderStatusNum(@RequestParam(value = "dateLimit", defaultValue = "") String dateLimit) {
        return CommonResult.success(storeOrderService.getPlatformOrderStatusNum(dateLimit));
    }

    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "备注订单")
    @PreAuthorize("hasAuthority('platform:order:mark')")
    @ApiOperation(value = "平台端备注")
    @RequestMapping(value = "/mark", method = RequestMethod.POST)
    public CommonResult<String> mark(@RequestBody @Validated OrderRemarkRequest request) {
        if (storeOrderService.platformMark(request)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @PreAuthorize("hasAuthority('platform:order:info')")
    @ApiOperation(value = "平台端订单详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<PlatformOrderInfoResponse> info(@RequestParam(value = "orderNo") String orderNo) {
        return CommonResult.success(storeOrderService.platformInfo(orderNo));
    }

    @PreAuthorize("hasAuthority('platform:order:logistics:info')")
    @ApiOperation(value = "平台端订单物流详情")
    @RequestMapping(value = "/getLogisticsInfo", method = RequestMethod.GET)
    public CommonResult<OrderLogistics> getLogisticsInfo(@RequestParam(value = "orderNo") String orderNo) {
        return CommonResult.success(storeOrderService.getLogisticsInfo(orderNo));
    }
}



