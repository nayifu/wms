package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付方式响应对象
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
@ApiModel(value = "PayMethodResponse对象", description = "支付方式响应对象")
public class PayMethodResponse {

    @ApiModelProperty(value = "paypal支付状态，false为关闭")
    private Boolean paypalStatus;

    @ApiModelProperty(value = "stripe支付状态，false为关闭")
    private Boolean stripeStatus;

    @ApiModelProperty(value = "wechat支付状态，false为关闭")
    private Boolean wechatPaySwitch;

//    @ApiModelProperty(value = "stripeApiKey，SHA256加密")
//    private String stripeApiKey;
}
