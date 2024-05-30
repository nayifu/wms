package com.zbkj.front.controller;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.request.CouponSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.StoreCouponFrontResponse;
import com.zbkj.common.response.StoreCouponUserOrder;
import com.zbkj.service.service.StoreCouponService;
import com.zbkj.service.service.StoreCouponUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController("CouponFrontController")
@RequestMapping("api/front")
@Api(tags = "优惠券")
public class CouponController {

    @Autowired
    private StoreCouponService storeCouponService;

    @Autowired
    private StoreCouponUserService storeCouponUserService;


    /**
     * 分页显示优惠券表
     */
    @ApiOperation(value = "分页列表")
    @RequestMapping(value = "/coupons", method = RequestMethod.GET)
    public CommonResult<PageInfo<StoreCouponFrontResponse>> getList(@ModelAttribute @Validated CouponSearchRequest request,
                                                                    @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(storeCouponService.getH5List(request, pageParamRequest));
    }

    @ApiOperation(value = "当前订单可用优惠券")
    @RequestMapping(value = "/coupons/order/{preOrderNo}", method = RequestMethod.GET)
    public CommonResult<List<StoreCouponUserOrder>> getCouponsListByPreOrderNo(@PathVariable String preOrderNo) {
        return CommonResult.success(storeCouponUserService.getListByPreOrderNo(preOrderNo));
    }
}



