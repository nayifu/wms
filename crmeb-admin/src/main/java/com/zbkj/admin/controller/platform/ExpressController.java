package com.zbkj.admin.controller.platform;

import com.zbkj.common.model.express.Express;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ExpressSearchRequest;
import com.zbkj.common.request.ExpressUpdateShowRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.ExpressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *  平台端物流公司控制器
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
@RequestMapping("api/admin/platform/express")
@Api(tags = "平台端物流公司控制器")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @PreAuthorize("hasAuthority('platform:express:page:list')")
    @ApiOperation(value = "物流公司分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiImplicitParam(name="keywords", value="搜索关键字")
    public CommonResult<CommonPage<Express>>  getList(@Validated ExpressSearchRequest request,
                                                      @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(expressService.getPlatformPage(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:express:update:show')")
    @ApiOperation(value = "修改显示状态")
    @RequestMapping(value = "/update/show", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated ExpressUpdateShowRequest expressRequest) {
        if (expressService.updateExpressShow(expressRequest)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

}



