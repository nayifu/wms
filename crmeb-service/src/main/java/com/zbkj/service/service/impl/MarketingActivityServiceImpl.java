package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.activity.ActivityProduct;
import com.zbkj.common.model.activity.MarketingActivity;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ActivityProductRequest;
import com.zbkj.common.request.ActivityRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ActivityListResponse;
import com.zbkj.common.response.ActivityProductInfoResponse;
import com.zbkj.common.response.MarketingActivityInfoResponse;
import com.zbkj.common.response.PcActivityInfoResponse;
import com.zbkj.common.vo.ActivityProductInfoVo;
import com.zbkj.service.dao.MarketingActivityDao;
import com.zbkj.service.service.ActivityProductService;
import com.zbkj.service.service.MarketingActivityService;
import com.zbkj.service.service.StoreProductService;
import com.zbkj.service.service.SystemAttachmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
*  MarketingActivityServiceImpl 接口实现
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
public class MarketingActivityServiceImpl extends ServiceImpl<MarketingActivityDao, MarketingActivity> implements MarketingActivityService {

    @Resource
    private MarketingActivityDao dao;

    @Autowired
    private ActivityProductService activityProductService;

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 分页列表
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MarketingActivity> getAdminPage(PageParamRequest pageParamRequest) {
        Page<MarketingActivity> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<MarketingActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(MarketingActivity::getIsDel, false);
        lqw.orderByDesc(MarketingActivity::getId);
        List<MarketingActivity> activityList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, activityList);
    }

    /**
     * 添加活动
     * @param request 添加参数
     * @return Boolean
     */
    @Override
    public Boolean add(ActivityRequest request) {
        MarketingActivity activity = new MarketingActivity();
        BeanUtils.copyProperties(request, activity);
        activity.setId(null);
        if (StrUtil.isNotBlank(request.getBanner())) {
            activity.setBanner(systemAttachmentService.clearPrefix(request.getBanner()));
        }
        List<ActivityProduct> apList = CollUtil.newArrayList();
        if (CollUtil.isNotEmpty(request.getProList())) {
            if (activity.getType().equals(3)) {
                List<ActivityProductRequest> imageList = request.getProList().stream().filter(e -> StrUtil.isNotBlank(e.getProImage())).collect(Collectors.toList());
                if (CollUtil.isEmpty(imageList)) {
                    throw new CrmebException("大图展示类型，请上传大图");
                }
            }
            // 校验所选商品是否符合在售标准
//            List<Integer> proIdList = request.getProList().stream().map(ActivityProduct::getProId).collect(Collectors.toList());
//            if (!storeProductService.checkSellGoods(proIdList)) {
//                throw new CrmebException("有商品不符合在售标准，请重新选择商品");
//            }
            request.getProList().forEach(e -> {
                ActivityProduct ap = new ActivityProduct();
                ap.setProId(e.getProId());
                if (StrUtil.isNotEmpty(e.getProImage())) {
                    ap.setProImage(systemAttachmentService.clearPrefix(e.getProImage()));
                }
                apList.add(ap);
            });
        }
        return transactionTemplate.execute(e -> {
            save(activity);
            if (CollUtil.isNotEmpty(apList)) {
                apList.forEach(ap -> ap.setAid(activity.getId()));
                activityProductService.saveBatch(apList);
            }
            return Boolean.TRUE;
        });
    }

    /**
     * 删除活动
     * @param id 活动id
     * @return Boolean
     */
    @Override
    public Boolean delete(Integer id) {
        MarketingActivity activity = getByIdException(id);
        activity.setIsDel(true);
        return transactionTemplate.execute(e -> {
            updateById(activity);
            activityProductService.deleteByAid(activity.getId());
            return Boolean.TRUE;
        });
    }

    /**
     * 编辑活动
     * @param request 编辑参数
     * @return Boolean
     */
    @Override
    public Boolean edit(ActivityRequest request) {
        if (ObjectUtil.isNull(request.getId())) {
            throw new CrmebException("请先选择活动");
        }
        getByIdException(request.getId());
        MarketingActivity activity = new MarketingActivity();
        BeanUtils.copyProperties(request, activity);
        if (StrUtil.isNotBlank(request.getBanner())) {
            activity.setBanner(systemAttachmentService.clearPrefix(request.getBanner()));
        } else {
            activity.setBanner("");
        }
        List<ActivityProduct> apList = CollUtil.newArrayList();
        if (CollUtil.isNotEmpty(request.getProList())) {
            if (activity.getType().equals(3)) {
                List<ActivityProductRequest> imageList = request.getProList().stream().filter(e -> StrUtil.isNotBlank(e.getProImage())).collect(Collectors.toList());
                if (CollUtil.isEmpty(imageList)) {
                    throw new CrmebException("大图展示类型，请上传大图");
                }
            }
            // 校验所选商品是否符合在售标准
//            List<Integer> proIdList = request.getProList().stream().map(ActivityProduct::getProId).collect(Collectors.toList());
//            if (!storeProductService.checkSellGoods(proIdList)) {
//                throw new CrmebException("有商品不符合在售标准，请重新选择商品");
//            }
            request.getProList().forEach(e -> {
                ActivityProduct ap = new ActivityProduct();
                ap.setAid(activity.getId());
                ap.setProId(e.getProId());
                if (StrUtil.isNotEmpty(e.getProImage())) {
                    ap.setProImage(systemAttachmentService.clearPrefix(e.getProImage()));
                }
                apList.add(ap);
            });
        }
        return transactionTemplate.execute(e -> {
            updateById(activity);
            activityProductService.deleteByAid(activity.getId());
            if (CollUtil.isNotEmpty(apList)) {
                apList.forEach(ap -> ap.setAid(activity.getId()));
                activityProductService.saveBatch(apList);
            }
            return Boolean.TRUE;
        });
    }

    /**
     * 活动详情（平台端）
     * @param id 活动id
     * @return MarketingActivityInfoResponse
     */
    @Override
    public MarketingActivityInfoResponse getInfo(Integer id) {
        MarketingActivity activity = getByIdException(id);
        MarketingActivityInfoResponse response = new MarketingActivityInfoResponse();
        BeanUtils.copyProperties(activity, response);
        List<ActivityProduct> apList = activityProductService.getH5ListByAid(activity.getId());
        if (CollUtil.isNotEmpty(apList)) {
            List<ActivityProductInfoResponse> infoResponseList = apList.stream().map(e -> {
                StoreProduct storeProduct = storeProductService.getById(e.getProId());
                ActivityProductInfoResponse infoResponse = new ActivityProductInfoResponse();
                BeanUtils.copyProperties(e, infoResponse);
                infoResponse.setProName(storeProduct.getStoreName());
                infoResponse.setProductImage(storeProduct.getImage());
                return infoResponse;
            }).collect(Collectors.toList());
            response.setProList(infoResponseList);
        }
        return response;
    }

    /**
     * 活动开关
     * @param id 活动id
     * @return Boolean
     */
    @Override
    public Boolean updateSwitch(Integer id) {
        MarketingActivity activity = getByIdException(id);
        activity.setIsOpen(!activity.getIsOpen());
        return updateById(activity);
    }

    /**
     * H5活动商品列表
     * @param aid 活动id
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<ActivityProductInfoVo> getH5List(Integer aid, PageParamRequest pageParamRequest) {
        MarketingActivity activity = getByIdException(aid);
        if (!activity.getIsOpen()) {
            throw new CrmebException("Event is closed");
        }
        return activityProductService.getH5ListByAid(aid, pageParamRequest);
    }

    /**
     * H5首页活动列表
     * @return List
     */
    @Override
    public List<ActivityListResponse> getIndexList() {
        LambdaQueryWrapper<MarketingActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(MarketingActivity::getIsOpen, true);
        lqw.eq(MarketingActivity::getIsDel, false);
        lqw.orderByDesc(MarketingActivity::getSort);
        List<MarketingActivity> activityList = dao.selectList(lqw);
        if (CollUtil.isEmpty(activityList)) {
            return CollUtil.newArrayList();
        }
        List<ActivityListResponse> listResponse = CollUtil.newArrayList();
        for (MarketingActivity activity : activityList) {
            PageParamRequest pageParamRequest = new PageParamRequest();
            pageParamRequest.setPage(1);
            pageParamRequest.setLimit(8);
            if (activity.getType().equals(2)) {
                pageParamRequest.setLimit(3);
            }
            PageInfo<ActivityProductInfoVo> pageInfo = getH5List(activity.getId(), pageParamRequest);
            List<ActivityProductInfoVo> voList = pageInfo.getList();
            if (CollUtil.isEmpty(voList)) {
                continue;
            }
            ActivityListResponse response = new ActivityListResponse();
            BeanUtils.copyProperties(activity, response);
            response.setProductList(voList);
            listResponse.add(response);
        }
        return listResponse;
    }

    /**
     * 获取pc活动详情
     * @param id 活动id
     * @return PcActivityInfoResponse
     */
    @Override
    public PcActivityInfoResponse getPcDetail(Integer id) {
        MarketingActivity activity = getByIdException(id);
        if (!activity.getIsOpen()) {
            throw new CrmebException("activity does not exist");
        }
        PcActivityInfoResponse response = new PcActivityInfoResponse();
        BeanUtils.copyProperties(activity, response);
        return response;
    }

    private MarketingActivity getByIdException(Integer id) {
        MarketingActivity activity = getById(id);
        if (ObjectUtil.isNull(activity) || activity.getIsDel()) {
            throw new CrmebException("activity does not exist");
        }
        return activity;
    }
}

