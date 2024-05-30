package com.zbkj.front.service.impl;

import com.zbkj.common.constants.VisitRecordConstants;
import com.zbkj.common.response.UserCenterResponse;
import com.zbkj.front.service.AsyncService;
import com.zbkj.front.service.UserCenterService;
import com.zbkj.service.service.SystemGroupDataService;
import com.zbkj.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * UserCenterService 接口实现类
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
public class UserCenterServiceImpl implements UserCenterService {

    @Autowired
    private UserService userService;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private SystemGroupDataService systemGroupDataService;

    /**
     * 个人中心-用户信息
     * @return UserCenterResponse
     */
    @Override
    public UserCenterResponse getUserCenter() {
        UserCenterResponse userCenter = userService.getUserCenter();
        // 保存用户访问记录
        asyncService.saveUserVisit(userService.getUserId(), VisitRecordConstants.VISIT_TYPE_CENTER);
        return userCenter;
    }

    /**
     * 获取个人中心菜单
     */
    @Override
    public HashMap<String, Object> getCenterMenu() {
        return systemGroupDataService.getMenuUser();
    }
}
