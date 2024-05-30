package com.zbkj.common.model.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 商户信息表
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
@TableName("eb_merchant_info")
@ApiModel(value="MerchantInfo对象", description="商户信息表")
public class MerchantInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商户信息ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商户ID")
    private Integer merId;

    @ApiModelProperty(value = "转账类型:bank-银行卡")
    private String transferType;

    @ApiModelProperty(value = "转账姓名")
    private String transferName;

    @ApiModelProperty(value = "转账银行")
    private String transferBank;

    @ApiModelProperty(value = "转账银行卡号")
    private String transferBankCard;

    @ApiModelProperty(value = "警戒库存")
    private Integer alertStock;

    @ApiModelProperty(value = "客服类型：H5-H5链接、phone-电话、message-Message,email-邮箱")
    private String serviceType;

    @ApiModelProperty(value = "客服H5链接")
    private String serviceLink;

    @ApiModelProperty(value = "客服电话")
    private String servicePhone;

    @ApiModelProperty(value = "客服Message")
    private String serviceMessage;

    @ApiModelProperty(value = "客服邮箱")
    private String serviceEmail;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
