package com.zbkj.common.request;

import com.zbkj.common.annotation.StringContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 商户端用户查询请求对象
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
@ApiModel(value="MerchantUserSearchRequest对象", description="商户端用户查询请求对象")
public class MerchantUserSearchRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "关键字：清输入姓名")
    private String keywords;

    @ApiModelProperty(value = "邮箱（全匹配）")
    private String email;

//    @ApiModelProperty(value = "用户标签")
//    private Integer tagId;

    @ApiModelProperty(value = "用户类型，facebook,twitter,google,email,phone,visitor", allowableValues = "range[facebook,twitter,google,email,phone,visitor]")
    @StringContains(limitValues = {"facebook","twitter","google","email","phone","visitor"}, message = "请选择正确的用户登录类型")
    private String userType;

    @ApiModelProperty(value = "性别，0未知，1男，2女，3保密")
    private String sex;

    @ApiModelProperty(value = "关注时间")
    private String dateLimit;
}
