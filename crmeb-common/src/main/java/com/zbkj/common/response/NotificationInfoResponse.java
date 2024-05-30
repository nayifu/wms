package com.zbkj.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统通知详情相应对象
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
@ApiModel(value="NotificationInfoResponse对象", description="系统通知详情相应对象")
public class NotificationInfoResponse implements Serializable {

    private static final long serialVersionUID = -3214167698001861141L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "模板id(短信)")
    private String tempId;

    @ApiModelProperty(value = "模板说明(公用)")
    private String title;

    @ApiModelProperty(value = "模板编号(短信)")
    private String tempKey;

    @ApiModelProperty(value = "内容(短信)")
    private String content;

    @ApiModelProperty(value = "模板名")
    private String name;

    @ApiModelProperty(value = "状态,1-开启，2-关闭")
    private Integer status;

    @ApiModelProperty(value = "主题(邮件)")
    private String subject;

    @ApiModelProperty(value = "邮件正文(邮件)")
    private String text;

    @ApiModelProperty(value = "类型(公用)")
    private String type;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;
}
