package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.constants.RedisConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.product.StoreProductCategory;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.request.StoreProductCategoryRequest;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.ProCategoryCacheTree;
import com.zbkj.common.vo.ProCategoryCacheVo;
import com.zbkj.service.dao.StoreProductCategoryDao;
import com.zbkj.service.service.StoreProductCategoryService;
import com.zbkj.service.service.StoreProductService;
import com.zbkj.service.service.SystemAttachmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
*  StoreProductCategoryServiceImpl 接口实现
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
public class StoreProductCategoryServiceImpl extends ServiceImpl<StoreProductCategoryDao, StoreProductCategory> implements StoreProductCategoryService {

    @Resource
    private StoreProductCategoryDao dao;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SystemAttachmentService systemAttachmentService;
    @Autowired
    private StoreProductService productService;


    /**
     * 获取分类列表
     * @return List
     */
    @Override
    public List<StoreProductCategory> getAdminList() {
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        LambdaQueryWrapper<StoreProductCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreProductCategory::getIsDel, false);
        lqw.eq(StoreProductCategory::getMerId, admin.getMerId());
        lqw.orderByDesc(StoreProductCategory::getSort);
        lqw.orderByAsc(StoreProductCategory::getId);
        return dao.selectList(lqw);
    }

    /**
     * 添加分类
     * @param request 分类参数
     * @return Boolean
     */
    @Override
    public Boolean add(StoreProductCategoryRequest request) {
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        if (checkName(request.getName(), admin.getMerId())) {
            throw new CrmebException("分类名称已存在");
        }
        if (!request.getPid().equals(0)) {
            StoreProductCategory parentCategory = getByIdException(request.getPid());
            if (!parentCategory.getPid().equals(0)) {
                throw new CrmebException("商户商品分类只支持二级分类");
            }
        }
        StoreProductCategory productCategory = new StoreProductCategory();
        BeanUtils.copyProperties(request, productCategory);
        productCategory.setId(null);
        productCategory.setMerId(admin.getMerId());
        if (StrUtil.isNotEmpty(productCategory.getIcon())) {
            productCategory.setIcon(systemAttachmentService.clearPrefix(productCategory.getIcon()));
        }
        boolean save = save(productCategory);
        if (save) {
            redisUtil.delete(StrUtil.format(RedisConstants.STORE_PRODUCT_CATEGORY_CACHE_LIST_KEY, admin.getMerId()));
        }
        return save;
    }

    /**
     * 删除分类
     * @param id 分类id
     * @return Boolean
     */
    @Override
    public Boolean delete(Integer id) {
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        StoreProductCategory category = getByIdException(id);
        if (!admin.getMerId().equals(category.getMerId())) {
            throw new CrmebException("不能操作非自己商户的数据");
        }
        if (category.getPid().equals(0)) {
            List<StoreProductCategory> categoryList = findAllChildListByPid(id);
            if (CollUtil.isNotEmpty(categoryList)) {
                throw new CrmebException("该分类下有子分类，请先删除子分类");
            }
        }
        if (productService.isExistStoreCategory(category.getId())) {
            throw new CrmebException("有商品已使用该分类，请先修改商品分类");
        }
        category.setIsDel(true);
        boolean update = updateById(category);
        if (update) {
            redisUtil.delete(StrUtil.format(RedisConstants.STORE_PRODUCT_CATEGORY_CACHE_LIST_KEY, admin.getMerId()));
        }
        return update;
    }

    /**
     * 修改分类
     * @param request 修改参数
     * @return Boolean
     */
    @Override
    public Boolean edit(StoreProductCategoryRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new CrmebException("商品分类ID不能为空");
        }
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        StoreProductCategory oldCategory = getByIdException(request.getId());
        if (!admin.getMerId().equals(oldCategory.getMerId())) {
            throw new CrmebException("不能操作非自己商户的数据");
        }
        if (!oldCategory.getName().equals(request.getName())) {
            if (checkName(request.getName(), admin.getMerId())) {
                throw new CrmebException("分类名称已存在");
            }
        }
        if (!oldCategory.getPid().equals(0) && request.getPid().equals(0)) {
            if (productService.isExistStoreCategory(oldCategory.getPid())) {
                throw new CrmebException("分类已有商品使用，请先修改商品，再修改分类级别");
            }
        }
        StoreProductCategory category = new StoreProductCategory();
        BeanUtils.copyProperties(request, category);
        if (StrUtil.isNotEmpty(category.getIcon())) {
            category.setIcon(systemAttachmentService.clearPrefix(category.getIcon()));
        }
        boolean update = updateById(category);
        if (update) {
            redisUtil.delete(StrUtil.format(RedisConstants.STORE_PRODUCT_CATEGORY_CACHE_LIST_KEY, admin.getMerId()));
        }
        return update;
    }

    /**
     * 修改分类显示状态
     * @param id 分类ID
     * @return Boolean
     */
    @Override
    public Boolean updateShowStatus(Integer id) {
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        StoreProductCategory category = getByIdException(id);
        category.setIsShow(!category.getIsShow());
        boolean update = updateById(category);
        if (update) {
            redisUtil.delete(StrUtil.format(RedisConstants.STORE_PRODUCT_CATEGORY_CACHE_LIST_KEY, admin.getMerId()));
        }
        return update;
    }

    /**
     * 获取分类缓存树
     * @return List<ProCategoryCacheVo>
     */
    @Override
    public List<ProCategoryCacheVo> getCacheTree() {
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        return getCacheTreeByMerId(admin.getMerId());
    }

    private List<ProCategoryCacheVo> getCacheTreeByMerId(Integer merId) {
        List<StoreProductCategory> categoryList;
        if (redisUtil.exists(StrUtil.format(RedisConstants.STORE_PRODUCT_CATEGORY_CACHE_LIST_KEY, merId))) {
            categoryList = redisUtil.get(StrUtil.format(RedisConstants.STORE_PRODUCT_CATEGORY_CACHE_LIST_KEY, merId));
        } else {
            LambdaQueryWrapper<StoreProductCategory> lqw = Wrappers.lambdaQuery();
            lqw.eq(StoreProductCategory::getIsDel, false);
            lqw.eq(StoreProductCategory::getIsShow, true);
            lqw.eq(StoreProductCategory::getMerId, merId);
            categoryList = dao.selectList(lqw);
            if (CollUtil.isEmpty(categoryList)) {
                return CollUtil.newArrayList();
            }
            redisUtil.set(StrUtil.format(RedisConstants.STORE_PRODUCT_CATEGORY_CACHE_LIST_KEY, merId), categoryList);
        }
        List<ProCategoryCacheVo> voList = categoryList.stream().map(e -> {
            ProCategoryCacheVo cacheVo = new ProCategoryCacheVo();
            BeanUtils.copyProperties(e, cacheVo);
            return cacheVo;
        }).collect(Collectors.toList());
        ProCategoryCacheTree categoryTree = new ProCategoryCacheTree(voList);
        return categoryTree.buildTree();
    }

    /**
     * 获取商户商品分类列表
     * @param merId 商户id
     * @return List
     */
    @Override
    public List<ProCategoryCacheVo> findListByMerId(Integer merId) {
        return getCacheTreeByMerId(merId);
    }

    /**
     * 根据分类id获取所有下级对象
     * @param pid 分类父id
     * @return List<ProductCategory>
     */
    private List<StoreProductCategory> findAllChildListByPid(Integer pid) {
        LambdaQueryWrapper<StoreProductCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreProductCategory::getPid, pid);
        lqw.eq(StoreProductCategory::getIsDel, false);
        return dao.selectList(lqw);
    }

    private StoreProductCategory getByIdException(Integer id) {
        StoreProductCategory category = getById(id);
        if (ObjectUtil.isNull(category) || category.getIsDel()) {
            throw new CrmebException("分类不存在");
        }
        return category;
    }

    /**
     * 校验分类名称
     * @param name 分类名称
     * @return Boolean
     */
    private Boolean checkName(String name, Integer merId) {
        LambdaQueryWrapper<StoreProductCategory> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProductCategory::getId);
        lqw.eq(StoreProductCategory::getName, name);
        lqw.eq(StoreProductCategory::getMerId, merId);
        lqw.eq(StoreProductCategory::getIsDel, false);
        lqw.last(" limit 1");
        StoreProductCategory productCategory = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(productCategory) ? Boolean.TRUE : Boolean.FALSE;
    }
}

