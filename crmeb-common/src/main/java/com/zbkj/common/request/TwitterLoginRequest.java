package com.zbkj.common.request;

import com.zbkj.common.constants.RegularConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Twitter登录请求对象
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
@ApiModel(value="TwitterLoginRequest对象", description="Twitter登录请求对象")
public class TwitterLoginRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "oauthToken", required = true)
    @NotBlank(message = "oauthToken can not be empty")
    private String oauthToken;

    @ApiModelProperty(value = "oauthTokenSecret", required = true)
    @NotBlank(message = "oauthTokenSecret can not be empty")
    private String oauthTokenSecret;

    @ApiModelProperty(value = "oauthVerifier", required = true)
    @NotBlank(message = "oauthVerifier can not be empty")
    private String oauthVerifier;
}
