package com.zbkj.admin.controller.merchant;

import com.zbkj.common.model.product.StoreProductRule;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreProductRuleRequest;
import com.zbkj.common.request.StoreProductRuleSearchRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.StoreProductRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 商品规则值(规格)控制器
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
@RequestMapping("api/admin/merchant/product/rule")
@Api(tags = "商户端商品规则值(规格)控制器") //配合swagger使用
public class StoreProductRuleController {

    @Autowired
    private StoreProductRuleService storeProductRuleService;

    @PreAuthorize("hasAuthority('merchant:product:rule:page:list')")
    @ApiOperation(value = "商户端商品规则值(规格)分页列表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<StoreProductRule>>  getList(@Validated StoreProductRuleSearchRequest request,
                                                               @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(storeProductRuleService.getList(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('merchant:product:rule:save')")
    @ApiOperation(value = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<String> save(@RequestBody @Validated StoreProductRuleRequest storeProductRuleRequest) {
        if (storeProductRuleService.save(storeProductRuleRequest)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @PreAuthorize("hasAuthority('merchant:product:rule:delete')")
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<String> delete(@PathVariable Integer id) {
        if (storeProductRuleService.removeById(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @PreAuthorize("hasAuthority('merchant:product:rule:update')")
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated StoreProductRuleRequest storeProductRuleRequest) {
        if (storeProductRuleService.updateRule(storeProductRuleRequest)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @PreAuthorize("hasAuthority('merchant:product:rule:info')")
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public CommonResult<StoreProductRule> info(@PathVariable Integer id) {
        StoreProductRule storeProductRule = storeProductRuleService.getById(id);
        return CommonResult.success(storeProductRule);
   }
}



