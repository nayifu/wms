package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户编辑Request
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
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="UserEditRequest对象", description="修改个人资料")
public class UserEditRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户昵称")
    @NotBlank(message = "Please fill in user nickname")
    @Length(max = 255, message = "User nickname cannot exceed 255 characters")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    @NotBlank(message = "Please upload user avatar")
    private String avatar;
}