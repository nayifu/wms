package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台端订单分页列表响应对象
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
@ApiModel(value="PlatformOrderPageResponse对象", description="平台端订单分页列表响应对象")
public class PlatformOrderPageResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商户订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String masterOrderNo;

    @ApiModelProperty(value = "收货人姓名")
    private String realName;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "支付状态")
    private Boolean paid;

    @ApiModelProperty(value = "支付方式:paypal")
    private String payType;

    @ApiModelProperty(value = "订单状态（0：待发货,1：待收货,2：已收货/待评价,3：已完成，9：已取消）")
    private Integer status;

    @ApiModelProperty(value = "退款状态：0 未退款 1 申请中 2 退款中 3 已退款")
    private Integer refundStatus;

    @ApiModelProperty(value = "用户备注")
    private String userRemark;

    @ApiModelProperty(value = "商户备注")
    private String merRemark;

    @ApiModelProperty(value = "平台备注")
    private String platformRemark;

    @ApiModelProperty(value = "用户是否删除")
    private Boolean isUserDel;

    @ApiModelProperty(value = "商户是否删除")
    private Boolean isMerchantDel;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    // 以下为商户信息
    @ApiModelProperty(value = "商户名称")
    private String merName;
}
