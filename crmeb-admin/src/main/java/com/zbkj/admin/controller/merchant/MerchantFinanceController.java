package com.zbkj.admin.controller.merchant;

import com.zbkj.admin.service.FinanceService;
import com.zbkj.common.model.bill.MerchantDailyStatement;
import com.zbkj.common.model.bill.MerchantMonthStatement;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.FundsMonitorRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.TransferApplyRequest;
import com.zbkj.common.request.TransferRecordRequest;
import com.zbkj.common.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 *  商户端财务控制器
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
@RequestMapping("api/admin/merchant/finance")
@Api(tags = "商户端财务控制器")
public class MerchantFinanceController {

    @Autowired
    private FinanceService financeService;

    @PreAuthorize("hasAuthority('merchant:finance:monitor:list')")
    @ApiOperation(value = "资金流水分页列表")
    @RequestMapping(value = "/monitor/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MonitorResponse>> getMonitorList(@Validated FundsMonitorRequest request,
                                                                    @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(financeService.getMerchantFundMonitoring(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('merchant:finance:transfer:base:info')")
    @ApiOperation(value = "获取转账申请基础信息")
    @RequestMapping(value = "/transfer/base/info", method = RequestMethod.GET)
    public CommonResult<TransferBaseInfoResponse> getTransferBaseInfo() {
        return CommonResult.success(financeService.getTransferBaseInfo());
    }

    @PreAuthorize("hasAuthority('merchant:finance:transfer:apply')")
    @ApiOperation(value = "转账申请")
    @RequestMapping(value = "/transfer/apply", method = RequestMethod.POST)
    public CommonResult<String> transferApply(@RequestBody @Validated TransferApplyRequest request) {
        if (financeService.transferApply(request)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('merchant:finance:transfer:record:page:list')")
    @ApiOperation(value = "转账记录分页列表")
    @RequestMapping(value = "/transfer/record/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<TransferRecordPageResponse>> getTransferRecordList(@Validated TransferRecordRequest request,
                                                                                      @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(financeService.getMerchantTransferRecordList(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('merchant:finance:transfer:record:info')")
    @ApiOperation(value = "转账记录详情")
    @RequestMapping(value = "/transfer/record/info/{id}", method = RequestMethod.GET)
    public CommonResult<TransferRecordMerchantInfoResponse> getTransferRecordInfo(@PathVariable Integer id) {
        return CommonResult.success(financeService.getMerchantTransferRecordInfo(id));
    }

    @PreAuthorize("hasAuthority('merchant:finance:daily:statement:page:list')")
    @ApiOperation(value = "日帐单管理分页列表")
    @RequestMapping(value = "/daily/statement/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MerchantDailyStatement>> getDailyStatementList(@RequestParam(value = "dateLimit", required = false, defaultValue = "") String dateLimit,
                                                                                  @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(financeService.getMerchantDailyStatementList(dateLimit, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('merchant:finance:month:statement:page:list')")
    @ApiOperation(value = "月帐单管理分页列表")
    @RequestMapping(value = "/month/statement/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<MerchantMonthStatement>> getMonthStatementList(@RequestParam(value = "dateLimit", required = false, defaultValue = "") String dateLimit,
                                                                                  @ModelAttribute PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(financeService.getMerchantMonthStatementList(dateLimit, pageParamRequest)));
    }
}



