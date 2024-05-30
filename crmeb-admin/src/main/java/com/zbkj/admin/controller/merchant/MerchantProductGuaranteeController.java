package com.zbkj.admin.controller.merchant;

import com.zbkj.common.model.product.ProductGuarantee;
import com.zbkj.common.request.ProductGuaranteeRequest;
import com.zbkj.common.response.CommonResult;
import com.zbkj.service.service.ProductGuaranteeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 平台端商品保障服务控制器
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
@RequestMapping("api/admin/merchant/product/guarantee")
@Api(tags = "商户端商品保障服务控制器")
public class MerchantProductGuaranteeController {

    @Autowired
    private ProductGuaranteeService guaranteeService;

    @PreAuthorize("hasAuthority('merchant:product:guarantee:list')")
    @ApiOperation(value = "保障服务列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<ProductGuarantee>> getList() {
        return CommonResult.success(guaranteeService.getList());
    }
}



