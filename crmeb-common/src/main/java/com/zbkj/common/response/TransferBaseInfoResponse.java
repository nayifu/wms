package com.zbkj.common.response;

import com.zbkj.common.annotation.StringContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商户转账申请基础信息
 *  +----------------------------------------------------------------------
 *  | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 *  +----------------------------------------------------------------------
 *  | Copyright (c) 2016~2023 https://www.crmeb.com All rights reserved.
 *  +----------------------------------------------------------------------
 *  | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 *  +----------------------------------------------------------------------
 *  | Author: CRMEB Team <admin@crmeb.com>
 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TransferBaseInfoResponse对象", description="商户转账申请基础信息")
public class TransferBaseInfoResponse implements Serializable {

    private static final long serialVersionUID = -4585094537501770138L;

    @ApiModelProperty(value = "商户保证金额")
    private BigDecimal guaranteedAmount;

    @ApiModelProperty(value = "商户每笔最小转账额度")
    private BigDecimal transferMinAmount;

    @ApiModelProperty(value = "商户每笔最高转账额度")
    private BigDecimal transferMaxAmount;

    @ApiModelProperty(value = "商户余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "商户可用转账余额")
    private BigDecimal transferBalance;

    @ApiModelProperty(value = "转账类型:bank-银行卡")
    private String transferType;

    @ApiModelProperty(value = "转账姓名")
    private String transferName;

    @ApiModelProperty(value = "转账银行")
    private String transferBank;

    @ApiModelProperty(value = "转账银行卡号")
    private String transferBankCard;
}
