package com.zbkj.common.request;

import com.zbkj.common.annotation.StringContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 支付订单参数
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
@ApiModel(value = "OrderPayRequest对象", description = "订单支付")
public class OrderPayRequest {

    @ApiModelProperty(value = "订单编号")
    @NotNull(message = "Order No cannot be empty")
    private String orderNo;

    @ApiModelProperty(value = "支付类型：paypal-paypal支付，stripe-Stripe支付,wechat-微信支付")
    @NotNull(message = "Payment type cannot be empty")
    @StringContains(limitValues = {"paypal", "stripe", "wechat"}, message = "Unknown payment method")
    private String payType;

    @ApiModelProperty(value = "支付渠道：pc,mobile")
    @NotNull(message = "Pay Channel cannot be empty")
    private String payChannel;
}
