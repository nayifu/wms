package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单支付结果 Response
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
@ApiModel(value = "OrderPayResultResponse对象", description = "订单支付结果响应对象")
public class OrderPayResultResponse {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付状态")
    private Boolean status;

    @ApiModelProperty(value = "支付类型")
    private String payType;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "paypal支付重定向地址,wechat的H5支付重定向地址，wechat的native扫码支付图片地址")
    private String redirect;

    @ApiModelProperty(value = "stripe-client-secret")
    private String stripeClientSecret;
}
