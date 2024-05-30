package com.zbkj.front.controller;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ActivityListResponse;
import com.zbkj.common.response.CommonResult;
import com.zbkj.common.vo.ActivityProductInfoVo;
import com.zbkj.service.service.MarketingActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
@RequestMapping("api/front/activity")
@Api(tags = "活动控制器")
public class ActivityController {

    @Autowired
    private MarketingActivityService activityService;

    @ApiOperation(value = "首页活动列表")
    @RequestMapping(value = "/index/list", method = RequestMethod.GET)
    public CommonResult<List<ActivityListResponse>> getIndexList() {
        return CommonResult.success(activityService.getIndexList());
    }

    @ApiOperation(value = "活动商品分页列表")
    @RequestMapping(value = "/list/{aid}", method = RequestMethod.GET)
    public CommonResult<PageInfo<ActivityProductInfoVo>> getActivityProductList(@PathVariable(name="aid") Integer aid,
                                                                                @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(activityService.getH5List(aid, pageParamRequest));
    }

}
