package com.zbkj.admin.controller.platform;

import com.zbkj.common.model.merchant.MerchantApply;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.MerchantPlatformDetailResponse;
import com.zbkj.service.service.MerchantApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商户入驻控制器
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
@RequestMapping("api/admin/platform/merchant/apply")
@Api(tags = "商户入驻控制器")
public class MerchantApplyController {

    @Autowired
    private MerchantApplyService merchantApplyService;

    @PreAuthorize("hasAuthority('platform:merchant:apply:page:list')")
    @ApiOperation(value="商户入驻分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MerchantApply>> getPageList(@Validated MerchantApplySearchRequest searchRequest,
                                                               @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(merchantApplyService.getAdminPage(searchRequest, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:merchant:apply:audit')")
    @ApiOperation(value="审核")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public CommonResult<Object> audit(@RequestBody @Validated MerchantApplyAuditRequest request) {
        if (merchantApplyService.audit(request)) {
            return CommonResult.success("申请审核成功");
        }
        return CommonResult.failed("申请审核失败");
    }

    @PreAuthorize("hasAuthority('platform:merchant:apply:remark')")
    @ApiOperation(value="备注")
    @RequestMapping(value = "/remark", method = RequestMethod.POST)
    public CommonResult<Object> remark(@RequestBody @Validated MerchantApplyRemarkRequest request) {
        if (merchantApplyService.remark(request)) {
            return CommonResult.success("备注成功");
        }
        return CommonResult.failed("备注失败");
    }
}
