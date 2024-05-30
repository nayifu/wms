package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 评论添加对象
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
@ApiModel(value="StoreProductReplyAddRequest对象", description="评论添加对象")
public class StoreProductReplyAddRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商户订单编号", required = true)
    @NotEmpty(message = "Order no cannot be empty")
    private String orderNo;

    @ApiModelProperty(value = "订单详情id", required = true)
    @NotNull(message = "Order info id cannot be empty")
    private Integer orderInfoId;

    @ApiModelProperty(value = "评价星级", example = "5", required = true)
    @NotNull(message = "Rating star cannot be empty")
    @Range(min = 1, max = 5, message = "Rated 1-5 stars")
    private Integer star;

    @ApiModelProperty(value = "评论内容", required = true)
    @NotBlank(message = "Please fill in the comments")
    @Length(max = 512, message = "Comment content length cannot exceed 512 characters")
    private String comment;

    @ApiModelProperty(value = "评论图片", required = true)
    private String pics;
}
