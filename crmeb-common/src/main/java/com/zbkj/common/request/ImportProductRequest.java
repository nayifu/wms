package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 导入商品请求对象
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
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ImportProductRequest对象", description = "导入商品请求对象")
public class ImportProductRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "导入平台1=淘宝，2=京东，3=苏宁，4=拼多多, 5=天猫")
    @NotNull(message = "请选择商品平台")
    @Range(min = 1, max = 5, message = "未知的商品平台")
    private Integer form;

    @ApiModelProperty(value = "导入商品的URL")
    @NotBlank(message = "请填写导入商品的URL")
    private String url;

}
