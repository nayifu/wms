package com.zbkj.admin.controller.platform;


import com.zbkj.common.model.user.User;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.service.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 平台端用户控制器
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
@RequestMapping("api/admin/platform/user")
@Api(tags = "平台端用户控制器")
@Validated
public class PlatformUserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('platform:user:page:list')")
    @ApiOperation(value = "平台端用户分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<UserResponse>> getList(@ModelAttribute @Validated UserSearchRequest request,
                                                          @ModelAttribute PageParamRequest pageParamRequest) {
        CommonPage<UserResponse> userCommonPage = CommonPage.restPage(userService.getPlatformPage(request, pageParamRequest));
        return CommonResult.success(userCommonPage);
    }

    @PreAuthorize("hasAuthority('platform:user:update')")
    @ApiOperation(value = "修改用户信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated UserUpdateRequest userRequest) {
        if (userService.updateUser(userRequest)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('platform:user:info')")
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/info/{uid}", method = RequestMethod.GET)
    public CommonResult<User> info(@PathVariable("uid") Integer uid) {
        return CommonResult.success(userService.getInfoByUid(uid));
    }

    @PreAuthorize("hasAuthority('platform:user:expenses:record')")
    @ApiOperation(value="获取用户消费记录")
    @RequestMapping(value = "/expenses/record/{uid}", method = RequestMethod.GET)
    public CommonResult<CommonPage<ExpensesRecordResponse>> getExpensesRecord(@PathVariable("uid") Integer uid,
                                                                              @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(userService.getExpensesRecord(uid, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:user:have:coupons')")
    @ApiOperation(value="获取用户持有优惠券")
    @RequestMapping(value = "/have/coupons/{uid}", method = RequestMethod.GET)
    public CommonResult<CommonPage<UserHaveCouponResponse>> getHaveCoupons(@PathVariable("uid") Integer uid,
                                                                           @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(userService.getHaveCoupons(uid, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:user:topdetail')")
    @ApiOperation(value = "平台端用户详情页Top数据")
    @RequestMapping(value = "topdetail/{uid}", method = RequestMethod.GET)
    public CommonResult<TopDetail> topDetail (@PathVariable("uid") Integer uid) {
        return CommonResult.success(userService.getTopDetail(uid));
    }

    @PreAuthorize("hasAuthority('platform:user:group')")
    @ApiOperation(value = "用户分配分组")
    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public CommonResult<String> group(@RequestBody @Validated UserAssignGroupRequest request) {
        if (userService.group(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('platform:user:tag')")
    @ApiOperation(value = "用户分配标签")
    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    public CommonResult<String> tag(@RequestBody @Validated UserAssignTagRequest request) {
        if (userService.tag(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}



