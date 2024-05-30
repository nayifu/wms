package com.zbkj.front.controller;

import com.zbkj.common.request.*;
import com.zbkj.common.response.CartMerchantResponse;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.StoreCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 购物车 前端控制器
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
@RequestMapping("api/front/cart")
@Api(tags = "商品 -- 购物车") //配合swagger使用
public class CartController {

    @Autowired
    private StoreCartService storeCartService;

    @ApiOperation(value = "购物车列表") //配合swagger使用
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name="isValid", value="类型，true-有效商品，false-无效商品", required = true),
            @ApiImplicitParam(name="page", value="页码", required = true),
            @ApiImplicitParam(name="limit", value="每页数量", required = true)
    })
    public CommonResult<List<CartMerchantResponse>> getList(@RequestParam Boolean isValid) {
        return CommonResult.success(storeCartService.getList(isValid));
    }

    /**
     * 新增购物车表
     * @param storeCartListRequest 新增参数
     */
    @ApiOperation(value = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<Object> save(@RequestBody @Validated List<CartRequest> storeCartListRequest) {
        if (storeCartService.saveCate(storeCartListRequest)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 删除购物车
     */
    @ApiOperation(value = "删除购物车")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult<String> delete(@RequestBody @Validated CartDeleteRequest request) {
        if (storeCartService.deleteCartByIds(request.getIds())) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 修改购物车商品数量
     */
    @ApiOperation(value = "修改购物车商品数量")
    @RequestMapping(value = "/num", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated CartUpdateNumRequest request) {
        if (storeCartService.updateCartNum(request.getId(), request.getNumber())) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 获取购物车数量
     */
    @ApiOperation(value = "获取购物车数量")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public CommonResult<Map<String, Integer>> count(@Validated CartNumRequest request) {
        return CommonResult.success(storeCartService.getUserCount(request));
    }

    /**
     * 购物车重选提交
     * @param resetRequest 重选参数
     * @return 结果
     */
    @ApiOperation(value = "购物车重选提交")
    @RequestMapping(value = "/resetcart", method = RequestMethod.POST)
    public CommonResult<Object> resetCart(@RequestBody @Validated CartResetRequest resetRequest){
        return CommonResult.success(storeCartService.resetCart(resetRequest));
    }
}



