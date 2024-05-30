package com.zbkj.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zbkj.common.constants.MenuConstants;
import com.zbkj.common.constants.SystemRoleConstants;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.model.system.SystemMenu;
import com.zbkj.common.model.system.SystemPermissions;
import com.zbkj.common.vo.LoginUserVo;
import com.zbkj.service.service.SystemAdminService;
import com.zbkj.service.service.SystemMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户验证处理
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Autowired
    private SystemAdminService systemAdminService;

    @Autowired
    private SystemMenuService systemMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String name = username.substring(0, username.length() - 1);
        Integer type = Integer.valueOf(username.substring(username.length() - 1));

        SystemAdmin user = systemAdminService.selectUserByUserNameAndType(name, type);
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", name);
            throw new UsernameNotFoundException("登录用户：" + name + " 不存在");
        } else if (!user.getStatus()) {
            log.info("登录用户：{} 已被停用.", name);
            throw new UsernameNotFoundException("对不起，您的账号：" + name + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SystemAdmin user) {
        List<Integer> roles = Stream.of(user.getRoles().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        List<SystemMenu> menuList;
        if (roles.contains(1) || roles.contains("[1]")) {// 超级管理员
            // 获取全部权限
            menuList = systemMenuService.getAllPermissions(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM);
        } else if (roles.contains(2)) {
            menuList = systemMenuService.getAllPermissions(SystemRoleConstants.SYSTEM_ROLE_TYPE_MERCHANT);
        } else {
            menuList = systemMenuService.findPermissionByUserId(user.getId());
        }
        menuList = menuList.stream().filter(e -> StrUtil.isNotEmpty(e.getPerms())).collect(Collectors.toList());
        List<SystemPermissions> permissionsList = menuList.stream().map(e -> {
            SystemPermissions permissions = new SystemPermissions();
            permissions.setId(e.getId());
            permissions.setPid(e.getPid());
            permissions.setName(e.getName());
            permissions.setPath(e.getPerms());
            permissions.setSort(e.getSort());
            return permissions;
        }).collect(Collectors.toList());
        return new LoginUserVo(user, permissionsList);
    }


}
