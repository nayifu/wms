package com.zbkj.admin.controller.platform;

import com.zbkj.common.model.activity.MarketingActivity;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ActivityRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.MarketingActivityInfoResponse;
import com.zbkj.service.service.MarketingActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 平台端营销活动控制器
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
@RequestMapping("api/admin/platform/activity")
@Api(tags = "平台端营销活动控制器") //配合swagger使用
public class MarketingActivityController {

    @Autowired
    private MarketingActivityService activityService;

    @PreAuthorize("hasAuthority('platform:activity:page:list')")
    @ApiOperation(value="活动分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MarketingActivity>> getList(@Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(activityService.getAdminPage(pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:activity:add')")
    @ApiOperation(value="添加活动")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<Object> add(@RequestBody @Validated ActivityRequest request) {
        if (activityService.add(request)) {
            return CommonResult.success("添加活动成功");
        }
        return CommonResult.failed("添加活动失败");
    }

    @PreAuthorize("hasAuthority('platform:activity:delete')")
    @ApiOperation(value="删除活动")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<Object> delete(@PathVariable("id") Integer id) {
        if (activityService.delete(id)) {
            return CommonResult.success("删除活动成功");
        }
        return CommonResult.failed("删除活动失败");
    }


    @PreAuthorize("hasAuthority('platform:activity:update')")
    @ApiOperation(value="编辑活动")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<Object> update(@RequestBody @Validated ActivityRequest request) {
        if (activityService.edit(request)) {
            return CommonResult.success("编辑活动成功");
        }
        return CommonResult.failed("编辑活动失败");
    }

    @PreAuthorize("hasAuthority('platform:activity:info')")
    @ApiOperation(value="活动详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public CommonResult<MarketingActivityInfoResponse> info(@PathVariable Integer id) {
        return CommonResult.success(activityService.getInfo(id));
    }

    @PreAuthorize("hasAuthority('platform:activity:switch')")
    @ApiOperation(value="活动开关")
    @RequestMapping(value = "/switch/{id}", method = RequestMethod.POST)
    public CommonResult<Object> updateSwitch(@PathVariable("id") Integer id) {
        if (activityService.updateSwitch(id)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
