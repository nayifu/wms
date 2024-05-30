package com.zbkj.front.controller;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.merchant.MerchantCategory;
import com.zbkj.common.model.merchant.MerchantType;
import com.zbkj.common.request.MerchantMoveSearchRequest;
import com.zbkj.common.request.MerchantSettledApplyRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.*;
import com.zbkj.service.service.MerchantCategoryService;
import com.zbkj.service.service.MerchantService;
import com.zbkj.service.service.MerchantTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商户 前端控制器
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
@RequestMapping("api/front/merchant")
@Api(tags = "商户控制器")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantTypeService typeService;
    @Autowired
    private MerchantCategoryService categoryService;

    @ApiOperation(value = "商户入驻申请")
    @RequestMapping(value = "/settled/apply", method = RequestMethod.POST)
    public CommonResult<String> settledApply(@RequestBody @Validated MerchantSettledApplyRequest request) {
        if (merchantService.settledApply(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "商户入驻记录")
    @RequestMapping(value = "/settled/record", method = RequestMethod.GET)
    public CommonResult<PageInfo<MerchantSettledResponse>> settledRecord(@ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(merchantService.findSettledRecord(pageParamRequest));
    }

    @ApiOperation(value = "商户搜索列表")
    @RequestMapping(value = "/search/list", method = RequestMethod.GET)
    public CommonResult<PageInfo<MerchantSearchResponse>> searchList(MerchantMoveSearchRequest request, @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(merchantService.findSearchList(request, pageParamRequest));
    }

    @ApiOperation(value = "店铺街")
    @RequestMapping(value = "/street", method = RequestMethod.GET)
    public CommonResult<PageInfo<MerchantSearchResponse>> getStreet(@ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(merchantService.getStreet(pageParamRequest));
    }

    @ApiOperation(value = "店铺首页信息")
    @RequestMapping(value = "/index/info/{id}", method = RequestMethod.GET)
    public CommonResult<MerchantIndexInfoResponse> getIndexInfo(@PathVariable Integer id) {
        return CommonResult.success(merchantService.getIndexInfo(id));
    }

    @ApiOperation(value = "店铺详细信息")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public CommonResult<MerchantDetailResponse> getDetail(@PathVariable Integer id) {
        return CommonResult.success(merchantService.getDetail(id));
    }

    @ApiOperation(value="获取全部商户类型列表")
    @RequestMapping(value = "/all/type/list", method = RequestMethod.GET)
    public CommonResult<List<MerchantType>> allTypeList() {
        return CommonResult.success(typeService.allList());
    }

    @ApiOperation(value="获取全部商户分类列表")
    @RequestMapping(value = "/all/category/list", method = RequestMethod.GET)
    public CommonResult<List<MerchantCategory>> allCategoryList() {
        return CommonResult.success(categoryService.allList());
    }

    @ApiOperation(value="获取入驻协议")
    @RequestMapping(value = "/settled/agreement", method = RequestMethod.GET)
    public CommonResult<MerchantAgreementResponse> getSettledAgreement() {
        return CommonResult.success(merchantService.getSettledAgreement());
    }

    @ApiOperation(value="获取商户客服信息")
    @RequestMapping(value = "/customer/service/info/{id}", method = RequestMethod.GET)
    public CommonResult<MerchantServiceInfoResponse> getCustomerServiceInfo(@PathVariable("id") Integer id) {
        return CommonResult.success(merchantService.getCustomerServiceInfo(id));
    }
}