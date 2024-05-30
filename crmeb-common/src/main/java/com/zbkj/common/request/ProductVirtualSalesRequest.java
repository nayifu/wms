package com.zbkj.common.request;

import com.zbkj.common.annotation.StringContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商品虚拟销量请求对象
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
@ApiModel(value="ProductVirtualSalesRequest对象", description="商品虚拟销量请求对象")
public class ProductVirtualSalesRequest implements Serializable {

    private static final long serialVersionUID = -452373239606480650L;

    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private Integer id;

    @ApiModelProperty(value = "修改类型：add,sub", required = true)
    @NotEmpty(message = "修改类型不能为空")
    @StringContains(limitValues = {"add","sub"}, message = "未知的修改类型")
    private String type;

    @ApiModelProperty(value = "变更虚拟商品数量", required = true)
    @NotNull(message = "变更虚拟商品数量不能为空")
    @Range(min = 1, max = 9999, message = "变更虚拟商品数量范围为1-9999")
    private Integer num;
}
