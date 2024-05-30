package com.zbkj.admin.controller.platform;

import com.zbkj.common.model.system.SystemMenu;
import com.zbkj.common.request.SystemMenuRequest;
import com.zbkj.common.request.SystemMenuSearchRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.SystemMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 平台端商户菜单控制器
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
@RequestMapping("api/admin/platform/merchant/menu")
@Api(tags = "平台端商户菜单控制器")
public class PlatformMerchantMenuController {

    @Autowired
    private SystemMenuService systemMenuService;

    /**
     * 菜单列表
     * @param request 搜索条件
     */
    @PreAuthorize("hasAuthority('platform:merchant:menu:list')")
    @ApiOperation(value = "菜单列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<SystemMenu>> getList(@Validated SystemMenuSearchRequest request) {
        return CommonResult.success(systemMenuService.getMerchantList(request));
    }

    /**
     * 新增菜单
     * @param systemMenuRequest 新增菜单
     */
    @PreAuthorize("hasAuthority('platform:merchant:menu:add')")
    @ApiOperation(value = "新增菜单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<String> add(@RequestBody @Validated SystemMenuRequest systemMenuRequest) {
        if (systemMenuService.addMerchantMenu(systemMenuRequest)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 删除菜单
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('platform:merchant:menu:delete')")
    @ApiOperation(value = "删除菜单")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<String> delete(@PathVariable(value = "id") Integer id) {
        if (systemMenuService.deleteMerchantMenu(id)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("hasAuthority('platform:merchant:menu:update')")
    @ApiOperation(value = "修改菜单")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated SystemMenuRequest systemMenuRequest) {
        if (systemMenuService.editMerchantMenu(systemMenuRequest)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 菜单详情
     */
    @PreAuthorize("hasAuthority('platform:merchant:menu:info')")
    @ApiOperation(value = "菜单详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public CommonResult<SystemMenu> info(@PathVariable(value = "id") Integer id) {
        return CommonResult.success(systemMenuService.getInfo(id));
   }

    /**
     * 修改菜单显示状态
     */
    @PreAuthorize("hasAuthority('platform:merchant:menu:show:status')")
    @ApiOperation(value = "修改菜单显示状态")
    @RequestMapping(value = "/update/show/{id}", method = RequestMethod.POST)
    public CommonResult<Object> updateShowStatus(@PathVariable(value = "id") Integer id) {
        if (systemMenuService.updateMerchantShowStatus(id)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }
}



