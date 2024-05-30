package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.product.ProductCategory;
import com.zbkj.common.request.ProductCategoryRequest;
import com.zbkj.common.vo.ProCategoryCacheVo;

import java.util.List;

/**
*  ProductCategoryService 接口
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
public interface ProductCategoryService extends IService<ProductCategory> {

    /**
     * 获取分类列表
     */
    List<ProductCategory> getAdminList();

    /**
     * 添加商品分类
     * @param request 添加参数
     * @return Boolean
     */
    Boolean add(ProductCategoryRequest request);

    /**
     * 删除分类
     * @param id 分类ID
     * @return Boolean
     */
    Boolean delete(Integer id);

    /**
     * 修改分类
     * @param request 修改参数
     * @return Boolean
     */
    Boolean edit(ProductCategoryRequest request);

    /**
     * 修改分类显示状态
     * @param id 分类ID
     * @return Boolean
     */
    Boolean updateShowStatus(Integer id);

    /**
     * 获取分类缓存树(平台端)
     * @return List<ProCategoryCacheVo>
     */
    List<ProCategoryCacheVo> getCacheTree();

    /**
     * 商户端分类缓存树
     * @return List<ProCategoryCacheVo>
     */
    List<ProCategoryCacheVo> getMerchantCacheTree();

    /**
     * 根据菜单id获取所有下级对象
     * @param pid 菜单id
     * @param level 分类级别
     * @return List<ProductCategory>
     */
    List<ProductCategory> findAllChildListByPid(Integer pid, Integer level);
}