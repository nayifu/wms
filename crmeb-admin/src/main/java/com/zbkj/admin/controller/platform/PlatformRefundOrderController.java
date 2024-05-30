package com.zbkj.admin.controller.platform;

import com.zbkj.common.annotation.LogControllerAnnotation;
import com.zbkj.common.enums.MethodType;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.PlatformRefundOrderSearchRequest;
import com.zbkj.common.request.RefundOrderRemarkRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.PlatformRefundOrderPageResponse;
import com.zbkj.common.response.RefundOrderCountItemResponse;
import com.zbkj.service.service.StoreRefundOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 平台端退款订单控制器
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
@RequestMapping("api/admin/platform/refund/order")
@Api(tags = "平台端退款订单控制器") //配合swagger使用
public class PlatformRefundOrderController {

    @Autowired
    private StoreRefundOrderService refundOrderService;

    @PreAuthorize("hasAuthority('platform:refund:order:page:list')")
    @ApiOperation(value = "平台端退款订单分页列表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<PlatformRefundOrderPageResponse>> getList(@Validated PlatformRefundOrderSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(refundOrderService.getPlatformAdminPage(request, pageParamRequest)));
    }

    /**
     * 获取订单各状态数量
     */
    @PreAuthorize("hasAuthority('platform:refund:order:status:num')")
    @ApiOperation(value = "平台端获取退款订单各状态数量")
    @RequestMapping(value = "/status/num", method = RequestMethod.GET)
    public CommonResult<RefundOrderCountItemResponse> getOrderStatusNum(@RequestParam(value = "dateLimit", defaultValue = "") String dateLimit) {
        return CommonResult.success(refundOrderService.getPlatformOrderStatusNum(dateLimit));
    }


    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "平台备注退款订单")
    @PreAuthorize("hasAuthority('platform:refund:order:mark')")
    @ApiOperation(value = "平台备注退款订单")
    @RequestMapping(value = "/mark", method = RequestMethod.POST)
    public CommonResult<String> mark(@RequestBody @Validated RefundOrderRemarkRequest request) {
        if (refundOrderService.platformMark(request)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

}



