package com.zbkj.common.model.system;

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
 * 通知设置表
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
@TableName("eb_system_notification")
@ApiModel(value="SystemNotification对象", description="通知设置表")
public class SystemNotification implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "标识")
    private String mark;

    @ApiModelProperty(value = "通知类型")
    private String type;

    @ApiModelProperty(value = "通知场景说明")
    private String description;

    @ApiModelProperty(value = "发送短信（0：不存在，1：开启，2：关闭）")
    private Integer isSms;

    @ApiModelProperty(value = "短信id")
    private Integer smsId;

    @ApiModelProperty(value = "发送海外短信（0：不存在，1：开启，2：关闭）")
    private Integer isOverseaSms;

    @ApiModelProperty(value = "海外短信id")
    private Integer overseaSmsId;

    @ApiModelProperty(value = "发送邮箱（0：不存在，1：开启，2：关闭）")
    private Integer isEmail;

    @ApiModelProperty(value = "邮箱id")
    private Integer emailId;

    @ApiModelProperty(value = "发送类型（1：用户，2：管理员）")
    private Integer sendType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}