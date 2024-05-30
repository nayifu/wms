package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.ProductConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.product.StoreProductRelation;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserCollectAllRequest;
import com.zbkj.common.request.UserCollectRequest;
import com.zbkj.common.response.UserRelationResponse;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.service.dao.StoreProductRelationDao;
import com.zbkj.service.service.StoreProductRelationService;
import com.zbkj.service.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * StoreProductRelationServiceImpl 接口实现
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
@Service
public class StoreProductRelationServiceImpl extends ServiceImpl<StoreProductRelationDao, StoreProductRelation>
        implements StoreProductRelationService {

    @Resource
    private StoreProductRelationDao dao;

    @Autowired
    private UserService userService;

    /**
     * 添加收藏产品
     * @param request UserCollectAllRequest 新增参数
     * @return boolean
     */
    @Override
    public Boolean all(UserCollectAllRequest request) {
        Integer[] arr = request.getProductId();
        if(arr.length < 1){
            throw new CrmebException("请选择产品");
        }

        List<Integer> list = CrmebUtil.arrayUnique(arr);

        Integer uid = userService.getUserIdException();
        deleteAll(request, uid, ProductConstants.PRODUCT_RELATION_TYPE_COLLECT);  //先删除所有已存在的

        ArrayList<StoreProductRelation> storeProductRelationList = new ArrayList<>();
        for (Integer productId: list) {
            StoreProductRelation storeProductRelation = new StoreProductRelation();
            storeProductRelation.setUid(uid);
            storeProductRelation.setType(ProductConstants.PRODUCT_RELATION_TYPE_COLLECT);
            storeProductRelation.setProductId(productId);
            storeProductRelation.setCategory(request.getCategory());
            storeProductRelationList.add(storeProductRelation);
        }
        return saveBatch(storeProductRelationList);
    }


    /**
     * 取消收藏产品
     */
    @Override
    public Boolean delete(String requestJson) {
        JSONObject jsonObject = JSONObject.parseObject(requestJson);
        if (StrUtil.isBlank(jsonObject.getString("ids"))) {
            throw new CrmebException("Collection id cannot be empty");
        }
        List<Integer> idList = CrmebUtil.stringToArray(jsonObject.getString("ids"));
        if (CollUtil.isEmpty(idList)) {
            throw new CrmebException("Collection id cannot be empty");
        }
        Integer userId = userService.getUserIdException();
        LambdaQueryWrapper<StoreProductRelation> lqw = Wrappers.lambdaQuery();
        lqw.in(StoreProductRelation::getId, idList);
        lqw.eq(StoreProductRelation::getUid, userId);
        int delete = dao.delete(lqw);
        return delete > 0;
    }

    /**
     * 取消收藏产品
     * @param request UserCollectAllRequest 参数
     * @param type 类型
     */
    private void deleteAll(UserCollectAllRequest request, Integer uid, String type) {
        LambdaQueryWrapper<StoreProductRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(StoreProductRelation::getProductId, Arrays.asList(request.getProductId()))
                .eq(StoreProductRelation::getCategory, request.getCategory())
                .eq(StoreProductRelation::getUid, uid)
                .eq(StoreProductRelation::getType, type);
        dao.delete(lambdaQueryWrapper);
    }

    /**
     * 获取用户收藏列表
     * @param pageParamRequest 分页参数
     * @return List<UserRelationResponse>
     */
    @Override
    public PageInfo<UserRelationResponse> getUserList(PageParamRequest pageParamRequest) {
        Integer userId = userService.getUserIdException();
        Page<Object> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<UserRelationResponse> list = dao.getUserList(userId);
        return CommonPage.copyPageInfo(page, list);
    }

    /**
     * 获取用户的收藏数量
     * @param uid 用户uid
     * @return 收藏数量
     */
    @Override
    public Integer getCollectCountByUid(Integer uid) {
        LambdaQueryWrapper<StoreProductRelation> lqr = Wrappers.lambdaQuery();
        lqr.eq(StoreProductRelation::getUid, uid);
        lqr.eq(StoreProductRelation::getType,ProductConstants.PRODUCT_RELATION_TYPE_COLLECT);
        return dao.selectCount(lqr);
    }

    /**
     * 根据商品Id取消收藏
     * @param proId 商品Id
     * @return Boolean
     */
    @Override
    public Boolean deleteByProId(Integer proId) {
        LambdaQueryWrapper<StoreProductRelation> lqw = Wrappers.lambdaQuery();
        lqw.in(StoreProductRelation::getProductId, proId);
        int delete = dao.delete(lqw);
        return delete > 0;
    }

    /**
     * 根据商品Id取消收藏
     * @param proId 商品Id
     * @return Boolean
     */
    @Override
    public Boolean deleteByProIdAndUid(Integer proId) {
        Integer userId = userService.getUserIdException();
        LambdaQueryWrapper<StoreProductRelation> lqw = Wrappers.lambdaQuery();
        lqw.in(StoreProductRelation::getProductId, proId);
        lqw.eq(StoreProductRelation::getUid, userId);
        int delete = dao.delete(lqw);
        return delete > 0;
    }

    /**
     * 根据日期获取收藏量
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    @Override
    public Integer getCountByDate(String date) {
        LambdaQueryWrapper<StoreProductRelation> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProductRelation::getId);
        lqw.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        return dao.selectCount(lqw);
    }

    /**
     * 根据日期获取收藏量
     * @param date 日期，yyyy-MM-dd格式
     * @param proId 商品id
     * @return Integer
     */
    @Override
    public Integer getCountByDateAndProId(String date, Integer proId) {
        LambdaQueryWrapper<StoreProductRelation> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProductRelation::getId);
        lqw.eq(StoreProductRelation::getProductId, proId);
        lqw.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        return dao.selectCount(lqw);
    }

    /**
     * 添加收藏
     * @param request 收藏参数
     */
    @Override
    public Boolean add(UserCollectRequest request) {
        StoreProductRelation storeProductRelation = new StoreProductRelation();
        BeanUtils.copyProperties(request, storeProductRelation);
        storeProductRelation.setUid(userService.getUserIdException());
        return save(storeProductRelation);
    }

    /**
     * 获取商品的收藏数量
     * @param productId 商品id
     * @return Integer
     */
    @Override
    public Integer getCollectCountByProductId(Integer productId) {
        LambdaQueryWrapper<StoreProductRelation> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProductRelation::getId);
        lqw.eq(StoreProductRelation::getProductId, productId);
        return dao.selectCount(lqw);
    }

    /**
     * 用户是否收藏
     * @param uid 用户uid
     * @param proId 商品id
     * @return Boolean
     */
    @Override
    public Boolean existCollectByUser(Integer uid, Integer proId) {
        LambdaQueryWrapper<StoreProductRelation> lqw = new LambdaQueryWrapper<>();
        lqw.select(StoreProductRelation::getId);
        lqw.eq(StoreProductRelation::getProductId, proId);
        lqw.eq(StoreProductRelation::getUid, uid);
        lqw.eq(StoreProductRelation::getType,ProductConstants.PRODUCT_RELATION_TYPE_COLLECT);
        lqw.last(" limit 1");
        StoreProductRelation relation = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(relation);
    }
}

