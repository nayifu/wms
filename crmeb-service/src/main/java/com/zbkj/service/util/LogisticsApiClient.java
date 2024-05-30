package com.zbkj.service.util;

import com.alibaba.cloudapi.sdk.client.ApacheHttpClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.ParamPosition;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *  阿里全球快递查询客户端
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
public class LogisticsApiClient extends ApacheHttpClient {

    public final static String HOST = "wdexpress.market.alicloudapi.com";

    static LogisticsApiClient instance = new LogisticsApiClient();

    public static LogisticsApiClient getInstance() {
        return instance;
    }

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }

    /**
     * 全球快递物流查询
     * @param n 快递单号 【顺丰请输入单号 : 收件人或寄件人手机号后四位。例如：123456789:1234】
     * @param t 可不填，自动识别，快递物流公司类型【共418个见附表】
     * @return ApiResponse
     */
    public ApiResponse query(String n , String t) {
        String path = "/gxali";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("n" , n , ParamPosition.QUERY , true);
        request.addParam("t" , t , ParamPosition.QUERY , false);
        return sendSyncRequest(request);
    }

    /**
     * 单号识别_快递公司
     * @param no 快递单号
     * @return ApiResponse
     */
    public ApiResponse exCompany(String no) {
        String path = "/exCompany";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("no" , no , ParamPosition.QUERY , true);
        return sendSyncRequest(request);
    }

    /**
     * 物流公司列表
     * @param type 快递公司缩写 非必填
     * @return ApiResponse
     */
    public ApiResponse globalExpressLists(String type) {
        String path = "/globalExpressLists";
        ApiRequest request = new ApiRequest(HttpMethod.GET , path);
        request.addParam("type" , type , ParamPosition.QUERY , false);
        return sendSyncRequest(request);
    }
}
