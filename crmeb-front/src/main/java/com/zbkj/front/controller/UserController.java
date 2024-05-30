package com.zbkj.front.controller;


import com.zbkj.common.request.UpdatePasswordRequest;
import com.zbkj.common.request.UserEditRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.UserCenterResponse;
import com.zbkj.front.service.UserCenterService;
import com.zbkj.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 用户 -- 用户中心
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
@Slf4j
@RestController
@RequestMapping("api/front/user")
@Api(tags = "用户 -- 用户中心")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCenterService userCenterService;

    /**
     * 手机号修改密码
     */
    @ApiOperation(value = "手机号修改密码")
    @RequestMapping(value = "/phone/update/password", method = RequestMethod.POST)
    public CommonResult<Object> phoneUpdatePassword(@RequestBody @Validated UpdatePasswordRequest request) {
        if (userService.phoneUpdatePassword(request)) {
            return CommonResult.success("password has been updated");
        }
        return CommonResult.failed("Failed to change password");
    }

    /**
     * 邮箱修改密码
     */
    @ApiOperation(value = "邮箱修改密码")
    @RequestMapping(value = "/email/update/password", method = RequestMethod.POST)
    public CommonResult<Object> emailUpdatePassword(@RequestBody @Validated UpdatePasswordRequest request) {
        if (userService.emailUpdatePassword(request)) {
            return CommonResult.success("password has been updated");
        }
        return CommonResult.failed("Failed to change password");
    }

    /**
     * 修改个人资料
     */
    @ApiOperation(value = "修改个人资料")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonResult<Object> personInfo(@RequestBody @Validated UserEditRequest request) {
        if (userService.editUser(request)) {
            return CommonResult.success("Successfully modified");
        }
        return CommonResult.failed("fail to edit");
    }

    /**
     * 个人中心-用户信息
     */
    @ApiOperation(value = "个人中心-用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<UserCenterResponse> getUserCenter() {
        return CommonResult.success(userCenterService.getUserCenter());
    }

    /**
     * 用户中心菜单
     */
    @ApiOperation(value = "获取个人中心菜单")
    @RequestMapping(value = "/menu/user", method = RequestMethod.GET)
    public CommonResult<HashMap<String, Object>> getMenuUser() {
        return CommonResult.success(userCenterService.getCenterMenu());
    }

}



