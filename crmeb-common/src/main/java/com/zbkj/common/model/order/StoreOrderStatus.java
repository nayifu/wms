package com.zbkj.common.model.order;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单操作记录表
 * </p>
 *
 * @author HZW
 * @since 2022-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_order_status")
@ApiModel(value="StoreOrderStatus对象", description="订单操作记录表")
public class StoreOrderStatus implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商户订单号")
    private String merOrderNo;

    @ApiModelProperty(value = "操作类型：create-订单生成，cancel-订单取消，pay-支付，express-发货，receipt-收货，complete-完成,refund-退款,delete-删除")
    private String changeType;

    @ApiModelProperty(value = "操作备注")
    private String changeMessage;

    @ApiModelProperty(value = "操作时间")
    private Date createTime;


}
