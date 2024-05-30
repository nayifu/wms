package com.zbkj.common.request;

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
 * 修改密码请求对象
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
@ApiModel(value="UpdatePasswordRequest对象", description="修改密码请求对象")
public class UpdatePasswordRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "New password cannot be empty")
    @Pattern(regexp = RegularConstants.PASSWORD, message = "The password is 8-20 characters long and contains at least any three of uppercase letters, lowercase letters, numbers or special symbols")
    private String newPassword;

    @ApiModelProperty(value = "重复新密码", required = true)
    @NotBlank(message = "Repeat new password cannot be empty")
    @Pattern(regexp = RegularConstants.PASSWORD, message = "The password is 8-20 characters long and contains at least any three of uppercase letters, lowercase letters, numbers or special symbols")
    private String passwordAgain;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "verification code must be filled")
    @Pattern(regexp = RegularConstants.VALIDATE_CODE_NUM_SIX, message = "The format of the verification code is incorrect, the verification code must be 6 digits")
    private String captcha;


}