package com.zbkj.admin.controller.platform;

import com.zbkj.common.annotation.LogControllerAnnotation;
import com.zbkj.common.enums.MethodType;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.PlatformProductListResponse;
import com.zbkj.common.response.StoreProductInfoResponse;
import com.zbkj.common.response.StoreProductTabsHeader;
import com.zbkj.service.service.StoreProductService;
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
 * 平台端商品控制器
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
@RequestMapping("api/admin/platform/product")
@Api(tags = "平台端商品控制器") //配合swagger使用
public class ProductController {

    @Autowired
    private StoreProductService storeProductService;

    @PreAuthorize("hasAuthority('platform:product:page:list')")
    @ApiOperation(value = "商品分页列表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<PlatformProductListResponse>> getList(@Validated ProductSearchRequest request,
                                                                         @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(storeProductService.getPlatformPageList(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:product:tabs:headers')")
    @ApiOperation(value = "商品表头数量")
    @RequestMapping(value = "/tabs/headers", method = RequestMethod.GET)
    public CommonResult<List<StoreProductTabsHeader>> getTabsHeader() {
        return CommonResult.success(storeProductService.getPlatformTabsHeader());
    }

    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "商品审核")
    @PreAuthorize("hasAuthority('platform:product:audit')")
    @ApiOperation(value = "商品审核")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public CommonResult<String> audit(@RequestBody @Validated StoreProductAuditRequest request) {
        if (storeProductService.audit(request)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "强制下架商品")
    @PreAuthorize("hasAuthority('platform:product:force:down')")
    @ApiOperation(value = "强制下架商品")
    @RequestMapping(value = "/force/down", method = RequestMethod.POST)
    public CommonResult<String> forceDown(@RequestBody @Validated ProductForceDownRequest request) {
        if (storeProductService.forceDown(request)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "虚拟销量")
    @PreAuthorize("hasAuthority('platform:product:virtual:sales')")
    @ApiOperation(value = "虚拟销量")
    @RequestMapping(value = "/virtual/sales", method = RequestMethod.POST)
    public CommonResult<String> virtualSales(@RequestBody @Validated ProductVirtualSalesRequest request) {
        if (storeProductService.updateVirtualSales(request)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 商品详情
     * @param id 商品id
     */
    @PreAuthorize("hasAuthority('platform:product:info')")
    @ApiOperation(value = "商品详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public CommonResult<StoreProductInfoResponse> info(@PathVariable Integer id) {
        return CommonResult.success(storeProductService.getInfo(id));
   }

    @PreAuthorize("hasAuthority('platform:product:update:rank')")
    @ApiOperation(value = "修改商品后台排序")
    @RequestMapping(value = "/update/rank", method = RequestMethod.POST)
    @ApiImplicitParam(name="rank", value="商品后台排序0-9999", required = true)
    public CommonResult<String> updateRank(@RequestBody @Validated UpdateRankRequest request) {
        if (storeProductService.updateRank(request.getId(), request.getRank())) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}



