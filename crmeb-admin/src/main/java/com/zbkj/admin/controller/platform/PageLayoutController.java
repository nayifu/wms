package com.zbkj.admin.controller.platform;

import com.alibaba.fastjson.JSONObject;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.PageLayoutBottomNavigationResponse;
import com.zbkj.service.service.PageLayoutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 页面布局 前端控制器
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
@RequestMapping("api/admin/platform/page/layout")
@Api(tags = "平台端页面布局控制器")
public class PageLayoutController {

    @Autowired
    private PageLayoutService pageLayoutService;

    /**
     * 页面首页
     */
    @PreAuthorize("hasAuthority('platform:page:layout:index')")
    @ApiOperation(value = "页面首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public CommonResult<HashMap<String, Object>> index() {
        return CommonResult.success(pageLayoutService.index());
    }

    /**
     * 页面首页保存
     */
    @PreAuthorize("hasAuthority('platform:page:layout:save')")
    @ApiOperation(value = "页面首页保存(不建议调用)")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<Object> save(@RequestBody JSONObject jsonObject) {
        if (pageLayoutService.save(jsonObject)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 页面首页banner保存
     */
    @PreAuthorize("hasAuthority('platform:page:layout:index:banner:save')")
    @ApiOperation(value = "页面首页banner保存")
    @RequestMapping(value = "/index/banner/save", method = RequestMethod.POST)
    public CommonResult<Object> indexBannerSave(@RequestBody JSONObject jsonObject) {
        if (pageLayoutService.indexBannerSave(jsonObject)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 页面首页menu保存
     */
    @PreAuthorize("hasAuthority('platform:page:layout:index:menu:save')")
    @ApiOperation(value = "页面首页menu保存")
    @RequestMapping(value = "/index/menu/save", method = RequestMethod.POST)
    public CommonResult<Object> indexMenuSave(@RequestBody JSONObject jsonObject) {
        if (pageLayoutService.indexMenuSave(jsonObject)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 页面首页新闻保存
     */
    @PreAuthorize("hasAuthority('platform:page:layout:index:news:save')")
    @ApiOperation(value = "页面首页新闻保存")
    @RequestMapping(value = "/index/news/save", method = RequestMethod.POST)
    public CommonResult<Object> indexNewsSave(@RequestBody JSONObject jsonObject) {
        if (pageLayoutService.indexNewsSave(jsonObject)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('platform:page:layout:index:ranking:save')")
    @ApiOperation(value = "页面首页排行入口保存")
    @RequestMapping(value = "/index/ranking/save", method = RequestMethod.POST)
    public CommonResult<Object> indexRankingSave(@RequestBody JSONObject jsonObject) {
        if (pageLayoutService.indexRankingSave(jsonObject)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 页面用户中心banner保存
     */
    @PreAuthorize("hasAuthority('platform:page:layout:user:banner:save')")
    @ApiOperation(value = "页面用户中心banner保存")
    @RequestMapping(value = "/user/banner/save", method = RequestMethod.POST)
    public CommonResult<Object> userBannerSave(@RequestBody JSONObject jsonObject) {
        if (pageLayoutService.userBannerSave(jsonObject)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 页面用户中心导航保存
     */
    @PreAuthorize("hasAuthority('platform:page:layout:user:menu:save')")
    @ApiOperation(value = "页面用户中心导航保存")
    @RequestMapping(value = "/user/menu/save", method = RequestMethod.POST)
    public CommonResult<Object> userMenuSave(@RequestBody JSONObject jsonObject) {
        if (pageLayoutService.userMenuSave(jsonObject)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }


    /**
     * 分类页配置
     */
    @PreAuthorize("hasAuthority('platform:page:layout:category:config')")
    @ApiOperation(value = "获取分类页配置")
    @RequestMapping(value = "/category/config", method = RequestMethod.GET)
    public CommonResult<Map<String, Object>> categoryConfig() {
        return CommonResult.success(pageLayoutService.getCategoryConfig());
    }

    /**
     * 分类页配置保存
     */
    @PreAuthorize("hasAuthority('platform:page:layout:category:config:save')")
    @ApiOperation(value = "分类页配置保存")
    @RequestMapping(value = "/category/config/save", method = RequestMethod.POST)
    public CommonResult<Object> categoryConfigSave(@RequestBody JSONObject jsonObject) {
        if (pageLayoutService.categoryConfigSave(jsonObject)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('platform:page:layout:bottom:navigation')")
    @ApiOperation(value = "底部导航获取")
    @RequestMapping(value = "/bottom/navigation/get", method = RequestMethod.GET)
    public CommonResult<PageLayoutBottomNavigationResponse> getBottomNavigation() {
        return CommonResult.success(pageLayoutService.getBottomNavigation());
    }

    @PreAuthorize("hasAuthority('platform:page:layout:bottom:navigation:save')")
    @ApiOperation(value = "底部导航保存")
    @RequestMapping(value = "/bottom/navigation/save", method = RequestMethod.POST)
    public CommonResult<Object> bottomNavigationSave(@RequestBody JSONObject jsonObject) {
        if (pageLayoutService.bottomNavigationSave(jsonObject)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
