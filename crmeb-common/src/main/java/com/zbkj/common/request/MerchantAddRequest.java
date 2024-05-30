package com.zbkj.common.request;

import com.zbkj.common.constants.RegularConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 添加商户请求对象
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
@ApiModel(value = "MerchantAddRequest对象", description = "添加商户请求对象")
public class MerchantAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商户名称")
    @NotEmpty(message = "商户名称不能为空")
    @Length(max = 50, message = "商户名称不能超过50个字符")
    private String name;

    @ApiModelProperty(value = "商户分类ID")
    @NotNull(message = "商户分类不能为空")
    private Integer categoryId;

    @ApiModelProperty(value = "商户类型ID")
    @NotNull(message = "商户类型不能为空")
    private Integer typeId;

//    @ApiModelProperty(value = "商户账号")
//    @NotEmpty(message = "商户账号不能为空")
//    private String account;

    @ApiModelProperty(value = "登录密码,前端不传")
//    @NotEmpty(message = "登录密码不能为空")
    private String password;

    @ApiModelProperty(value = "商户姓名")
    private String realName;

    @ApiModelProperty(value = "商户邮箱")
    @NotEmpty(message = "商户邮箱不能为空")
    @Pattern(regexp = RegularConstants.EMAIL, message = "请输入正确的邮箱")
    private String email;

    @ApiModelProperty(value = "商户手机号")
    private String phone;

    @ApiModelProperty(value = "手续费(%)")
    @NotNull(message = "手续费不能为空")
    @Range(min = 0, max = 100, message = "手续费率范围为0-100")
    private Integer handlingFee;

    @ApiModelProperty(value = "商户关键字")
    private String keywords;

    @ApiModelProperty(value = "商户地址")
    private String address;

    @ApiModelProperty(value = "是否自营：0-自营，1-非自营")
    @NotNull(message = "自营开关不能为空")
    private Boolean isSelf;

    @ApiModelProperty(value = "是否推荐:0-不推荐，1-推荐")
    @NotNull(message = "推荐开关不能为空")
    private Boolean isRecommend;

    @ApiModelProperty(value = "商户开关:0-关闭，1-开启")
    @NotNull(message = "商户开关不能为空")
    private Boolean isSwitch;

    @ApiModelProperty(value = "商品审核开关:0-关闭，1-开启")
    @NotNull(message = "商品审核开关不能为空")
    private Boolean productSwitch;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序")
    @NotNull(message = "排序不能为空")
    @Range(min = 0, max = 9999, message = "排序的的范围为0-9999")
    private Integer sort;

    @ApiModelProperty(value = "资质图片")
    private String qualificationPicture;

}
