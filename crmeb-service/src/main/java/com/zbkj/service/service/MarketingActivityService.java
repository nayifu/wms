package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.activity.MarketingActivity;
import com.zbkj.common.request.ActivityRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ActivityListResponse;
import com.zbkj.common.response.MarketingActivityInfoResponse;
import com.zbkj.common.response.PcActivityInfoResponse;
import com.zbkj.common.vo.ActivityProductInfoVo;

import java.util.List;

/**
*  MarketingActivityService 接口
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
public interface MarketingActivityService extends IService<MarketingActivity> {

    /**
     * 分页列表
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<MarketingActivity> getAdminPage(PageParamRequest pageParamRequest);

    /**
     * 添加活动
     * @param request 添加参数
     * @return Boolean
     */
    Boolean add(ActivityRequest request);

    /**
     * 删除活动
     * @param id 活动id
     * @return Boolean
     */
    Boolean delete(Integer id);

    /**
     * 编辑活动
     * @param request 编辑参数
     * @return Boolean
     */
    Boolean edit(ActivityRequest request);

    /**
     * 活动详情（平台端）
     * @param id 活动id
     * @return MarketingActivityInfoResponse
     */
    MarketingActivityInfoResponse getInfo(Integer id);

    /**
     * 活动开关
     * @param id 活动id
     * @return Boolean
     */
    Boolean updateSwitch(Integer id);

    /**
     * H5活动商品列表
     * @param aid 活动id
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<ActivityProductInfoVo> getH5List(Integer aid, PageParamRequest pageParamRequest);

    /**
     * H5首页活动列表
     * @return List
     */
    List<ActivityListResponse> getIndexList();

    /**
     * 获取pc活动详情
     * @param id 活动id
     * @return PcActivityInfoResponse
     */
    PcActivityInfoResponse getPcDetail(Integer id);
}