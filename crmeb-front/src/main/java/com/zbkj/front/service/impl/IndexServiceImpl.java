package com.zbkj.front.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.GroupDataConstants;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.constants.VisitRecordConstants;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.model.system.SystemConfig;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.IndexInfoResponse;
import com.zbkj.common.response.IndexProductResponse;
import com.zbkj.common.response.PageLayoutBottomNavigationResponse;
import com.zbkj.common.response.PcIndexInfoResponse;
import com.zbkj.common.vo.ProCategoryCacheVo;
import com.zbkj.front.service.AsyncService;
import com.zbkj.front.service.IndexService;
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
public class IndexServiceImpl implements IndexService {

    @Autowired
    private SystemGroupDataService systemGroupDataService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private AsyncService asyncService;

    /**
     * 首页数据
     * banner、金刚区、广告位
     */
    @Override
    public IndexInfoResponse getIndexInfo() {
        IndexInfoResponse indexInfoResponse = new IndexInfoResponse();
        indexInfoResponse.setBanner(systemGroupDataService.getListMapByGid(GroupDataConstants.GROUP_DATA_ID_INDEX_BANNER)); //首页banner滚动图
        indexInfoResponse.setIndexBannerType(systemConfigService.getValueByKey(SysConfigConstants.FORNT_INDEX_BANNER_TYPE));
        indexInfoResponse.setBastBanner(systemGroupDataService.getListMapByGid(GroupDataConstants.GROUP_DATA_ID_INDEX_BEST_BANNER)); //中部推荐banner图
        indexInfoResponse.setMenus(systemGroupDataService.getListMapByGid(GroupDataConstants.GROUP_DATA_ID_INDEX_MENU)); //导航模块
        indexInfoResponse.setRanking(systemGroupDataService.getListMapByGid(GroupDataConstants.GROUP_DATA_ID_STORE_PRODUCT_RANKING)); //导航模块
        indexInfoResponse.setShopPayCurrency(systemConfigService.getValueByKey(Constants.SHOP_PAY_CURRENCY)); //商城当前使用的币种


        indexInfoResponse.setLogoUrl(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_KEY_SITE_LOGO));// 企业logo地址
        // 客服部分
        indexInfoResponse.setConsumerType(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_TYPE));
        switch (indexInfoResponse.getConsumerType()) {
            case Constants.CONSUMER_TYPE_H5:
                indexInfoResponse.setConsumerH5Url(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_H5_URL));
            case Constants.CONSUMER_TYPE_HOTLINE:
                indexInfoResponse.setConsumerHotline(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_HOTLINE));
            case Constants.CONSUMER_TYPE_MESSAGE:
                indexInfoResponse.setConsumerMessage(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_MESSAGE));
            case Constants.CONSUMER_TYPE_EMAIL:
                indexInfoResponse.setConsumerEmail(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_EMAIL));
        }
        // 查询6个首页一级商品分类
        List<ProCategoryCacheVo> categoryVoList = CollUtil.newArrayList();
        List<ProCategoryCacheVo> merchantCacheTree = productCategoryService.getMerchantCacheTree();
        for (int i = 0; i < 6; i++) {
            if (merchantCacheTree.size() < (i +1)) {
                break;
            }
            ProCategoryCacheVo categoryCacheVo = merchantCacheTree.get(i);
            ProCategoryCacheVo cacheVo = new ProCategoryCacheVo();
            cacheVo.setId(categoryCacheVo.getId());
            cacheVo.setName(categoryCacheVo.getName());
            cacheVo.setIcon(categoryCacheVo.getIcon());
            categoryVoList.add(cacheVo);
        }
        indexInfoResponse.setCategoryList(categoryVoList);
        // 保存用户访问记录
        asyncService.saveUserVisit(userService.getUserId(), VisitRecordConstants.VISIT_TYPE_INDEX);
        return indexInfoResponse;
    }

    /**
     * 热门搜索
     * @return List<HashMap<String, String>>
     */
    @Override
    public List<HashMap<String, Object>> hotKeywords() {
        return systemGroupDataService.getListMapByGid(GroupDataConstants.GROUP_DATA_ID_INDEX_KEYWORDS);
    }

    /**
     * 获取首页商品列表
     * @param pageParamRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<IndexProductResponse> findIndexProductList(PageParamRequest pageParamRequest) {
        PageInfo<StoreProduct> pageInfo = storeProductService.getIndexProduct(pageParamRequest);
        List<StoreProduct> storeProductList = pageInfo.getList();
        if(CollUtil.isEmpty(storeProductList)) {
            return CommonPage.copyPageInfo(pageInfo, CollUtil.newArrayList());
        }
        List<IndexProductResponse> productResponseArrayList = new ArrayList<>();
        for (StoreProduct storeProduct : storeProductList) {
            IndexProductResponse productResponse = new IndexProductResponse();
            BeanUtils.copyProperties(storeProduct, productResponse);
            productResponseArrayList.add(productResponse);
        }
        return CommonPage.copyPageInfo(pageInfo, productResponseArrayList);
    }

    /**
     * 获取颜色配置
     * @return SystemConfig
     */
    @Override
    public SystemConfig getColorConfig() {
        return systemConfigService.getColorConfig();
    }

    /**
     * 获取全局本地图片域名
     * @return String
     */
    @Override
    public String getImageDomain() {
        String localUploadUrl = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_LOCAL_UPLOAD_URL);
        return StrUtil.isBlank(localUploadUrl) ? "" : localUploadUrl;
    }

    /**
     * PC首页数据
     */
    @Override
    public PcIndexInfoResponse getPcIndexInfo() {
        PcIndexInfoResponse indexInfoResponse = new PcIndexInfoResponse();
        indexInfoResponse.setBanner(systemGroupDataService.getListMapByGid(GroupDataConstants.GROUP_DATA_ID_PC_HONE_BANNER));
//        indexInfoResponse.setShopStreetBack(systemConfigService.getValueByKey(SysConfigConstants.PC_HOME_SHOP_STREET_BACK_IMAGE));
        indexInfoResponse.setRecommendImage(systemConfigService.getValueByKey(SysConfigConstants.PC_HOME_RECOMMEND_IMAGE));
        indexInfoResponse.setLogoUrl(systemConfigService.getValueByKey(SysConfigConstants.PC_TOP_LOGO));
        indexInfoResponse.setBastBanner(systemGroupDataService.getListMapByGid(GroupDataConstants.GROUP_DATA_ID_INDEX_BEST_BANNER)); //中部推荐banner图
        indexInfoResponse.setShopStreetHeaderImag(systemConfigService.getValueByKey(SysConfigConstants.PC_SHOP_STREET_HEADER_IMAGE));
        indexInfoResponse.setShopPayCurrency(systemConfigService.getValueByKey(Constants.SHOP_PAY_CURRENCY)); //商城当前使用的币种
        // 客服部分
        indexInfoResponse.setConsumerType(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_TYPE));
        switch (indexInfoResponse.getConsumerType()) {
            case Constants.CONSUMER_TYPE_H5:
                indexInfoResponse.setConsumerH5Url(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_H5_URL));
            case Constants.CONSUMER_TYPE_HOTLINE:
                indexInfoResponse.setConsumerHotline(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_HOTLINE));
            case Constants.CONSUMER_TYPE_MESSAGE:
                indexInfoResponse.setConsumerMessage(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_MESSAGE));
            case Constants.CONSUMER_TYPE_EMAIL:
                indexInfoResponse.setConsumerEmail(systemConfigService.getValueByKey(SysConfigConstants.CONFIG_CONSUMER_EMAIL));
        }
        // 保存用户访问记录
        asyncService.saveUserVisit(userService.getUserId(), VisitRecordConstants.VISIT_TYPE_INDEX);
        return indexInfoResponse;
    }

    /**
     * 获取底部导航信息
     */
    @Override
    public PageLayoutBottomNavigationResponse getBottomNavigationInfo() {
        String isCustom = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_BOTTOM_NAVIGATION_IS_CUSTOM);
        List<HashMap<String, Object>> bnList = systemGroupDataService.getListMapByGid(GroupDataConstants.GROUP_DATA_ID_BOTTOM_NAVIGATION);
        PageLayoutBottomNavigationResponse response = new PageLayoutBottomNavigationResponse();
        response.setIsCustom(isCustom);
        response.setBottomNavigationList(bnList);
        return response;
    }
}

