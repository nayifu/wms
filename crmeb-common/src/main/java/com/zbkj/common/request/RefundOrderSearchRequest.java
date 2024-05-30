package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 退款订单列表请求对象
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
@ApiModel(value="RefundOrderSearchRequest对象", description="退款订单列表请求对象")
public class RefundOrderSearchRequest implements Serializable {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "退款订单号")
    private String refundOrderNo;

    @ApiModelProperty(value = "商户订单号")
    private String merOrderNo;

    @ApiModelProperty(value = "创建时间区间")
    private String dateLimit;

    @ApiModelProperty(value = "退款状态：0:待审核 1:审核未通过 2：退款中 3:已退款 9:全部")
    @NotNull(message = "退款状态不能为空")
    private Integer refundStatus;
}
