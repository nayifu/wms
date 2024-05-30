package com.zbkj.common.request;

import com.zbkj.common.constants.RegularConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 商户入驻申请请求对象
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
@ApiModel(value="MerchantSettledApplyRequest对象", description="商户入驻申请请求对象")
public class MerchantSettledApplyRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商户名称")
    @NotEmpty(message = "Merchant name cannot be empty")
    private String name;

    @ApiModelProperty(value = "商户分类ID")
    @NotNull(message = "Please select a merchant category")
    private Integer categoryId;

    @ApiModelProperty(value = "商户类型ID")
    @NotNull(message = "Please select merchant type")
    private Integer typeId;

    @ApiModelProperty(value = "商户姓名")
    @NotEmpty(message = "Merchant realName cannot be empty")
    private String realName;

    @ApiModelProperty(value = "商户邮箱")
    @NotEmpty(message = "Merchant email address cannot be empty")
    private String email;

    @ApiModelProperty(value = "商户手机号")
    private String phone;

    @ApiModelProperty(value = "手续费(%)")
    private Integer handlingFee;

    @ApiModelProperty(value = "商户关键字")
    private String keywords;

    @ApiModelProperty(value = "商户地址")
    @NotEmpty(message = "Merchant address cannot be empty")
    private String address;

    @ApiModelProperty(value = "资质图片")
    @NotEmpty(message = "Qualification picture cannot be empty")
    private String qualificationPicture;

    @ApiModelProperty(value = "验证码", required = true)
    @NotEmpty(message = "verification code must be filled")
    @Pattern(regexp = RegularConstants.VALIDATE_CODE_NUM_SIX, message = "The format of the verification code is incorrect, the verification code must be 6 digits")
    private String captcha;

}
