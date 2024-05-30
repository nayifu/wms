package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.SystemAdminAddRequest;
import com.zbkj.common.request.SystemAdminRequest;
import com.zbkj.common.request.SystemAdminUpdateRequest;
import com.zbkj.common.response.SystemAdminResponse;

import java.util.List;
import java.util.Map;

/**
 * SystemAdminService 接口
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
public interface SystemAdminService extends IService<SystemAdmin> {

    /**
     * 后台管理员列表
     * @param request 请求参数
     * @param pageParamRequest 分页参数
     * @return List
     */
    List<SystemAdminResponse> getList(SystemAdminRequest request, PageParamRequest pageParamRequest);

    /**
     * 新增管理员
     */
    Boolean saveAdmin(SystemAdminAddRequest systemAdminAddRequest);

    /**
     * 管理员名称唯一校验
     * @param account 管理员账号
     * @return Boolean
     */
    Boolean checkAccount(String account);

    /**
     * 更新管理员
     */
    Boolean updateAdmin(SystemAdminUpdateRequest systemAdminRequest);

    /**
     * 删除管理员
     * @param id 管理员id
     * @return 删除结果
     */
    Boolean removeAdmin(Integer id);

    /**
     * 修改后台管理员状态
     * @param id 管理员id
     * @param status 状态
     * @return Boolean
     */
    Boolean updateStatus(Integer id, Boolean status);

    /**
     * 管理员详情
     * @param id 管理员id
     * @return SystemAdmin
     */
    SystemAdmin getDetail(Integer id);

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @param type 用户类型
     * @return SystemAdmin
     */
    SystemAdmin selectUserByUserNameAndType(String username, Integer type);

    /**
     * 获取管理员名称map
     * @param idList id列表
     * @return Map
     */
    Map<Integer, String> getNameMapByIdList(List<Integer> idList);

    /**
     * 通过账号获取用户
     * @param account 账号
     * @param type 用户类型
     * @return SystemAdmin
     */
    SystemAdmin getByAccountAndType(String account, Integer type);

    /**
     * 是否存在角色
     * @param roleId 角色id
     * @return Boolean
     */
    Boolean isExistRole(Integer roleId);
}
