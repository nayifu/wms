package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.order.StoreOrderInfo;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.StoreOrderInfoVo;
import com.zbkj.service.dao.StoreOrderInfoDao;
import com.zbkj.service.service.StoreOrderInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * StoreOrderInfoServiceImpl 接口实现
 * +----------------------------------------------------------------------
 * | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 * +----------------------------------------------------------------------
 * | Copyright (c) 2016~2023 https://www.crmeb.com All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 * +----------------------------------------------------------------------
 * | Author: CRMEB Team <admin@crmeb.com>
 * +----------------------------------------------------------------------
 */
@Service
public class StoreOrderInfoServiceImpl extends ServiceImpl<StoreOrderInfoDao, StoreOrderInfo>
        implements StoreOrderInfoService {

    @Resource
    private StoreOrderInfoDao dao;

    /**
     * 获取订单详情vo列表
     * @param orderNo 订单号
     * @return List
     */
    @Override
    public List<StoreOrderInfoVo> getVoListByOrderNo(String orderNo){
        LambdaQueryWrapper<StoreOrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StoreOrderInfo::getMerOrderNo, orderNo);
        List<StoreOrderInfo> systemStoreStaffList = dao.selectList(lambdaQueryWrapper);
        if(systemStoreStaffList.size() < 1){
            return null;
        }

        List<StoreOrderInfoVo> storeOrderInfoVoList = new ArrayList<>();
        for (StoreOrderInfo storeOrderInfo : systemStoreStaffList) {
            StoreOrderInfoVo storeOrderInfoVo = new StoreOrderInfoVo();
            BeanUtils.copyProperties(storeOrderInfo, storeOrderInfoVo);
            storeOrderInfoVoList.add(storeOrderInfoVo);
        }
        return storeOrderInfoVoList;
    }

    /**
     * 获取订单详情-订单编号
     * @param orderNo 订单编号
     * @return List
     */
    @Override
    public List<StoreOrderInfo> getListByOrderNo(String orderNo) {
        LambdaQueryWrapper<StoreOrderInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreOrderInfo::getMerOrderNo, orderNo);
        return dao.selectList(lqw);
    }

    /**
     * 根据时间、商品id获取销售件数
     * @param date 时间，格式'yyyy-MM-dd'
     * @param proId 商品id
     * @return Integer
     */
    @Override
    public Integer getSalesNumByDateAndProductId(String date, Integer proId) {
        return dao.getSalesNumByDateAndProductId(date, proId);
    }

    /**
     * 根据时间、商品id获取销售额
     * @param date 时间，格式'yyyy-MM-dd'
     * @param proId 商品id
     * @return BigDecimal
     */
    @Override
    public BigDecimal getSalesByDateAndProductId(String date, Integer proId) {
        return dao.getSalesByDateAndProductId(date, proId);
    }

    /**
     * 通过订单号字符串查询
     * @param orderNoList 订单号列表
     * @return List
     */
    @Override
    public List<StoreOrderInfo> getListByOrderNoList(List<String> orderNoList) {
        LambdaQueryWrapper<StoreOrderInfo> lqw = new LambdaQueryWrapper<>();
        lqw.in(StoreOrderInfo::getMerOrderNo, orderNoList);
        return dao.selectList(lqw);
    }

    /**
     * 查询订单详情未评论数量
     * @param merOrderNo 商户订单号
     */
    @Override
    public Integer getNotReplyNumByOrderNo(String merOrderNo) {
        LambdaQueryWrapper<StoreOrderInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StoreOrderInfo::getIsReply, false);
        lqw.eq(StoreOrderInfo::getMerOrderNo, merOrderNo);
        return dao.selectCount(lqw);
    }

    /**
     * 订单商品评论列表
     * @param pageRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<StoreOrderInfo> getReplyList(Integer userId, PageParamRequest pageRequest) {
        Page<Object> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit());
        List<StoreOrderInfo> list = dao.findReplyList(userId);
        return CommonPage.copyPageInfo(page, list);
    }

    /**
     * 订单收货
     * @param orderNo 订单号
     * @return Boolean
     */
    @Override
    public Boolean orderReceipt(String orderNo) {
        LambdaUpdateWrapper<StoreOrderInfo> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(StoreOrderInfo::getIsReceipt, true);
        wrapper.eq(StoreOrderInfo::getMerOrderNo, orderNo);
        return update(wrapper);
    }

    /**
     * 获取待评论数量
     * @param userId 用户uid
     * @return Integer
     */
    @Override
    public Integer getAwaitNumByUid(Integer userId) {
        return dao.getAwaitNumByUid(userId);
    }
}

