package com.zbkj.common.response;

import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.vo.SimpleProductVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 优惠券记录表
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
@ApiModel(value="StoreCouponInfoResponse对象", description="优惠券详情")
public class StoreCouponInfoResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "优惠券表ID")
    private Integer id;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "兑换的优惠券面值")
    private BigDecimal money;

    @ApiModelProperty(value = "是否限量, 默认0 否， 1是")
    private Boolean isLimited;

    @ApiModelProperty(value = "发放总数")
    private Integer total;

    @ApiModelProperty(value = "使用类型 1-商家券, 2-商品券, 3-平台券")
    private Integer useType;

    @ApiModelProperty(value = "主键id 商品id")
    private String primaryKey;

    @ApiModelProperty(value = "最低消费，0代表不限制")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "是否固定领取时间， 默认0 否， 1是")
    private Boolean isForever;

    @ApiModelProperty(value = "可领取开始时间")
    private Date receiveStartTime;

    @ApiModelProperty(value = "可领取结束时间")
    private Date receiveEndTime;

    @ApiModelProperty(value = "请设置是否固定使用时间, 默认0 否， 1是")
    private Boolean isFixedTime;

    @ApiModelProperty(value = "可使用时间范围 开始时间")
    private Date useStartTime;

    @ApiModelProperty(value = "可使用时间范围 结束时间")
    private Date useEndTime;

    @ApiModelProperty(value = "天数")
    @Max(value = 999, message = "天数不能超过999天")
    private Integer day;

    @ApiModelProperty(value = "优惠券类型 1 手动领取, 2 新人券, 3 赠送券")
    private Integer type;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态（0：关闭，1：开启）")
    private Boolean status;

    @ApiModelProperty(value = "商品信息")
    private List<SimpleProductVo> productList;

}
