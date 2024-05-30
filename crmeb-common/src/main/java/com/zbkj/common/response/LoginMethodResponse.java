package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * LoginMethodResponse
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
@ApiModel(value="LoginMethodResponse", description="登录方式响应数据")
public class LoginMethodResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "Twitter开关")
    private Boolean twitterOpen;

    @ApiModelProperty(value = "GooGle开关")
    private Boolean googleOpen;

    @ApiModelProperty(value = "Facebook开关")
    private Boolean facebookOpen;

//    @ApiModelProperty(value = "Twitter客户端key")
//    private String twitterConsumerKey;
//
//    @ApiModelProperty(value = "Twitter客户端secret")
//    private String twitterConsumerSecret;

    @ApiModelProperty(value = "GooGle客户端ID")
    private String googleClientId;

    @ApiModelProperty(value = "Facebook AppId")
    private String facebookAppId;

    @ApiModelProperty(value = "匿名下单开关（游客）")
    private Boolean visitorOpen;

    @ApiModelProperty(value = "协议")
    private String agreement;
}
