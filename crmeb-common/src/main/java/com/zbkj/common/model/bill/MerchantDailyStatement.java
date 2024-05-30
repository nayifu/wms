package com.zbkj.common.model.bill;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商户日帐单表
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
@TableName("eb_merchant_daily_statement")
@ApiModel(value="MerchantDailyStatement对象", description="商户日帐单表")
public class MerchantDailyStatement implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "帐单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "订单收入金额")
    private BigDecimal orderIncomeAmount;

    @ApiModelProperty(value = "订单支付总金额")
    private BigDecimal orderPayAmount;

    @ApiModelProperty(value = "订单支付笔数")
    private Integer orderNum;

    @ApiModelProperty(value = "平台手续费")
    private BigDecimal handlingFee;

    @ApiModelProperty(value = "支出总金额")
    private BigDecimal payoutAmount;

    @ApiModelProperty(value = "支出笔数")
    private Integer payoutNum;

    @ApiModelProperty(value = "商户退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "退款笔数")
    private Integer refundNum;

    @ApiModelProperty(value = "商户日收支")
    private BigDecimal incomeExpenditure;

    @ApiModelProperty(value = "日期：年-月-日")
    private String dataDate;


}
