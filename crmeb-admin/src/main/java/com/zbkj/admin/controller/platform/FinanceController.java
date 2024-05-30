package com.zbkj.admin.controller.platform;

import com.zbkj.admin.service.FinanceService;
import com.zbkj.common.model.bill.PlatformDailyStatement;
import com.zbkj.common.model.bill.PlatformMonthStatement;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.vo.TransferConfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *  平台端财务控制器
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
@RequestMapping("api/admin/platform/finance")
@Api(tags = "平台端财务控制器")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @PreAuthorize("hasAuthority('platform:finance:monitor:list')")
    @ApiOperation(value = "资金流水分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MonitorResponse>> getFundMonitoringList(@ModelAttribute FundsMonitorRequest request,
                                                                           @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(financeService.getFundMonitoring(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:finance:monitor:info')")
    @ApiOperation(value = "资金流水详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public CommonResult<List<MonitorInfoResponse>> getFundMonitoringInfo(@PathVariable Integer id) {
        return CommonResult.success(financeService.getFundMonitoringInfo(id));
    }

    @PreAuthorize("hasAuthority('platform:finance:transfer:config')")
    @ApiOperation(value = "获取转账设置")
    @RequestMapping(value = "/transfer/config", method = RequestMethod.GET)
    public CommonResult<TransferConfigVo> getTransferConfig() {
        return CommonResult.success(financeService.getTransferConfig());
    }

    @PreAuthorize("hasAuthority('platform:finance:transfer:config:edit')")
    @ApiOperation(value = "编辑转账设置")
    @RequestMapping(value = "/transfer/config/edit", method = RequestMethod.POST)
    public CommonResult<String> transferConfigEdit(@RequestBody @Validated TransferConfigVo request) {
        if (financeService.transferConfigEdit(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('platform:finance:transfer:record:page:list')")
    @ApiOperation(value = "转账记录分页列表")
    @RequestMapping(value = "/transfer/record/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<TransferRecordPlatformPageResponse>> getTransferRecordList(@ModelAttribute TransferRecordRequest request,
                                                                                              @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(financeService.getPlatformTransferRecordList(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:finance:transfer:record:info')")
    @ApiOperation(value = "转账记录详情")
    @RequestMapping(value = "/transfer/record/info/{id}", method = RequestMethod.GET)
    public CommonResult<TransferRecordInfoResponse> getTransferRecordInfo(@PathVariable Integer id) {
        return CommonResult.success(financeService.getPlatformTransferRecordInfo(id));
    }

    @PreAuthorize("hasAuthority('platform:finance:transfer:audit')")
    @ApiOperation(value = "转账审核")
    @RequestMapping(value = "/transfer/audit", method = RequestMethod.POST)
    public CommonResult<String> transferAudit(@RequestBody @Validated TransferAuditRequest request) {
        if (financeService.transferAudit(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('platform:finance:transfer:proof')")
    @ApiOperation(value = "转账凭证")
    @RequestMapping(value = "/transfer/proof", method = RequestMethod.POST)
    public CommonResult<String> transferProof(@RequestBody @Validated TransferProofRequest request) {
        if (financeService.transferProof(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('platform:finance:transfer:remark')")
    @ApiOperation(value = "转账备注")
    @RequestMapping(value = "/transfer/remark", method = RequestMethod.POST)
    public CommonResult<String> transferRemark(@RequestBody @Validated TransferRemarkRequest request) {
        if (financeService.transferRemark(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('platform:finance:daily:statement:page:list')")
    @ApiOperation(value = "日帐单管理分页列表")
    @RequestMapping(value = "/daily/statement/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<PlatformDailyStatement>> getDailyStatementList(@RequestParam(value = "dateLimit", required = false, defaultValue = "") String dateLimit,
                                                                                  @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(financeService.getPlatformDailyStatementList(dateLimit, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('platform:finance:month:statement:page:list')")
    @ApiOperation(value = "月帐单管理分页列表")
    @RequestMapping(value = "/month/statement/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<PlatformMonthStatement>> getMonthStatementList(@RequestParam(value = "dateLimit", required = false, defaultValue = "") String dateLimit,
                                                                                  @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(financeService.getPlatformMonthStatementList(dateLimit, pageParamRequest)));
    }
}



