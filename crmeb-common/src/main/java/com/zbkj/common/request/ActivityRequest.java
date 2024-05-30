package com.zbkj.common.request;

import com.zbkj.common.model.activity.ActivityProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 营销活动请求对象
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
@ApiModel(value="ActivityRequest对象", description="营销活动请求对象")
public class ActivityRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "活动ID,添加时不填，修改时必填")
    private Integer id;

    @ApiModelProperty(value = "活动Banner")
    private String banner;

    @ApiModelProperty(value = "活动简介", required = true)
    @NotEmpty(message = "活动简介不能为空")
    @Length(min = 1, max = 255, message = "活动简介长度不能超过255个字符")
    private String instruction;

    @ApiModelProperty(value = "活动名称", required = true)
    @NotEmpty(message = "活动名称不能为空")
    @Length(min = 1, max = 50, message = "活动名称长度不能超过50个字符")
    private String name;

    @ApiModelProperty(value = "活动展示类型：1-轮播列表，2-大小格，3-大图模式")
    @NotNull(message = "活动展示类型不能为空")
    @Range(min = 1, max = 3, message = "未知的活动展示类型")
    private Integer type;

    @ApiModelProperty(value = "排序", required = true)
    @NotNull(message = "排序不能为空")
    @Range(min = 1, max = 999, message = "排序范围为1~999")
    private Integer sort;

    @ApiModelProperty(value = "商品列表")
    @Valid
    private List<ActivityProductRequest> proList;
}
