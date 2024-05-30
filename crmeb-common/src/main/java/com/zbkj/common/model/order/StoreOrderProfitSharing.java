package com.zbkj.common.model.order;

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
import java.util.Date;

/**
 * <p>
 * 订单分账表
 * </p>
 *
 * @author HZW
 * @since 2022-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_order_profit_sharing")
@ApiModel(value="StoreOrderProfitSharing对象", description="订单分账表")
public class StoreOrderProfitSharing implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "分账id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商户订单号")
    private String merOrderNo;

    @ApiModelProperty(value = "商户ID")
    private Integer merId;

    @ApiModelProperty(value = "分账金额")
    private BigDecimal profitSharingPrice;

    @ApiModelProperty(value = "分账给商户金额")
    private BigDecimal profitSharingMerPrice;

    @ApiModelProperty(value = "分账时间")
    private Date profitSharingTime;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal profitSharingRefund;

    @ApiModelProperty(value = "0:未分账 1:已分账 2:已退款")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
