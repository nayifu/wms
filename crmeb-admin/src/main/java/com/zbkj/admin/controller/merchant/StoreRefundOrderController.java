package com.zbkj.admin.controller.merchant;

import com.zbkj.common.annotation.LogControllerAnnotation;
import com.zbkj.common.enums.MethodType;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.RefundOrderRemarkRequest;
import com.zbkj.common.request.RefundOrderSearchRequest;
import com.zbkj.common.request.StoreOrderRefundRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.MerchantRefundOrderPageResponse;
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
 * 商户侧退款订单控制器
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
@RequestMapping("api/admin/merchant/refund/order")
@Api(tags = "商户侧退款订单控制器") //配合swagger使用
public class StoreRefundOrderController {

    @Autowired
    private StoreRefundOrderService refundOrderService;

    @PreAuthorize("hasAuthority('merchant:refund:order:page:list')")
    @ApiOperation(value = "商户端退款订单分页列表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MerchantRefundOrderPageResponse>> getList(@Validated RefundOrderSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(refundOrderService.getMerchantAdminPage(request, pageParamRequest)));
    }

    /**
     * 获取订单各状态数量
     */
    @PreAuthorize("hasAuthority('merchant:refund:order:status:num')")
    @ApiOperation(value = "商户端获取退款订单各状态数量")
    @RequestMapping(value = "/status/num", method = RequestMethod.GET)
    public CommonResult<RefundOrderCountItemResponse> getOrderStatusNum(@RequestParam(value = "dateLimit", defaultValue = "") String dateLimit) {
        return CommonResult.success(refundOrderService.getMerchantOrderStatusNum(dateLimit));
    }


    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "商户备注退款订单")
    @PreAuthorize("hasAuthority('merchant:refund:order:mark')")
    @ApiOperation(value = "商户备注退款订单")
    @RequestMapping(value = "/mark", method = RequestMethod.POST)
    public CommonResult<String> mark(@RequestBody @Validated RefundOrderRemarkRequest request) {
        if (refundOrderService.mark(request)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "订单退款")
    @PreAuthorize("hasAuthority('merchant:refund:order:refund')")
    @ApiOperation(value = "订单退款")
    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    public CommonResult<Boolean> send(@Validated StoreOrderRefundRequest request) {
        return CommonResult.success(refundOrderService.refund(request));
    }

    /**
     * 拒绝退款
     */
    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "订单拒绝退款")
    @PreAuthorize("hasAuthority('merchant:refund:order:refund:refuse')")
    @ApiOperation(value = "拒绝退款")
    @RequestMapping(value = "/refund/refuse", method = RequestMethod.GET)
    public CommonResult<Object> refundRefuse(@Validated StoreOrderRefundRequest request) {
        if (refundOrderService.refundRefuse(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

}



