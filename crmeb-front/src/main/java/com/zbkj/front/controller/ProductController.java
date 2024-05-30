package com.zbkj.front.controller;


import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.request.MerchantProductSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.ProductRequest;
import com.zbkj.common.response.*;
import com.zbkj.common.vo.ProCategoryCacheVo;
import com.zbkj.front.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 -- 用户中心
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
@RestController("ProductController")
@RequestMapping("api/front/product")
@Api(tags = "商品")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "获取商品分类")
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public CommonResult<List<ProCategoryCacheVo>> getCategory() {
        return CommonResult.success(productService.getCategory());
    }

    /**
     * 商品列表
     */
    @ApiOperation(value = "商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<PageInfo<IndexProductResponse>> getList(@Validated ProductRequest request, @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(productService.getList(request, pageParamRequest));
    }

    /**
     * 商品详情
     */
    @ApiOperation(value = "商品详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public CommonResult<ProductDetailResponse> getDetail(@PathVariable Integer id) {
        return CommonResult.success(productService.getDetail(id));
    }

    /**
     * 商品评论列表
     */
    @ApiOperation(value = "商品评论列表")
    @RequestMapping(value = "/reply/list/{id}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "type", value = "评价等级|0=全部,1=好评,2=中评,3=差评", allowableValues = "range[0,1,2,3]")
    public CommonResult<PageInfo<ProductReplyResponse>> getReplyList(@PathVariable Integer id,
                                                                     @RequestParam(value = "type") Integer type, @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(productService.getReplyList(id, type, pageParamRequest));
    }

    /**
     * 商品评论数量
     */
    @ApiOperation(value = "商品评论数量")
    @RequestMapping(value = "/reply/config/{id}", method = RequestMethod.GET)
    public CommonResult<StoreProductReplayCountResponse> getReplyCount(@PathVariable Integer id) {
        return CommonResult.success(productService.getReplyCount(id));
    }

    /**
     * 商品详情评论
     */
    @ApiOperation(value = "商品详情评论")
    @RequestMapping(value = "/reply/detail/{id}", method = RequestMethod.GET)
    public CommonResult<ProductDetailReplyResponse> getProductReply(@PathVariable Integer id) {
        return CommonResult.success(productService.getProductReply(id));
    }

    /**
     * 商品规格详情
     */
    @ApiOperation(value = "商品规格详情")
    @RequestMapping(value = "/sku/detail/{id}", method = RequestMethod.GET)
    public CommonResult<ProductDetailResponse> getSkuDetail(@PathVariable Integer id) {
        return CommonResult.success(productService.getSkuDetail(id));
    }

    /**
     * 商品排行榜
     */
    @ApiOperation(value = "商品排行榜")
    @RequestMapping(value = "/leaderboard", method = RequestMethod.GET)
    public CommonResult<List<StoreProduct>> getLeaderboard() {
        return CommonResult.success(productService.getLeaderboard());
    }

    @ApiOperation(value = "商户商品分类列表")
    @RequestMapping(value = "/merchant/{id}/category/list", method = RequestMethod.GET)
    public CommonResult<List<ProCategoryCacheVo>> findMerchantProductCategoryList(@PathVariable Integer id) {
        return CommonResult.success(productService.findMerchantProductCategoryList(id));
    }

    /**
     * 商品列表
     */
    @ApiOperation(value = "商户商品列表")
    @RequestMapping(value = "/merchant/pro/list", method = RequestMethod.GET)
    public CommonResult<PageInfo<IndexProductResponse>> getMerchantProList(@Validated MerchantProductSearchRequest request, @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(productService.getMerchantProList(request, pageParamRequest));
    }
}



