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
 * 邮箱登录请求对象
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
@ApiModel(value="EmailLoginRequest对象", description="邮箱登录请求对象")
public class EmailLoginRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "邮箱", required = true)
    @NotBlank(message = "E-mail can not be empty")
    @Pattern(regexp = RegularConstants.EMAIL, message = "Email format error")
    private String email;

    @ApiModelProperty(value = "密码", required = true, example = "1~[6,18]")
//    @Pattern(regexp = RegularConstants.PASSWORD, message = "The password is 8-20 characters long and contains at least any three of uppercase letters, lowercase letters, numbers or special symbols")
    @NotBlank(message = "password can not be blank")
    private String password;
}
