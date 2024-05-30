package com.zbkj.admin.controller.merchant;

import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreCouponUserSearchRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.StoreCouponUserResponse;
import com.zbkj.service.service.StoreCouponUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 优惠券发放记录表 前端控制器
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
@RequestMapping("api/admin/merchant/coupon/user")
@Api(tags = "商户端用户优惠券控制器")
public class StoreCouponUserController {

    @Autowired
    private StoreCouponUserService storeCouponUserService;

    @PreAuthorize("hasAuthority('merchant:coupon:user:page:list')")
    @ApiOperation(value = "优惠券领取记录分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<StoreCouponUserResponse>> getList(@Validated StoreCouponUserSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        storeCouponUserService.getPageList(request, pageParamRequest);
        CommonPage<StoreCouponUserResponse> storeCouponUserCommonPage = CommonPage.restPage(storeCouponUserService.getPageList(request, pageParamRequest));
        return CommonResult.success(storeCouponUserCommonPage);
    }

}



