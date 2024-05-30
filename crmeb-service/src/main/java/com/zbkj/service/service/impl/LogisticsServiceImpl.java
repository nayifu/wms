package com.zbkj.service.service.impl;


import cn.hutool.core.util.StrUtil;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.alibaba.fastjson.JSONObject;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.vo.LogisticsApiResponseVo;
import com.zbkj.service.service.LogisticService;
import com.zbkj.service.service.SystemConfigService;
import com.zbkj.service.util.LogisticsApiClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
* ExpressServiceImpl 接口实现
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
@Service
public class LogisticsServiceImpl implements LogisticService {

    private static Logger logger = LoggerFactory.getLogger(LogisticsServiceImpl.class);

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 查询物流信息(阿里云全国物流查询)
     * @param expNo 快递单号（顺丰需要拼接手机号后四位，例：SF1346812059781:1029）
     * @param expCode 快递公司编号
     * @return LogisticsApiResponseVo
     */
    @Override
    public LogisticsApiResponseVo query(String expNo, String expCode) {
        String appKey = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_ALIYUN_LOGISTICS_APP_KEY);
        if (StrUtil.isBlank(appKey)) {
            throw new CrmebException("请先配置物流参数");
        }
        String appSecret = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_ALIYUN_LOGISTICS_APP_SECRET);
        if (StrUtil.isBlank(appSecret)) {
            throw new CrmebException("请先配置物流参数");
        }

        //HTTP Client init
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey(appKey);
        httpParam.setAppSecret(appSecret);
        LogisticsApiClient.getInstance().init(httpParam);
        ApiResponse response = LogisticsApiClient.getInstance().query(expNo , expCode);
        LogisticsApiClient.getInstance().shutdown();
        if (response.getCode() == 200) {// 成功
            return JSONObject.parseObject(response.getBody(), LogisticsApiResponseVo.class);
        } else {// 请求失败
            List<String> strings = Optional.ofNullable(response.getHeaders().get("x-ca-error-message")).orElse(response.getHeaders().get("X-Ca-Error-Message"));
            String error = strings.get(0);
            logger.error("Error description:{}", error);
            if (response.getCode() == 400 && error.equals("Invalid AppCode `not exists`")) {
                logger.error("全球快递查询AppCode错误");
            } else if (response.getCode() == 400 && error.equals("Invalid Url")) {
                logger.error("全球快递查询请求的 Method、Path 或者环境错误");
            } else if (response.getCode() == 400 && error.equals("Invalid Param Location")) {
                logger.error("全球快递查询参数错误");
            } else if (response.getCode() == 403 && error.equals("Unauthorized")) {
                logger.error("全球快递查询服务未被授权（或URL和Path不正确）");
            } else if (response.getCode() == 403 && error.equals("Quota Exhausted")) {
                logger.error("全球快递查询套餐包次数用完 ");
            } else {
                logger.error("全球快递查询 参数名错误 或 其他错误");
                logger.error(error);
            }
        }
        return null;
    }

}

