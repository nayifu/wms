package com.zbkj.service.service;

import com.alibaba.fastjson.JSONException;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.vo.SimpleProductVo;

import java.io.IOException;
import java.util.List;

/**
 * StoreProductService 接口
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
public interface StoreProductService extends IService<StoreProduct> {

    /**
     * 获取产品列表Admin
     * @param request 筛选参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<AdminProductListResponse> getAdminList(StoreProductSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 根据id集合获取商品简单信息
     * @param productIds id集合
     * @return 商品信息
     */
    List<SimpleProductVo> getSimpleListInIds(List<Integer> productIds);

    /**
     * 新增商品
     * @param request 商品请求对象
     * @return Boolean
     */
    Boolean save(StoreProductAddRequest request);

    /**
     * 更新商品信息
     * @param storeProductRequest 商品参数
     * @return 更新结果
     */
    Boolean update(StoreProductAddRequest storeProductRequest);

    /**
     * 商品详情（管理端）
     * @param id 商品id
     * @return StoreProductInfoResponse
     */
    StoreProductInfoResponse getInfo(Integer id);

    /**
     * 获取tabsHeader对应数量
     * @return List
     */
    List<StoreProductTabsHeader> getTabsHeader();

    /**
     * 根据其他平台url导入产品信息
     * @param url 待倒入平台的url
     * @param tag 待导入平台标识
     * @return 待导入的商品信息
     */
    StoreProductRequest importProductFrom99Api(String url, int tag);

    /**
     * 删除商品
     * @param productId 商品id
     * @param type 类型：recycle——回收站 delete——彻底删除
     * @return 删除结果
     */
    Boolean deleteProduct(Integer productId, String type);

    /**
     * 恢复已删除商品
     * @param productId 商品id
     * @return 恢复结果
     */
    Boolean reStoreProduct(Integer productId);

    /**
     * 添加/扣减库存
     * @param id 商品id
     * @param num 数量
     * @param type 类型：add—添加，sub—扣减
     */
    Boolean operationStock(Integer id, Integer num, String type);

    /**
     * 下架
     * @param id 商品id
     */
    Boolean offShelf(Integer id);

    /**
     * 上架
     * @param id 商品id
     * @return Boolean
     */
    Boolean putOnShelf(Integer id);

    /**
     * 首页商品列表
     * @param pageParamRequest 分页参数
     * @return CommonPage
     */
    PageInfo<StoreProduct> getIndexProduct(PageParamRequest pageParamRequest);

    /**
     * 获取商品移动端列表
     * @param request 筛选参数
     * @param pageRequest 分页参数
     * @return List
     */
    PageInfo<StoreProduct> findH5List(ProductRequest request, PageParamRequest pageRequest);

    /**
     * 获取移动端商品详情
     * @param id 商品id
     * @return StoreProduct
     */
    StoreProduct getH5Detail(Integer id);

    /**
     * 获取购物车商品信息
     * @param productId 商品编号
     * @return StoreProduct
     */
    StoreProduct getCartByProId(Integer productId);

    /**
     * 根据日期获取新增商品数量
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    Integer getNewProductByDate(String date);

    /**
     * 获取所有未删除的商品
     * @return List<StoreProduct>
     */
    List<StoreProduct> findAllProductByNotDelte();

    /**
     * 模糊搜索商品名称
     * @param productName 商品名称
     * @param merId 商户Id
     * @return List
     */
    List<StoreProduct> likeProductName(String productName, Integer merId);

    /**
     * 警戒库存数量
     * @return Integer
     */
    Integer getVigilanceInventoryNum(Integer merId);

    /**
     * 销售中（上架）商品数量
     * @return Integer
     */
    Integer getOnSaleNum(Integer merId);

    /**
     * 未销售（仓库）商品数量
     * @return Integer
     */
    Integer getNotSaleNum(Integer merId);

    /**
     * 获取商品排行榜
     * @return List
     */
    List<StoreProduct> getLeaderboard();

    /**
     * 强制下架商户所有商品
     * @param merchantId 商户ID
     * @return Boolean
     */
    Boolean forcedRemovalAll(Integer merchantId);

    /**
     * 平台端商品分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<PlatformProductListResponse> getPlatformPageList(ProductSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 商品审核
     * @param request 审核参数
     * @return Boolean
     */
    Boolean audit(StoreProductAuditRequest request);

    /**
     * 强制下加商品
     * @param request 商品id参数
     * @return Boolean
     */
    Boolean forceDown(ProductForceDownRequest request);

    /**
     * 修改虚拟销量
     * @param request 修改参数
     * @return Boolean
     */
    Boolean updateVirtualSales(ProductVirtualSalesRequest request);

    /**
     * 是否有商品使用对应的商户商品分类
     * @param id 商户商品分类id
     * @return Boolean
     */
    Boolean isExistStoreCategory(Integer id);

    /**
     * 商品增加浏览量
     * @param proId 商品id
     * @return Boolean
     */
    Boolean addBrowse(Integer proId);

    /**
     * 获取商户推荐商品
     * @param merId 商户id
     * @param num 查询商品数量
     * @return List
     */
    List<ProMerchantProductResponse> getRecommendedProductsByMerId(Integer merId, Integer num);

    /**
     * 商户商品列表
     * @param request 搜索参数
     * @param pageParamRequest 分页参数
     * @return List
     */
    PageInfo<StoreProduct> findMerchantProH5List(MerchantProductSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 修改商品后台排序
     * @param id 商品id
     * @param rank 商品后台排序
     * @return Boolean
     */
    Boolean updateRank(Integer id, Integer rank);

    /**
     * 判断商品是否使用品牌
     * @param brandId 品牌id
     * @return Boolean
     */
    Boolean isUseBrand(Integer brandId);

    /**
     * 判断商品是否使用平台分类
     * @param categoryId 平台分类id
     * @return Boolean
     */
    Boolean isUsePlatformCategory(Integer categoryId);

    /**
     * 查询使用服务保障的商品列表
     * @param gid 服务保障id
     * @return List
     */
    List<StoreProduct> findUseGuarantee(Integer gid);

    /**
     * 判断商品是否使用服务保障
     * @param gid 服务保障id
     * @return Boolean
     */
    Boolean isUseGuarantee(Integer gid);

    /**
     * 获取待审核商品数量
     */
    Integer getAwaitAuditNum();

    /**
     * 下架商品商品
     * @param merId 商户id
     */
    Boolean downByMerId(Integer merId);

    /**
     * 平台端获取商品表头数量
     * @return List
     */
    List<StoreProductTabsHeader> getPlatformTabsHeader();
}
