package com.zbkj.common.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 添加购物车参数Request对象
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
@ApiModel(value="OrderRefundApplyRequest对象", description="订单申请退款")
public class OrderRefundApplyRequest {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单号", required = true)
    @NotEmpty(message = "Order no cannot be empty")
    private String orderNo;

    @ApiModelProperty(value = "退款原因", required = true)
    @NotNull(message = "Refund reason must be filled")
    @Length(max = 255, message = "Refund reason cannot exceed 255 characters")
    private String text;

    @ApiModelProperty(value = "退款凭证图片(多个图片请用,(英文逗号)隔开)")
    @JsonProperty("refund_reason_wap_img")
    private String reasonImage;

    @ApiModelProperty(value = "备注说明")
    @JsonProperty("refund_reason_wap_explain")
    private String explain;
}
