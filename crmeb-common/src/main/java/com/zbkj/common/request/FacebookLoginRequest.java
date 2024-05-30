package com.zbkj.common.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zbkj.common.constants.RegularConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Facebook登录请求对象
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
@ApiModel(value="FacebookLoginRequest对象", description="Facebook登录请求对象")
public class FacebookLoginRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户标识", required = true)
    @NotBlank(message = "facebook id is not null")
    private String id;

    @ApiModelProperty(value = "用户姓名", required = true)
    @NotBlank(message = "facebook name is not null")
    private String name;

    @ApiModelProperty(value = "头像", required = true)
    @NotBlank(message = "facebook picture is not null")
    private String picture;

    @ApiModelProperty(value = "邮箱")
    @Pattern(regexp = RegularConstants.EMAIL, message = "Email format error")
    private String email;
}
