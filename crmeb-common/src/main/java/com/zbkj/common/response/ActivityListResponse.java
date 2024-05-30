package com.zbkj.common.response;

import com.zbkj.common.vo.ActivityProductInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * H5活动列表响应对象
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
@ApiModel(value="ActivityListResponse对象", description="H5活动列表响应对象")
public class ActivityListResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "活动ID")
    private Integer id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动展示类型：1-轮播列表，2-大小格，3-大图模式")
    private Integer type;

    @ApiModelProperty(value = "商品列表")
    private List<ActivityProductInfoVo> productList;

}
