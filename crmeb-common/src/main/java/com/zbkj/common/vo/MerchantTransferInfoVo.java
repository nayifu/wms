package com.zbkj.common.vo;

import com.zbkj.common.annotation.StringContains;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 商户转账信息Vo对象
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
@ApiModel(value="MerchantTransferInfoVo对象", description="商户转账信息Vo对象")
public class MerchantTransferInfoVo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "转账类型:bank-银行卡", required = true)
    @NotEmpty(message = "转账类型不能为空")
    @StringContains(limitValues = {"bank"}, message = "未知的转账类型")
    private String transferType;

    @ApiModelProperty(value = "转账姓名", required = true)
    @NotEmpty(message = "转账姓名不能为空")
    private String transferName;

    @ApiModelProperty(value = "转账银行", required = true)
    @NotEmpty(message = "转账银行不能为空")
    private String transferBank;

    @ApiModelProperty(value = "转账银行卡号", required = true)
    @NotEmpty(message = "转账银行卡号不能为空")
    private String transferBankCard;
}
