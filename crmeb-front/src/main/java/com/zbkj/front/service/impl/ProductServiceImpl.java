package com.zbkj.front.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.ProductConstants;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.model.product.StoreProductAttr;
import com.zbkj.common.model.product.StoreProductAttrValue;
import com.zbkj.common.model.user.User;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.MerchantProductSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.ProductRequest;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.vo.MyRecord;
import com.zbkj.common.vo.ProCategoryCacheVo;
import com.zbkj.front.service.AsyncService;
import com.zbkj.front.service.ProductService;
import com.zbkj.service.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* IndexServiceImpl 接口实现
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
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private StoreProductReplyService storeProductReplyService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreProductRelationService storeProductRelationService;
    @Autowired
    private StoreProductAttrService attrService;
    @Autowired
    private StoreProductAttrValueService storeProductAttrValueService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private StoreProductCategoryService storeProductCategoryService;
    @Autowired
    private UserMerchantCollectService userMerchantCollectService;
    @Autowired
    private ProductGuaranteeService productGuaranteeService;

    /**
     * 获取商品分类
     * @return List<ProCategoryCacheVo>
     */
    @Override
    public List<ProCategoryCacheVo> getCategory() {
        return productCategoryService.getMerchantCacheTree();
    }

    /**
     * 商品列表
     * @return List<IndexProductResponse>
     */
    @Override
    public PageInfo<IndexProductResponse> getList(ProductRequest request, PageParamRequest pageRequest) {
        PageInfo<StoreProduct> pageInfo = storeProductService.findH5List(request, pageRequest);
        List<StoreProduct> storeProductList = pageInfo.getList();
        if (CollUtil.isEmpty(storeProductList)) {
            return CommonPage.copyPageInfo(pageInfo, CollUtil.newArrayList());
        }
        return CommonPage.copyPageInfo(pageInfo, productToIndexProduct(storeProductList));
    }

    /**
     * 获取商品详情
     * @param id 商品编号
     * @return 商品详情信息
     */
    @Override
    public ProductDetailResponse getDetail(Integer id) {
        ProductDetailResponse productDetailResponse = new ProductDetailResponse();
        // 查询商品
        StoreProduct storeProduct = storeProductService.getH5Detail(id);
        productDetailResponse.setProductInfo(storeProduct);
        if (StrUtil.isNotBlank(storeProduct.getGuaranteeIds())) {
            productDetailResponse.setGuaranteeList(productGuaranteeService.findByIdList(CrmebUtil.stringToArray(storeProduct.getGuaranteeIds())));
        }
        // 获取商品规格
        List<StoreProductAttr> attrList = attrService.getListByProductIdAndType(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        // 根据制式设置attr属性
        productDetailResponse.setProductAttr(attrList);
        // 根据制式设置sku属性
        HashMap<String, Object> skuMap = CollUtil.newHashMap();
        List<StoreProductAttrValue> storeProductAttrValues = storeProductAttrValueService.getListByProductIdAndType(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        for (StoreProductAttrValue storeProductAttrValue : storeProductAttrValues) {
            StoreProductAttrValueResponse atr = new StoreProductAttrValueResponse();
            BeanUtils.copyProperties(storeProductAttrValue, atr);
            skuMap.put(atr.getSku(), atr);
        }
        productDetailResponse.setProductValue(skuMap);

        // 获取商户信息
        Merchant merchant = merchantService.getById(storeProduct.getMerId());
        ProductMerchantResponse merchantResponse = new ProductMerchantResponse();
        BeanUtils.copyProperties(merchant, merchantResponse);
        // 获取商户推荐商品
        List<ProMerchantProductResponse> merchantProductResponseList = storeProductService.getRecommendedProductsByMerId(merchant.getId(), 4);
        merchantResponse.setProList(merchantProductResponseList);
        merchantResponse.setIsCollect(false);


        // 获取用户
        User user = userService.getInfo();
        // 用户收藏
        if (ObjectUtil.isNotNull(user)) {
            // 查询用户是否收藏收藏
            productDetailResponse.setUserCollect(storeProductRelationService.existCollectByUser(user.getUid(), storeProduct.getId()));
            // 商户是否被关注
            merchantResponse.setIsCollect(userMerchantCollectService.isCollect(user.getUid(), merchant.getId()));
        } else {
            productDetailResponse.setUserCollect(false);
        }
        productDetailResponse.setMerchantInfo(merchantResponse);
        // 异步调用进行数据统计
        asyncService.productDetailStatistics(storeProduct.getId(), ObjectUtil.isNotNull(user) ? user.getUid() : 0);
        return productDetailResponse;
    }

    /**
     * 获取商品SKU详情
     * @param id 商品编号
     * @return 商品详情信息
     */
    @Override
    public ProductDetailResponse getSkuDetail(Integer id) {
        ProductDetailResponse productDetailResponse = new ProductDetailResponse();
        // 查询商品
        StoreProduct storeProduct = storeProductService.getH5Detail(id);
        // 获取商品规格
        List<StoreProductAttr> attrList = attrService.getListByProductIdAndType(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        // 根据制式设置attr属性
        productDetailResponse.setProductAttr(attrList);
        // 根据制式设置sku属性
        HashMap<String, Object> skuMap = CollUtil.newHashMap();
        List<StoreProductAttrValue> storeProductAttrValues = storeProductAttrValueService.getListByProductIdAndType(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        for (StoreProductAttrValue storeProductAttrValue : storeProductAttrValues) {
            StoreProductAttrValueResponse atr = new StoreProductAttrValueResponse();
            BeanUtils.copyProperties(storeProductAttrValue, atr);
            skuMap.put(atr.getSku(), atr);
        }
        productDetailResponse.setProductValue(skuMap);
        return productDetailResponse;
    }

    /**
     * 商品评论列表
     * @param proId 商品编号
     * @param type 评价等级|0=全部,1=好评,2=中评,3=差评
     * @param pageParamRequest 分页参数
     * @return List<ProductReplyResponse>
     */
    @Override
    public PageInfo<ProductReplyResponse> getReplyList(Integer proId, Integer type, PageParamRequest pageParamRequest) {
        return storeProductReplyService.getH5List(proId, type, pageParamRequest);
    }

    /**
     * 产品评价数量和好评度
     * @return StoreProductReplayCountResponse
     */
    @Override
    public StoreProductReplayCountResponse getReplyCount(Integer id) {
        MyRecord myRecord = storeProductReplyService.getH5Count(id);
        Long sumCount = myRecord.getLong("sumCount");
        Long goodCount = myRecord.getLong("goodCount");
        Long inCount = myRecord.getLong("mediumCount");
        Long poorCount = myRecord.getLong("poorCount");
        String replyChance = myRecord.getStr("replyChance");
        Integer replyStar = myRecord.getInt("replyStar");
        return new StoreProductReplayCountResponse(sumCount, goodCount, inCount, poorCount, replyChance, replyStar);
    }

    /**
     * 商品列表转为首页商品格式
     * @param storeProductList 商品列表
     */
    private List<IndexProductResponse> productToIndexProduct(List<StoreProduct> storeProductList) {
        List<IndexProductResponse> productResponseArrayList = new ArrayList<>();
        for (StoreProduct storeProduct : storeProductList) {
            IndexProductResponse productResponse = new IndexProductResponse();
            BeanUtils.copyProperties(storeProduct, productResponse);
            productResponseArrayList.add(productResponse);
        }
        return productResponseArrayList;
    }

    /**
     * 商品详情评论
     * @param id 商品id
     * @return ProductDetailReplyResponse
     * 评论只有一条，图文
     * 评价总数
     * 好评率
     */
    @Override
    public ProductDetailReplyResponse getProductReply(Integer id) {
        return storeProductReplyService.getH5ProductReply(id);
    }

    /**
     * 获取商品排行榜
     * @return List
     */
    @Override
    public List<StoreProduct> getLeaderboard() {
        return storeProductService.getLeaderboard();
    }

    /**
     * 商户商品分类列表
     * @param merId 商户id
     * @return List
     */
    @Override
    public List<ProCategoryCacheVo> findMerchantProductCategoryList(Integer merId) {
        return storeProductCategoryService.findListByMerId(merId);
    }

    /**
     * 商户商品列表
     * @param request 搜索参数
     * @param pageParamRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<IndexProductResponse> getMerchantProList(MerchantProductSearchRequest request, PageParamRequest pageParamRequest) {
        PageInfo<StoreProduct> pageInfo = storeProductService.findMerchantProH5List(request, pageParamRequest);
        List<StoreProduct> storeProductList = pageInfo.getList();
        if (CollUtil.isEmpty(storeProductList)) {
            return CommonPage.copyPageInfo(pageInfo, CollUtil.newArrayList());
        }
        List<IndexProductResponse> responseList = productToIndexProduct(storeProductList);
        return CommonPage.copyPageInfo(pageInfo, responseList);
    }

}

