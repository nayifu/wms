package com.zbkj.common.request;

import com.zbkj.common.constants.RegularConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 修改商户密码请求对象
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
@ApiModel(value="MerchantUpdatePasswordRequest对象", description="修改商户密码请求对象")
public class MerchantUpdatePasswordRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商户ID")
    @NotNull(message = "商户ID不能为空")
    private Integer id;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 30 ,message = "密码长度在6-30个字符")
//    @Pattern(regexp = RegularConstants.PASSWORD, message = "密码格式错误，密码长度8-20位且至少包含大写字母、小写字母、数字或特殊符号中的任意三种")
    private String newPassword;

    @ApiModelProperty(value = "重复新密码", required = true)
    @NotBlank(message = "重复新密码不能为空")
    @Length(min = 6, max = 30 ,message = "密码长度在6-30个字符")
//    @Pattern(regexp = RegularConstants.PASSWORD, message = "密码格式错误，密码长度8-20位且至少包含大写字母、小写字母、数字或特殊符号中的任意三种")
    private String passwordAgain;

}
