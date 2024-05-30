package com.zbkj.admin.controller.merchant;

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

import java.util.List;


/**
 * 商户端物流公司控制器
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
@RequestMapping("api/admin/merchant/express")
@Api(tags = "商户端物流公司控制器")
public class MerchantExpressController {

    @Autowired
    private ExpressService expressService;

    /**
     * 查询全部物流公司
     */
    @PreAuthorize("hasAuthority('merchant:express:all')")
    @ApiOperation(value = "查询全部物流公司")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public CommonResult<List<Express>> all() {
        return CommonResult.success(expressService.findAll());
    }

}



