package com.zbkj.front.pc.controller;

import com.zbkj.common.model.user.UserAddress;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 用户地址 前端控制器
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
@RequestMapping("api/pc/address")
@Api(tags = "PC用户地址管理器")
public class PcUserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @ApiOperation(value = "全部地址列表")
    @RequestMapping(value = "/all/list", method = RequestMethod.GET)
    public CommonResult<List<UserAddress>> getAllList() {
        return CommonResult.success(userAddressService.getAllList());
    }

}



