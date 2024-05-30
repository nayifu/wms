package com.zbkj.admin.service;

import com.zbkj.common.request.LoginAdminUpdateRequest;
import com.zbkj.common.request.SystemAdminLoginRequest;
import com.zbkj.common.response.MenusResponse;
import com.zbkj.common.response.LoginAdminResponse;
import com.zbkj.common.response.SystemLoginResponse;

import java.util.List;
import java.util.Map;

/**
 * 管理端登录服务
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
public interface AdminLoginService {

    /**
     * 平台端登录
     */
    SystemLoginResponse platformLogin(SystemAdminLoginRequest request, String ip);

    /**
     * 商户端登录
     */
    SystemLoginResponse merchantLogin(SystemAdminLoginRequest request, String ip);

    /**
     * 用户登出
     */
    Boolean logout();

    /**
     * 获取登录页图片
     * @return Map
     */
    Map<String, Object> getLoginPic();

    /**
     * 获取管理员可访问目录
     * @return List<MenusResponse>
     */
    List<MenusResponse> getMenus();

    /**
     * 根据Token获取对应用户信息
     */
    LoginAdminResponse getInfoByToken();

    /**
     * 修改登录用户信息
     * @param request 请求参数
     * @return Boolean
     */
    Boolean loginAdminUpdate(LoginAdminUpdateRequest request);
}
