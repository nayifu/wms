package com.zbkj.admin.controller.merchant;

import com.zbkj.common.model.coupon.StoreCoupon;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreCouponRequest;
import com.zbkj.common.request.StoreCouponSearchRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.StoreCouponInfoResponse;
import com.zbkj.service.service.StoreCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 优惠券表 前端控制器
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
@RestController
@RequestMapping("api/admin/merchant/coupon")
@Api(tags = "商户端优惠券管理器")
public class StoreCouponController {

    @Autowired
    private StoreCouponService storeCouponService;

    @PreAuthorize("hasAuthority('merchant:coupon:page:list')")
    @ApiOperation(value = "优惠券分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<StoreCoupon>>  getList(@Validated StoreCouponSearchRequest request,
                                                          @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(storeCouponService.getMerchantPageList(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('merchant:coupon:save')")
    @ApiOperation(value = "新增优惠券")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<String> save(@RequestBody @Validated StoreCouponRequest request) {
        if (storeCouponService.create(request)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @PreAuthorize("hasAuthority('merchant:coupon:update:status')")
    @ApiOperation(value = "修改优惠券状态")
    @RequestMapping(value = "/update/status/{id}", method = RequestMethod.POST)
    public CommonResult<String> updateStatus(@PathVariable Integer id) {
        if (storeCouponService.updateStatus(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @PreAuthorize("hasAuthority('merchant:coupon:info')")
    @ApiOperation(value = "优惠券详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiImplicitParam(name="id", value="优惠券ID", required = true)
    public CommonResult<StoreCouponInfoResponse> info(@PathVariable Integer id) {
        return CommonResult.success(storeCouponService.info(id));
    }

    @PreAuthorize("hasAuthority('merchant:coupon:product:usable:list')")
    @ApiOperation(value = "商品可用优惠券列表")
    @RequestMapping(value = "/product/usable/list", method = RequestMethod.GET)
    public CommonResult<List<StoreCoupon>> getProductUsableList() {
        return CommonResult.success(storeCouponService.getProductUsableList());
    }

    /**
     * 删除优惠券
     * @param id 优惠券id
     */
    @PreAuthorize("hasAuthority('merchant:coupon:delete')")
    @ApiOperation(value = "删除优惠券")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<StoreCouponInfoResponse> delete(@PathVariable Integer id) {
        if (storeCouponService.delete(id)) {
            return CommonResult.success("删除成功");
        } else {
            return CommonResult.failed("删除失败");
        }
    }
}



