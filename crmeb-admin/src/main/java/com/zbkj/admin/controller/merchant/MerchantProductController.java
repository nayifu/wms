package com.zbkj.admin.controller.merchant;

import com.zbkj.common.annotation.LogControllerAnnotation;
import com.zbkj.common.enums.MethodType;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.AdminProductListResponse;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.StoreProductInfoResponse;
import com.zbkj.common.response.StoreProductTabsHeader;
import com.zbkj.service.service.StoreCartService;
import com.zbkj.service.service.StoreProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 商户端商品控制器
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
@RequestMapping("api/admin/merchant/product")
@Api(tags = "商户端商品控制器") //配合swagger使用
public class MerchantProductController {

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private StoreCartService storeCartService;

    /**
     * 分页显示商品表
     *
     * @param request          搜索条件
     * @param pageParamRequest 分页参数
     */
    @PreAuthorize("hasAuthority('merchant:product:page:list')")
    @ApiOperation(value = "分页列表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<AdminProductListResponse>> getList(@Validated StoreProductSearchRequest request,
                                                                      @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(storeProductService.getAdminList(request, pageParamRequest)));
    }

    /**
     * 新增商品
     *
     * @param request 新增参数
     */
    @LogControllerAnnotation(intoDB = true, methodType = MethodType.ADD, description = "新增商品")
    @PreAuthorize("hasAuthority('merchant:product:save')")
    @ApiOperation(value = "新增商品")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<String> save(@RequestBody @Validated StoreProductAddRequest request) {
        if (storeProductService.save(request)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 删除商品表
     *
     * @param id Integer
     */
    @LogControllerAnnotation(intoDB = true, methodType = MethodType.DELETE, description = "删除商品")
    @PreAuthorize("hasAuthority('merchant:product:delete')")
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public CommonResult<String> delete(@RequestBody @PathVariable Integer id,
                                       @RequestParam(value = "type", required = false, defaultValue = "recycle") String type) {
        if (storeProductService.deleteProduct(id, type)) {
            if (type.equals("recycle")) {
                storeCartService.productStatusNotEnable(id);
            } else {
                storeCartService.productDelete(id);
            }
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 恢复已删除商品表
     *
     * @param id Integer
     */
    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "恢复回收站商品")
    @PreAuthorize("hasAuthority('merchant:product:restore')")
    @ApiOperation(value = "恢复商品")
    @RequestMapping(value = "/restore/{id}", method = RequestMethod.GET)
    public CommonResult<String> restore(@RequestBody @PathVariable Integer id) {
        if (storeProductService.reStoreProduct(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 商品修改
     *
     * @param storeProductRequest 商品参数
     */
    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "修改商品")
    @PreAuthorize("hasAuthority('merchant:product:update')")
    @ApiOperation(value = "商品修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated StoreProductAddRequest storeProductRequest) {
        if (storeProductService.update(storeProductRequest)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 商品详情
     *
     * @param id 商品id
     */
    @PreAuthorize("hasAuthority('merchant:product:info')")
    @ApiOperation(value = "商品详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public CommonResult<StoreProductInfoResponse> info(@PathVariable Integer id) {
        return CommonResult.success(storeProductService.getInfo(id));
    }

    /**
     * 商品tabs表头数据
     */
    @PreAuthorize("hasAuthority('merchant:product:tabs:headers')")
    @ApiOperation(value = "商品表头数量")
    @RequestMapping(value = "/tabs/headers", method = RequestMethod.GET)
    public CommonResult<List<StoreProductTabsHeader>> getTabsHeader() {
        return CommonResult.success(storeProductService.getTabsHeader());
    }


    /**
     * 上架
     */
    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "上架商品")
    @PreAuthorize("hasAuthority('merchant:product:up')")
    @ApiOperation(value = "上架")
    @RequestMapping(value = "/putOnShell/{id}", method = RequestMethod.GET)
    public CommonResult<String> putOn(@PathVariable Integer id) {
        if (storeProductService.putOnShelf(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 下架
     */
    @LogControllerAnnotation(intoDB = true, methodType = MethodType.UPDATE, description = "下架商品")
    @PreAuthorize("hasAuthority('merchant:product:down')")
    @ApiOperation(value = "下架")
    @RequestMapping(value = "/offShell/{id}", method = RequestMethod.GET)
    public CommonResult<String> offShell(@PathVariable Integer id) {
        if (storeProductService.offShelf(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @PreAuthorize("hasAuthority('merchant:product:import:product')")
    @ApiOperation(value = "导入99Api商品")
    @RequestMapping(value = "/import/product", method = RequestMethod.POST)
    public CommonResult<StoreProductRequest> importProduct(@RequestBody @Validated ImportProductRequest request) {
        return CommonResult.success(storeProductService.importProductFrom99Api(request.getUrl(), request.getForm()));
    }
}



