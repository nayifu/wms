package com.zbkj.admin.controller.platform;

import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.MerchantHeaderNumResponse;
import com.zbkj.common.response.MerchantPageResponse;
import com.zbkj.common.response.MerchantPlatformDetailResponse;
import com.zbkj.service.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商户控制器
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
@RequestMapping("api/admin/platform/merchant")
@Api(tags = "平台端商户控制器")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PreAuthorize("hasAuthority('platform:merchant:page:list')")
    @ApiOperation(value = "商户分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MerchantPageResponse>> getPageList(@Validated MerchantSearchRequest searchRequest,
                                                                      @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(merchantService.getAdminPage(searchRequest, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:merchant:list:header:num')")
    @ApiOperation(value = "商户分页列表表头数量")
    @RequestMapping(value = "/list/header/num", method = RequestMethod.GET)
    public CommonResult<MerchantHeaderNumResponse> getListHeaderNum() {
        return CommonResult.success(merchantService.getListHeaderNum());
    }

    @PreAuthorize("hasAuthority('platform:merchant:add')")
    @ApiOperation(value = "添加商户")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<Object> add(@RequestBody @Validated MerchantAddRequest request) {
        if (merchantService.add(request)) {
            return CommonResult.success("添加商户成功");
        }
        return CommonResult.failed("添加商户失败");
    }

    @PreAuthorize("hasAuthority('platform:merchant:update')")
    @ApiOperation(value = "编辑商户")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<Object> update(@RequestBody @Validated MerchantUpdateRequest request) {
        if (merchantService.edit(request)) {
            return CommonResult.success("编辑商户成功");
        }
        return CommonResult.failed("编辑商户失败");
    }

    @PreAuthorize("hasAuthority('platform:merchant:update:password')")
    @ApiOperation(value = "修改商户密码")
    @RequestMapping(value = "/update/password", method = RequestMethod.POST)
    public CommonResult<Object> updatePassword(@RequestBody @Validated MerchantUpdatePasswordRequest request) {
        if (merchantService.updatePassword(request)) {
            return CommonResult.success("修改商户密码成功");
        }
        return CommonResult.failed("修改商户密码失败");
    }

    @PreAuthorize("hasAuthority('platform:merchant:copy:prodcut:num')")
    @ApiOperation(value = "修改复制商品数量")
    @RequestMapping(value = "/update/copy/product/num", method = RequestMethod.POST)
    public CommonResult<Object> updateCopyProductNum(@RequestBody @Validated MerchantUpdateProductNumRequest request) {
        if (merchantService.updateCopyProductNum(request)) {
            return CommonResult.success("修改复制商品数量成功");
        }
        return CommonResult.failed("修改复制商品数量失败");
    }

    @PreAuthorize("hasAuthority('platform:merchant:detail')")
    @ApiOperation(value = "商户详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public CommonResult<MerchantPlatformDetailResponse> getDetail(@PathVariable("id") Integer id) {
        return CommonResult.success(merchantService.getPlatformDetail(id));
    }

    @PreAuthorize("hasAuthority('platform:merchant:recommend:switch')")
    @ApiOperation(value = "推荐开关")
    @RequestMapping(value = "/recommend/switch/{id}", method = RequestMethod.POST)
    public CommonResult<Object> recommendSwitch(@PathVariable("id") Integer id) {
        if (merchantService.recommendSwitch(id)) {
            return CommonResult.success("切换商户推荐开关成功");
        }
        return CommonResult.failed("切换商户推荐开关失败");
    }

    @PreAuthorize("hasAuthority('platform:merchant:close')")
    @ApiOperation(value = "关闭商户")
    @RequestMapping(value = "/close/{id}", method = RequestMethod.POST)
    public CommonResult<Object> close(@PathVariable("id") Integer id) {
        if (merchantService.close(id)) {
            return CommonResult.success("关闭商户成功");
        }
        return CommonResult.failed("关闭商户失败");
    }

    @PreAuthorize("hasAuthority('platform:merchant:open')")
    @ApiOperation(value = "开启商户")
    @RequestMapping(value = "/open/{id}", method = RequestMethod.POST)
    public CommonResult<Object> open(@PathVariable("id") Integer id) {
        if (merchantService.open(id)) {
            return CommonResult.success("开启商户成功");
        }
        return CommonResult.failed("开启商户失败");
    }
}
