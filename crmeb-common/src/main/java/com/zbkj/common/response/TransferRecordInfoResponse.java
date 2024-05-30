package com.zbkj.common.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台端转账记录详情响应对象
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
@ApiModel(value="TransferRecordInfoResponse对象", description="平台端转账记录详情响应对象")
public class TransferRecordInfoResponse implements Serializable {

    private static final long serialVersionUID = -4585094537501770138L;

    @ApiModelProperty(value = "转账id")
    private Integer id;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "转账类型:bank-银行卡")
    private String transferType;

    @ApiModelProperty(value = "转账姓名")
    private String transferName;

    @ApiModelProperty(value = "转账银行")
    private String transferBank;

    @ApiModelProperty(value = "转账银行卡号")
    private String transferBankCard;

    @ApiModelProperty(value = "转账状态：0-未到账，1-已到账")
    private Integer transferStatus;

    @ApiModelProperty(value = "转账凭证")
    private String transferProof;

    @ApiModelProperty(value = "转账时间")
    private Date transferTime;

    @ApiModelProperty(value = "审核状态：0-待审核，1-通过审核，2-审核失败")
    private Integer auditStatus;

    @ApiModelProperty(value = "拒绝原因")
    private String refusalReason;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty(value = "商户备注")
    private String mark;

    @ApiModelProperty(value = "平台备注")
    private String platformMark;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ApiModelProperty(value = "商户余额")
    private BigDecimal balance;
}
