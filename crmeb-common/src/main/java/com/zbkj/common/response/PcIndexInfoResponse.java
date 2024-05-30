package com.zbkj.common.response;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * PC首页信息Response
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
@ApiModel(value="PcIndexInfoResponse对象", description="PC首页信息Response")
public class PcIndexInfoResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "首页banner滚动图")
    private List<HashMap<String, Object>> banner;

    @ApiModelProperty(value = "顶部logo")
    private String logoUrl;

    @ApiModelProperty(value = "当前商城支付币种")
    private String shopPayCurrency;

//    @ApiModelProperty(value = "店铺街图片")
//    private String shopStreetBack;

    @ApiModelProperty(value = "推荐商品竖图")
    private String recommendImage;

    @ApiModelProperty(value = "中部推荐banner图")
    private List<HashMap<String, Object>> bastBanner;

    @ApiModelProperty(value = "客服类型:h5,hotline,message,email")
    private String consumerType;

    @ApiModelProperty(value = "客服电话")
    private String consumerHotline;

    @ApiModelProperty(value = "客服H5链接")
    private String consumerH5Url;

    @ApiModelProperty(value = "客服Message")
    private String consumerMessage;

    @ApiModelProperty(value = "客服邮箱")
    private String consumerEmail;

    @ApiModelProperty(value = "店铺街头图")
    private String shopStreetHeaderImag;

    // 当前币种设置缺省币种
    public String getShopPayCurrency() {
        if(ObjectUtil.isEmpty(shopPayCurrency)){
            shopPayCurrency = "{\"country\":\" 美国\",\"currency\":\"美元\",\"symbol\":\"$\",\"ID\":\"256\"}";
        }
        return shopPayCurrency;
    }
}
