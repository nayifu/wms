package com.zbkj.front.service;

import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.system.SystemConfig;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.IndexInfoResponse;
import com.zbkj.common.response.IndexProductResponse;
import com.zbkj.common.response.PageLayoutBottomNavigationResponse;
import com.zbkj.common.response.PcIndexInfoResponse;

import java.util.HashMap;
import java.util.List;

/**
* IndexService 接口
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
public interface IndexService{

    /**
     * 首页信息
     * @return IndexInfoResponse
     */
    IndexInfoResponse getIndexInfo();

    /**
     * 热门搜索
     * @return List
     */
    List<HashMap<String, Object>> hotKeywords();

    /**
     * 获取首页商品列表
     * @param pageParamRequest 分页参数
     * @return List
     */
    PageInfo<IndexProductResponse> findIndexProductList(PageParamRequest pageParamRequest);

    /**
     * 获取颜色配置
     * @return SystemConfig
     */
    SystemConfig getColorConfig();

    /**
     * 获取全局本地图片域名
     * @return String
     */
    String getImageDomain();

    /**
     * PC首页数据
     */
    PcIndexInfoResponse getPcIndexInfo();

    /**
     * 获取底部导航信息
     */
    PageLayoutBottomNavigationResponse getBottomNavigationInfo();
}
