package com.zbkj.front.pc.controller;

import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.PcActivityInfoResponse;
import com.zbkj.service.service.MarketingActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 活动控制器
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
@RequestMapping("api/pc/activity")
@Api(tags = "PC活动控制器")
public class PcActivityController {

    @Autowired
    private MarketingActivityService activityService;

    @ApiOperation(value = "PC活动详情")
    @RequestMapping(value = "/detail/{aid}", method = RequestMethod.GET)
    public CommonResult<PcActivityInfoResponse> getDetail(@PathVariable("aid") Integer aid) {
        return CommonResult.success(activityService.getPcDetail(aid));
    }
}
