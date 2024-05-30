package com.zbkj.common.request;

import com.zbkj.common.annotation.StringContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 转账申请请求对象
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
@ApiModel(value="TransferApplyRequest对象", description="转账申请请求对象")
public class TransferApplyRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "转账类型:bank-银行卡", required = true)
    @NotEmpty(message = "转账类型不能为空")
    @StringContains(limitValues = {"bank"}, message = "未知的转账类型")
    private String transferType;

    @ApiModelProperty(value = "转账金额", required = true)
    @NotNull(message = "转账金额不能为空")
    @DecimalMin(value = "0.00", message = "转账金额不能小于0.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "备注")
    private String mark;
}
