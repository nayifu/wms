package com.zbkj.common.request;

import com.zbkj.common.constants.RegularConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 新增用户地址对象
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
@ApiModel(value="UserAddressRequest对象", description="新增用户地址对象")
public class UserAddressRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户地址id")
    private Integer id;

    @ApiModelProperty(value = "收货人姓名", required = true)
    @NotBlank(message = "Consignee name cannot be empty")
    @Length(max = 64, message = "Consignee name cannot exceed 64 characters")
    private String realName;

    @ApiModelProperty(value = "收货人邮箱", required = true)
    @NotBlank(message = "E-mail can not be empty")
    private String email;

    @ApiModelProperty(value = "国家", required = true)
    @NotBlank(message = "country cannot be empty")
    private String country;

    @ApiModelProperty(value = "收货人详细地址", required = true)
    @NotBlank(message = "Consignee's detailed address cannot be empty")
    @Length(max = 500, message = "Consignee's full address cannot exceed 500 characters")
    private String detail;

    @ApiModelProperty(value = "邮编", required = true)
    private Integer postCode;

    @ApiModelProperty(value = "国标区号", required = true)
    private String countryCode;

    @ApiModelProperty(value = "收货人电话", required = true)
    private String phone;

    @ApiModelProperty(value = "是否默认", example = "false", required = true)
    private Boolean isDefault;

}
