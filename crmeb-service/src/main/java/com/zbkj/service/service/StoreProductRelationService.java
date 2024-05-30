package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.product.StoreProductRelation;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserCollectAllRequest;
import com.zbkj.common.request.UserCollectRequest;
import com.zbkj.common.response.UserRelationResponse;

/**
 * StoreProductRelationService 接口
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
public interface StoreProductRelationService extends IService<StoreProductRelation> {

    /**
     * 取消收藏
     * @param requestJson 收藏idsJson
     * @return Boolean
     */
    Boolean delete(String requestJson);

    /**
     * 批量收藏
     * @param request 收藏参数
     * @return Boolean
     */
    Boolean all(UserCollectAllRequest request);

    /**
     * 获取用户收藏列表
     * @param pageParamRequest 分页参数
     * @return List<UserRelationResponse>
     */
    PageInfo<UserRelationResponse> getUserList(PageParamRequest pageParamRequest);

    /**
     * 获取用户的收藏数量
     * @param uid 用户uid
     * @return 收藏数量
     */
    Integer getCollectCountByUid(Integer uid);

    /**
     * 根据商品Id取消收藏
     * @param proId 商品Id
     * @return Boolean
     */
    Boolean deleteByProId(Integer proId);

    /**
     * 根据商品Id取消收藏
     * @param proId 商品Id
     * @return Boolean
     */
    Boolean deleteByProIdAndUid(Integer proId);

    /**
     * 根据日期获取收藏量
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    Integer getCountByDate(String date);

    /**
     * 根据日期获取收藏量
     * @param date 日期，yyyy-MM-dd格式
     * @param proId 商品id
     * @return Integer
     */
    Integer getCountByDateAndProId(String date, Integer proId);

    /**
     * 添加收藏
     * @param request 收藏参数
     */
    Boolean add(UserCollectRequest request);

    /**
     * 获取商品的收藏数量
     * @param productId 商品id
     * @return Integer
     */
    Integer getCollectCountByProductId(Integer productId);

    /**
     * 用户是否收藏
     * @param uid 用户uid
     * @param proId 商品id
     * @return Boolean
     */
    Boolean existCollectByUser(Integer uid, Integer proId);
}
