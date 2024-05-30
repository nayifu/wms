package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.activity.ActivityProduct;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.ActivityProductInfoVo;
import com.zbkj.service.dao.ActivityProductDao;
import com.zbkj.service.service.ActivityProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
*  ActivityProductServiceImpl 接口实现
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
@Service
public class ActivityProductServiceImpl extends ServiceImpl<ActivityProductDao, ActivityProduct> implements ActivityProductService {

    @Resource
    private ActivityProductDao dao;

    /**
     * 通过活动id删除
     * @param aid 活动id
     */
    @Override
    public Boolean deleteByAid(Integer aid) {
        LambdaUpdateWrapper<ActivityProduct> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ActivityProduct::getAid, aid);
        return dao.delete(wrapper) > 0;
    }

    /**
     * 通过活动id获取列表
     * @param aid 活动id
     */
    @Override
    public List<ActivityProduct> getH5ListByAid(Integer aid) {
        LambdaQueryWrapper<ActivityProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(ActivityProduct::getAid, aid);
        return dao.selectList(lqw);
    }

    /**
     * 通过活动id获取商品列表
     * @param aid 活动id
     * @return List<ActivityProductInfoVo>
     */
    @Override
    public PageInfo<ActivityProductInfoVo> getH5ListByAid(Integer aid, PageParamRequest pageParamRequest) {
        Page<ActivityProductInfoVo> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<ActivityProductInfoVo> infoVoList = dao.getH5ListByAid(aid);
        return CommonPage.copyPageInfo(page, infoVoList);
    }

}

