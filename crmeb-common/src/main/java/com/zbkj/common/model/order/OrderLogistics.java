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
import java.util.Date;

/**
 * 快递记录表
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
@TableName("eb_order_logistics")
@ApiModel(value="OrderLogistics对象", description="快递记录表")
public class OrderLogistics implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "快递单号")
    private String expNo;

    @ApiModelProperty(value = "快递公司编号")
    private String expCode;

    @ApiModelProperty(value = "快递公司名称")
    private String expName;

    @ApiModelProperty(value = "快递员 或 快递站(没有则为空)")
    private String courier;

    @ApiModelProperty(value = "快递员电话 (没有则为空)")
    private String courierPhone;

    @ApiModelProperty(value = "最后的轨迹时间")
    private String trackTime;

    @ApiModelProperty(value = "快递物流轨迹（JSON）AcceptStation-快递中转站，终点站，AcceptTime-事件时间")
    private String logisticsInfo;

    @ApiModelProperty(value = "物流状态：-1：单号或快递公司代码错误, 0：暂无轨迹， 1：快递收件(揽件)，2：在途中,3：签收,4：问题件 5.疑难件 6.退件签收")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "提示信息")
    private String reason;


}
