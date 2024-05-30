package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户分配分组请求对象
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
@ApiModel(value="UserAssignGroupRequest对象", description="用户分配分组请求对象")
public class UserAssignGroupRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户uid，英文逗号分隔")
    @NotBlank(message = "用户uid不能为空")
    private String uids;

    @ApiModelProperty(value = "用户分组ID")
    @NotNull(message = "用户分组ID不能为空")
    private Integer groupId;

}
