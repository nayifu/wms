package com.zbkj.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.product.StoreProductRule;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreProductRuleRequest;
import com.zbkj.common.request.StoreProductRuleSearchRequest;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.service.dao.StoreProductRuleDao;
import com.zbkj.service.service.StoreProductRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * StoreProductRuleServiceImpl 接口实现
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
public class StoreProductRuleServiceImpl extends ServiceImpl<StoreProductRuleDao, StoreProductRule> implements StoreProductRuleService {

    @Resource
    private StoreProductRuleDao dao;


    /**
    * 列表
    * @param request 请求参数
    * @param pageParamRequest 分页类参数
    * @return PageInfo<StoreProductRule>
    */
    @Override
    public PageInfo<StoreProductRule> getList(StoreProductRuleSearchRequest request, PageParamRequest pageParamRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Page<StoreProductRule> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StoreProductRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StoreProductRule::getMerId, systemAdmin.getMerId());
        if (StrUtil.isNotEmpty(request.getKeywords())) {
            lambdaQueryWrapper.like(StoreProductRule::getRuleName, request.getKeywords());
        }
        lambdaQueryWrapper.orderByDesc(StoreProductRule::getId);
        List<StoreProductRule> list = dao.selectList(lambdaQueryWrapper);
        return CommonPage.copyPageInfo(page, list);
    }

    /**
     * 新增商品规格
     * @param storeProductRuleRequest 规格参数
     * @return 新增结果
     */
    @Override
    public boolean save(StoreProductRuleRequest storeProductRuleRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        if (existRuleName(storeProductRuleRequest.getRuleName(), systemAdmin.getMerId())) {
            throw new CrmebException("此规格值已经存在");
        }
        StoreProductRule storeProductRule = new StoreProductRule();
        BeanUtils.copyProperties(storeProductRuleRequest, storeProductRule);
        storeProductRule.setMerId(systemAdmin.getMerId());
        return save(storeProductRule);
    }

    /**
     * 规格名是否存在
     * @param ruleName 规格名
     */
    private Boolean existRuleName(String ruleName, Integer merId) {
        LambdaQueryWrapper<StoreProductRule> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProductRule::getId);
        lqw.eq(StoreProductRule::getRuleName, ruleName);
        lqw.eq(StoreProductRule::getMerId, merId);
        lqw.last(" limit 1");
        StoreProductRule productRule = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(productRule);
    }

    /**
     * 修改规格
     * @param storeProductRuleRequest 规格参数
     * @return Boolean
     */
    @Override
    public Boolean updateRule(StoreProductRuleRequest storeProductRuleRequest) {
        if (ObjectUtil.isNull(storeProductRuleRequest.getId())) {
            throw new CrmebException("请先选择规格");
        }
        StoreProductRule storeProductRule = new StoreProductRule();
        BeanUtils.copyProperties(storeProductRuleRequest, storeProductRule);
        return updateById(storeProductRule);
    }

}

