package com.zbkj.admin.controller.platform;

import com.zbkj.common.model.system.SystemNotification;
import com.zbkj.common.request.NotificationInfoRequest;
import com.zbkj.common.request.NotificationSearchRequest;
import com.zbkj.common.request.NotificationUpdateRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.NotificationInfoResponse;
import com.zbkj.service.service.SystemNotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台端通知设置前端控制器
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
@Slf4j
@RestController
@RequestMapping("api/admin/platform/notification")
@Api(tags = "平台端通知设置前端控制器") //配合swagger使用
public class SystemNotificationController {

    @Autowired
    private SystemNotificationService systemNotificationService;

    /**
     * 系统通知列表
     * @param request ExpressSearchRequest 搜索条件
     */
    @PreAuthorize("hasAuthority('platform:system:notification:list')")
    @ApiOperation(value = "系统通知列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<SystemNotification>> getList(@Validated NotificationSearchRequest request) {
        return CommonResult.success(systemNotificationService.getList(request));
    }

    /**
     * 发送短信开关
     */
    @PreAuthorize("hasAuthority('platform:system:notification:sms:switch')")
    @ApiOperation(value = "发送短信开关")
    @RequestMapping(value = "/sms/switch/{id}", method = RequestMethod.POST)
    public CommonResult<Object> smsSwitch(@PathVariable Integer id) {
        if (systemNotificationService.smsSwitch(id)) {
            return CommonResult.success("更改成功");
        }
        return CommonResult.failed("更改失败");
    }

    /**
     * 发送海外短信开关
     */
    @PreAuthorize("hasAuthority('platform:system:notification:oversea:sms:switch')")
    @ApiOperation(value = "发送海外短信开关")
    @RequestMapping(value = "/oversea/sms/switch/{id}", method = RequestMethod.POST)
    public CommonResult<Object> overseaSmsSwitch(@PathVariable Integer id) {
        if (systemNotificationService.overseaSmsSwitch(id)) {
            return CommonResult.success("更改成功");
        }
        return CommonResult.failed("更改失败");
    }

    /**
     * 发送邮箱开关
     */
    @PreAuthorize("hasAuthority('platform:system:notification:email:switch')")
    @ApiOperation(value = "发送邮箱开关")
    @RequestMapping(value = "/email/switch/{id}", method = RequestMethod.POST)
    public CommonResult<Object> emailSwitch(@PathVariable Integer id) {
        if (systemNotificationService.emailSwitch(id)) {
            return CommonResult.success("更改成功");
        }
        return CommonResult.failed("更改失败");
    }

    /**
     * 通知详情
     */
    @PreAuthorize("hasAuthority('platform:system:notification:detail')")
    @ApiOperation(value = "通知详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public CommonResult<NotificationInfoResponse> info(@Validated NotificationInfoRequest request) {
        return CommonResult.success(systemNotificationService.getDetail(request));
    }

    /**
     * 修改通知
     */
    @PreAuthorize("hasAuthority('platform:system:notification:update')")
    @ApiOperation(value = "修改通知")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<Object> update(@Validated @RequestBody NotificationUpdateRequest request) {
        if (systemNotificationService.modify(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}



