package com.zbkj.front.pc.controller;

import com.zbkj.common.response.CommonResult;
import com.zbkj.common.response.PcIndexInfoResponse;
import com.zbkj.front.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 -- 用户中心
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
@RequestMapping("api/pc/home")
@Api(tags = "PC首页控制器")
public class PcIndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 首页数据
     */
    @ApiOperation(value = "pc首页数据")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public CommonResult<PcIndexInfoResponse> getIndexInfo() {
        return CommonResult.success(indexService.getPcIndexInfo());
    }
}
