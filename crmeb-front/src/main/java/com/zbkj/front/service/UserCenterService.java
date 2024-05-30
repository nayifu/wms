package com.zbkj.front.service;

import com.zbkj.common.response.UserCenterResponse;

import java.util.HashMap;

/**
 * UserCenterService 接口
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
public interface UserCenterService {

    /**
     * 个人中心-用户信息
     * @return UserCenterResponse
     */
    UserCenterResponse getUserCenter();

    /**
     * 获取个人中心菜单
     */
    HashMap<String, Object> getCenterMenu();
}
