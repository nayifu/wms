package com.zbkj.common.model.stripe;

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
 * 微信回调表
 * </p>
 *
 * @author HZW
 * @since 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_stripe_callback")
@ApiModel(value="StripeCallback对象", description="微信回调表")
public class StripeCallback implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "事件类型")
    private String type;

    @ApiModelProperty(value = "ID")
    private String dataId;

    @ApiModelProperty(value = "内容")
    private String data;

    @ApiModelProperty(value = "创建日期")
    private Long created;

    @ApiModelProperty(value = "响应状态")
    private Integer responseStatus;

    @ApiModelProperty(value = "响应描述")
    private String responseDes;

    @ApiModelProperty(value = "全部内容")
    private String request;

    @ApiModelProperty(value = "创建时间")
    private Date addTime;


}
