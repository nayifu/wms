package com.zbkj.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.constants.SystemRoleConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.model.system.SystemMenu;
import com.zbkj.common.model.system.SystemRole;
import com.zbkj.common.model.system.SystemRoleMenu;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.RoleInfoResponse;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.MenuCheckTree;
import com.zbkj.common.vo.MenuCheckVo;
import com.zbkj.service.dao.SystemRoleDao;
import com.zbkj.service.service.SystemAdminService;
import com.zbkj.service.service.SystemMenuService;
import com.zbkj.service.service.SystemRoleMenuService;
import com.zbkj.service.service.SystemRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SystemRoleServiceImpl 接口实现
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
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleDao, SystemRole> implements SystemRoleService {

    @Resource
    private SystemRoleDao dao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private SystemRoleMenuService systemRoleMenuService;

    @Autowired
    private SystemMenuService systemMenuService;

    @Autowired
    private SystemAdminService systemAdminService;

    /**
     * 列表
     *
     * @param request          请求参数
     * @param pageParamRequest 分页类参数
     * @return List<SystemRole>
     */
    @Override
    public List<SystemRole> getList(SystemRole request, PageParamRequest pageParamRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        LambdaQueryWrapper<SystemRole> lqw = Wrappers.lambdaQuery();
        lqw.select(SystemRole::getId, SystemRole::getRoleName, SystemRole::getStatus,
                SystemRole::getCreateTime, SystemRole::getUpdateTime);
        if (ObjectUtil.isNotNull(request.getStatus())) {
            lqw.eq(SystemRole::getStatus, request.getStatus());
        }
        if (ObjectUtil.isNotNull(request.getRoleName())) {
            lqw.like(SystemRole::getRoleName, request.getRoleName());
        }
        lqw.eq(SystemRole::getMerId, systemAdmin.getMerId());
        lqw.eq(SystemRole::getType, getRoleTypeByCurrentAdmin(systemAdmin.getType()));
        lqw.orderByAsc(SystemRole::getId);
        return dao.selectList(lqw);
    }

    /**
     * 修改身份状态
     */
    @Override
    public Boolean updateStatus(Integer id, Boolean status) {
        SystemAdmin currentAdmin = SecurityUtil.getLoginUserVo().getUser();
        SystemRole role = getById(id);
        if (ObjectUtil.isNull(role)) {
            throw new CrmebException("身份不存在");
        }
        // 超级管理员不允许编辑
        if (role.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM_SUPER)
                || role.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER)) {
            throw new CrmebException("系统内置权限，不允许编辑");
        }
        if (role.getStatus().equals(status)) {
            return true;
        }
        role.setStatus(status);
        return updateById(role);
    }

    /**
     * 添加身份
     *
     * @param systemRoleRequest 身份参数
     * @return Boolean
     */
    @Override
    public Boolean add(SystemRole systemRoleRequest) {
        SystemAdmin currentAdmin = SecurityUtil.getLoginUserVo().getUser();
        if (existName(systemRoleRequest.getRoleName(), null)) {
            throw new CrmebException("角色名称重复");
        }
        List<Integer> ruleList = Stream.of(systemRoleRequest.getRules().split(",")).map(Integer::valueOf).distinct().collect(Collectors.toList());
        systemRoleRequest.setId(null);
        systemRoleRequest.setRules("");
        systemRoleRequest.setMerId(currentAdmin.getMerId());
        systemRoleRequest.setType(getRoleTypeByCurrentAdmin(currentAdmin.getType()));
        return transactionTemplate.execute(e -> {
            boolean save = save(systemRoleRequest);
            if (!save) {
                return Boolean.FALSE;
            }
            List<SystemRoleMenu> roleMenuList = ruleList.stream().map(rule -> {
                SystemRoleMenu roleMenu = new SystemRoleMenu();
                roleMenu.setRid(systemRoleRequest.getId());
                roleMenu.setMenuId(rule);
                return roleMenu;
            }).collect(Collectors.toList());
            systemRoleMenuService.saveBatch(roleMenuList, 100);
            return Boolean.TRUE;
        });
    }

    /**
     * 判断角色名称是否存在
     *
     * @param roleName 角色名称
     * @param id       角色id
     * @return Boolean
     */
    private Boolean existName(String roleName, Integer id) {
        LambdaQueryWrapper<SystemRole> lqw = Wrappers.lambdaQuery();
        lqw.eq(SystemRole::getRoleName, roleName);
        if (ObjectUtil.isNotNull(id)) {
            lqw.ne(SystemRole::getId, id);
        }
        lqw.last(" limit 1");
        Integer count = dao.selectCount(lqw);
        return count > 0;
    }

    /**
     * 修改身份管理表
     *
     * @param systemRole 修改参数
     */
    @Override
    public Boolean edit(SystemRole systemRole) {
        SystemRole role = getById(systemRole.getId());
        SystemAdmin currentAdmin = SecurityUtil.getLoginUserVo().getUser();
        if (ObjectUtil.isNull(role)) {
            throw new CrmebException("角色不存在");
        }
        if (!currentAdmin.getMerId().equals(role.getMerId())) {
            throw new CrmebException("非自己管理的角色不能修改");
        }
        // 超级管理员不允许编辑
        if (role.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM_SUPER)
                || role.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER)) {
            throw new CrmebException("系统内置权限，不允许编辑");
        }
        if (!role.getRoleName().equals(systemRole.getRoleName())) {
            if (existName(systemRole.getRoleName(), systemRole.getId())) {
                throw new CrmebException("角色名称重复");
            }
        }
        List<Integer> ruleList = Stream.of(systemRole.getRules().split(",")).map(Integer::valueOf).distinct().collect(Collectors.toList());
        List<SystemRoleMenu> roleMenuList = ruleList.stream().map(rule -> {
            SystemRoleMenu roleMenu = new SystemRoleMenu();
            roleMenu.setRid(systemRole.getId());
            roleMenu.setMenuId(rule);
            return roleMenu;
        }).collect(Collectors.toList());
        systemRole.setRules("");
        return transactionTemplate.execute(e -> {
            updateById(systemRole);
            systemRoleMenuService.deleteByRid(systemRole.getId());
            systemRoleMenuService.saveBatch(roleMenuList, 100);
            return Boolean.TRUE;
        });
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     * @return Boolean
     */
    @Override
    public Boolean delete(Integer id) {
        SystemRole systemRole = getById(id);
        if (ObjectUtil.isNull(systemRole)) {
            throw new CrmebException("角色已删除");
        }
        if (systemRole.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM_SUPER) ||
                systemRole.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER)) {
            throw new CrmebException("系统内置权限，不允许删除");
        }
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        if (!systemAdmin.getMerId().equals(systemRole.getMerId())) {
            throw new CrmebException("非自己平台管理的角色不能删除");
        }
        if (systemAdminService.isExistRole(id)) {
            throw new CrmebException("有管理员使用角色，无法删除");
        }
        return transactionTemplate.execute(e -> {
            dao.deleteById(id);
            systemRoleMenuService.deleteByRid(id);
            return Boolean.TRUE;
        });
    }

    /**
     * 获取角色详情
     *
     * @param id 角色id
     * @return RoleInfoResponse
     */
    @Override
    public RoleInfoResponse getInfo(Integer id) {
        SystemRole systemRole = getById(id);
        if (ObjectUtil.isNull(systemRole)) {
            throw new CrmebException("角色不存在");
        }
        // 查询角色对应的菜单(权限)
        List<Integer> menuIdList = systemRoleMenuService.getMenuListByRid(id);
        List<SystemMenu> menuList;
        if (systemRole.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM_SUPER) || systemRole.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM)) {
            menuList = systemMenuService.getMenuCacheList(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM);
        } else {// 商户端
            menuList = systemMenuService.getMenuCacheList(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT);
        }

        List<MenuCheckVo> menuCheckVoList = menuList.stream().map(menu -> {
            MenuCheckVo menuCheckVo = new MenuCheckVo();
            BeanUtils.copyProperties(menu, menuCheckVo);
            if (menuIdList.contains(menu.getId())) {
                menuCheckVo.setChecked(true);
            } else {
                menuCheckVo.setChecked(false);
            }
            return menuCheckVo;
        }).collect(Collectors.toList());

        RoleInfoResponse response = new RoleInfoResponse();
        BeanUtils.copyProperties(systemRole, response);
        response.setMenuList(new MenuCheckTree(menuCheckVoList).buildTree());
        return response;
    }

    /**
     * 根据当前登录人判断非超管的权限 一般使用到区分平台和平台的位置
     *
     * @return 当前登录人的所属权限
     */
    @Override
    public Integer getRoleTypeByCurrentAdmin() {
        SystemAdmin currentAdmin = SecurityUtil.getLoginUserVo().getUser();
        if (currentAdmin.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER) ||
                currentAdmin.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT)) {
            return SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT;
        } else if (currentAdmin.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM_SUPER) ||
                currentAdmin.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM)) {
            return SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM;
        }
        return null;
    }

    /**
     * @param type 管理员类型：1= 平台超管, 2=商户超管, 3=系统管理员，4=商户管理员
     * @return Integer
     */
    private Integer getRoleTypeByCurrentAdmin(Integer type) {
        if (type.equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER) ||
                type.equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT)) {
            return SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT;
        } else if (type.equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM_SUPER) ||
                type.equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM)) {
            return SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM;
        }
        return null;
    }

    /**
     * 根据当前登录人获取素材资源归属人 平台的是权限 其他的都是商户id
     *
     * @return 资源归属方
     */
    @Override
    public Integer getOwnerByCurrentAdmin() {
        SystemAdmin currentAdmin = SecurityUtil.getLoginUserVo().getUser();
        if (currentAdmin.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER) ||
                currentAdmin.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT)) {
            return currentAdmin.getMerId();
        } else if (currentAdmin.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM_SUPER) ||
                currentAdmin.getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM)) {
            return -1;
        }
        return -1;
    }

    /**
     * 获取所有角色
     *
     * @param merId 平台-0，商户id
     * @return List
     */
    @Override
    public List<SystemRole> getListByMerId(Integer merId) {
        LambdaQueryWrapper<SystemRole> lqw = Wrappers.lambdaQuery();
        if (merId.equals(0)) {
            lqw.eq(SystemRole::getMerId, merId);
            lqw.ne(SystemRole::getType, SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER);
        } else {
            lqw.eq(SystemRole::getMerId, merId).or().eq(SystemRole::getType, SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT_SUPER);
        }
        lqw.orderByAsc(SystemRole::getId);
        return dao.selectList(lqw);
    }

}

