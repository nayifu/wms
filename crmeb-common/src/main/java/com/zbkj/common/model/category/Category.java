package com.zbkj.common.model.category;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分类表
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
@TableName("eb_category")
@ApiModel(value="Category对象", description="分类表")
public class Category implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父级ID")
    private Integer pid;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "类型ID | 类型，2 附件分类，4 设置分类，6 配置分类 ")
    private Integer type;

    @ApiModelProperty(value = "地址")
    private String url;

    @ApiModelProperty(value = "扩展字段")
    private String extra;

    @ApiModelProperty(value = "状态,1正常，0失效")
    private Boolean status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "分类所属：-1 - 平台，其他-商户")
    private Integer owner;


}
