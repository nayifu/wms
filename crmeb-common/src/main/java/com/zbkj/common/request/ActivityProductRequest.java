package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 营销活动商品关联请求对象
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
@ApiModel(value="ActivityProductRequest对象", description="营销活动商品关联请求对象")
public class ActivityProductRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private Integer proId;

    @ApiModelProperty(value = "活动商品图片，大图展示类型必传")
    private String proImage;

    @ApiModelProperty(value = "商品排序", required = true)
    @NotNull(message = "商品排序不能为空")
    @Range(min = 1, max = 999, message = "商品排序范围为1~999")
    private Integer sort;
}