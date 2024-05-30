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
 * 订单退款操作记录表
 * </p>
 *
 * @author HZW
 * @since 2022-03-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_order_refund_status")
@ApiModel(value="StoreOrderRefundStatus对象", description="订单退款操作记录表")
public class StoreOrderRefundStatus implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "退款订单号")
    private String refundOrderNo;

    @ApiModelProperty(value = "操作类型：apply-申请退款，fail-决绝退款，refund-退款")
    private String changeType;

    @ApiModelProperty(value = "操作备注")
    private String changeMessage;

    @ApiModelProperty(value = "操作时间")
    private Date createTime;


}
