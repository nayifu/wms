package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.activity.ActivityProduct;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.ActivityProductInfoVo;

import java.util.List;

/**
*  ActivityProductService 接口
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
public interface ActivityProductService extends IService<ActivityProduct> {

    /**
     * 通过活动id删除
     * @param aid 活动id
     */
    Boolean deleteByAid(Integer aid);

    /**
     * 通过活动id获取列表
     * @param aid 活动id
     */
    List<ActivityProduct> getH5ListByAid(Integer aid);

    /**
     * 通过活动id获取列表
     * @param aid 活动id
     * @return List<ActivityProductInfoVo>
     */
    PageInfo<ActivityProductInfoVo> getH5ListByAid(Integer aid, PageParamRequest pageParamRequest);
}