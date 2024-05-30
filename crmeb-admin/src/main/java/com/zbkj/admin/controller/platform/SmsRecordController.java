package com.zbkj.admin.controller.platform;

import com.zbkj.common.model.sms.SmsRecord;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 短信记录控制器
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
@RequestMapping("api/admin/platform/sms")
@Api(tags = "平台端短信记录控制器")
public class SmsRecordController {

    @Autowired
    private SmsService smsService;

    /**
     * 短信发送记录
     */
    @PreAuthorize("hasAuthority('platform:sms:recored')")
    @ApiOperation(value = "短信发送记录")
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public CommonResult<CommonPage<SmsRecord>> record(@Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(smsService.record(pageParamRequest)));
    }

}


