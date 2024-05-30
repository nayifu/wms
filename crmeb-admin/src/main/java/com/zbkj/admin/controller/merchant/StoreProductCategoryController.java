package com.zbkj.admin.controller.merchant;

import com.zbkj.common.model.product.StoreProductCategory;
import com.zbkj.common.request.StoreProductCategoryRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.vo.ProCategoryCacheVo;
import com.zbkj.service.service.StoreProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 商户端商户商品分类控制器
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
@RequestMapping("api/admin/merchant/store/product/category")
@Api(tags = "商户端商户商品分类控制器")
public class StoreProductCategoryController {

    @Autowired
    private StoreProductCategoryService categoryService;


    @PreAuthorize("hasAuthority('merchant:store:product:category:list')")
    @ApiOperation(value = "分类列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<StoreProductCategory>> getList() {
        return CommonResult.success(categoryService.getAdminList());
    }

    @PreAuthorize("hasAuthority('merchant:store:product:category:add')")
    @ApiOperation(value = "新增分类")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<String> add(@RequestBody @Validated StoreProductCategoryRequest request) {
        if (categoryService.add(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('merchant:store:product:category:delete')")
    @ApiOperation(value = "删除分类")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<String> delete(@PathVariable(value = "id") Integer id) {
        if (categoryService.delete(id)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('merchant:store:product:category:update')")
    @ApiOperation(value = "修改分类")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated StoreProductCategoryRequest request) {
        if (categoryService.edit(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('merchant:store:product:category:show:status')")
    @ApiOperation(value = "修改分类显示状态")
    @RequestMapping(value = "/update/show/{id}", method = RequestMethod.POST)
    public CommonResult<Object> updateShowStatus(@PathVariable(value = "id") Integer id) {
        if (categoryService.updateShowStatus(id)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    @PreAuthorize("hasAuthority('merchant:store:product:category:cache:tree')")
    @ApiOperation(value = "分类缓存树")
    @RequestMapping(value = "/cache/tree", method = RequestMethod.GET)
    public CommonResult<List<ProCategoryCacheVo>> getCacheTree() {
        return CommonResult.success(categoryService.getCacheTree());
    }
}



