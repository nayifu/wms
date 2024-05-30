package com.zbkj.front.service;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.request.MerchantProductSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.ProductRequest;
import com.zbkj.common.response.*;
import com.zbkj.common.vo.ProCategoryCacheVo;

import java.util.List;

/**
* ProductService 接口
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
public interface ProductService {

    /**
     * 商品分类
     * @return List
     */
    List<ProCategoryCacheVo> getCategory();

    /**
     * 商品列表
     * @param request 请求参数
     * @param pageParamRequest 分页参数
     * @return List
     */
    PageInfo<IndexProductResponse> getList(ProductRequest request, PageParamRequest pageParamRequest);

    /**
     * 获取商品详情
     * @param id 商品编号
     * @return 商品详情信息
     */
    ProductDetailResponse getDetail(Integer id);

    /**
     * 获取商品SKU详情
     * @param id 商品编号
     * @return 商品详情信息
     */
    ProductDetailResponse getSkuDetail(Integer id);

    /**
     * 商品评论列表
     * @param proId 商品编号
     * @param type 评价等级|0=全部,1=好评,2=中评,3=差评
     * @param pageParamRequest 分页参数
     * @return List<ProductReplyResponse>
     */
    PageInfo<ProductReplyResponse> getReplyList(Integer proId, Integer type, PageParamRequest pageParamRequest);

    /**
     * 商品评论数量
     * @param id 商品id
     * @return StoreProductReplayCountResponse
     */
    StoreProductReplayCountResponse getReplyCount(Integer id);

    /**
     * 商品详情评论
     * @param id 商品id
     * @return ProductDetailReplyResponse
     */
    ProductDetailReplyResponse getProductReply(Integer id);

    /**
     * 获取商品排行榜
     * @return List
     */
    List<StoreProduct> getLeaderboard();

    /**
     * 商户商品分类列表
     * @param merId 商户id
     * @return List
     */
    List<ProCategoryCacheVo> findMerchantProductCategoryList(Integer merId);

    /**
     * 商户商品列表
     * @param request 搜索参数
     * @param pageParamRequest 分页参数
     * @return List
     */
    PageInfo<IndexProductResponse> getMerchantProList(MerchantProductSearchRequest request, PageParamRequest pageParamRequest);
}
