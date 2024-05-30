package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.system.SystemMenu;
import com.zbkj.common.request.SystemMenuRequest;
import com.zbkj.common.request.SystemMenuSearchRequest;
import com.zbkj.common.vo.MenuCheckVo;
import com.zbkj.common.vo.MyRecord;
import io.swagger.models.auth.In;

import java.util.List;

/**
 *  SystemMenuService 接口
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
public interface SystemMenuService extends IService<SystemMenu> {

    /**
     * 通过权限获取管理员可访问目录
     * @return List<SystemMenu>
     */
    List<SystemMenu> findCatalogueByPermission(List<String> permissionsList);

    /**
     * 获取所有菜单
     * @param type 系统菜单类型：3-平台 4-商户
     * @return List<SystemMenu>
     */
    List<SystemMenu> findAllCatalogue(Integer type);

    /**
     * 菜单列表
     * @param request 请求参数
     */
    List<SystemMenu> getAdminList(SystemMenuSearchRequest request);

    /**
     * 新增菜单
     * @param systemMenuRequest 菜单参数
     * @return Boolean
     */
    Boolean add(SystemMenuRequest systemMenuRequest);

    /**
     * 根据id删除菜单
     * @param id 菜单id
     * @return Boolean
     */
    Boolean deleteById(Integer id);

    /**
     * 修改菜单
     * @param systemMenuRequest 菜单参数
     * @return Boolean
     */
    Boolean edit(SystemMenuRequest systemMenuRequest);

    /**
     * 获取菜单详情
     * @param id 菜单id
     * @return SystemMenu
     */
    SystemMenu getInfo(Integer id);

    /**
     * 修改菜单显示状态
     * @param id 菜单id
     * @return Boolean
     */
    Boolean updateShowStatus(Integer id);

    /**
     * 获取菜单缓存列表
     * @return List
     */
    List<SystemMenu> getCacheList();

    /**
     * 菜单缓存树
     * @return List
     */
    List<MenuCheckVo> getCacheTree();

    /**
     * 获取所有权限
     * @param type 系统菜单类型:3-平台 4-商户
     * @return List
     */
    List<SystemMenu> getAllPermissions(Integer type);

    /**
     * 通过用户id获取权限
     */
    List<SystemMenu> findPermissionByUserId(Integer userId);

    /**
     * 获取用户路由
     * @param userId 用户id
     * @return List
     */
    List<SystemMenu> getMenusByUserId(Integer userId);

    /**
     * 平台菜单列表
     * @param request 请求参数
     * @return List
     */
    List<SystemMenu> getPlatformList(SystemMenuSearchRequest request);

    /**
     * 商户菜单列表
     * @param request 请求参数
     * @return List
     */
    List<SystemMenu> getMerchantList(SystemMenuSearchRequest request);

    /**
     * 新增平台菜单
     * @param systemMenuRequest 菜单参数
     * @return Boolean
     */
    Boolean addPlatformMenu(SystemMenuRequest systemMenuRequest);

    /**
     * 新增商户菜单
     * @param systemMenuRequest 菜单参数
     * @return Boolean
     */
    Boolean addMerchantMenu(SystemMenuRequest systemMenuRequest);

    /**
     * 删除平台端菜单
     * @param id 菜单id
     * @return Boolean
     */
    Boolean deletePlatformMenu(Integer id);

    /**
     * 删除商户端菜单
     * @param id 菜单id
     * @return Boolean
     */
    Boolean deleteMerchantMenu(Integer id);

    /**
     * 修改平台端菜单
     * @param systemMenuRequest 菜单参数
     * @return Boolean
     */
    Boolean editPlatformMenu(SystemMenuRequest systemMenuRequest);

    /**
     * 修改商户端菜单
     * @param systemMenuRequest 菜单参数
     * @return Boolean
     */
    Boolean editMerchantMenu(SystemMenuRequest systemMenuRequest);

    /**
     * 修改平台端菜单显示状态
     * @param id 菜单id
     * @return Boolean
     */
    Boolean updatePlatformShowStatus(Integer id);

    /**
     * 修改商户端菜单显示状态
     * @param id 菜单id
     * @return Boolean
     */
    Boolean updateMerchantShowStatus(Integer id);

    /**
     * 平台端菜单缓存树
     * @return List
     */
    List<MenuCheckVo> getPlatformMenuCacheTree();

    /**
     * 商户端菜单缓存树
     * @return List
     */
    List<MenuCheckVo> getMenuCacheList();


    /**
     * 获取菜单缓存列表
     * @param type 3-平台端，4-商户端
     * @return List
     */
    List<SystemMenu> getMenuCacheList(Integer type);
}