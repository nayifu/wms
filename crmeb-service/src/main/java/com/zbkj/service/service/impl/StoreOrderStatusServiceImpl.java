package com.zbkj.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.constants.OrderConstants;
import com.zbkj.common.model.order.StoreOrderStatus;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreOrderStatusSearchRequest;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.dao.StoreOrderStatusDao;
import com.zbkj.service.service.StoreOrderStatusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * StoreOrderStatusServiceImpl 接口实现
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
public class StoreOrderStatusServiceImpl extends ServiceImpl<StoreOrderStatusDao, StoreOrderStatus> implements StoreOrderStatusService {

    @Resource
    private StoreOrderStatusDao dao;

    /**
    * 列表
    * @param request 请求参数
    * @param pageParamRequest 分页类参数
    * @return List<StoreOrderStatus>
    */
    @Override
    public List<StoreOrderStatus> getList(StoreOrderStatusSearchRequest request, PageParamRequest pageParamRequest) {
        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StoreOrderStatus> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StoreOrderStatus::getMerOrderNo, request.getOrderNo());
        lqw.orderByDesc(StoreOrderStatus::getCreateTime);
        return dao.selectList(lqw);
    }

    /**
     * 保存订单退款记录
     * @param orderNo 订单号
     * @param amount  金额
     * @param message  备注
     * @return {@link Boolean}
     */
    @Override
    public Boolean saveRefund(String orderNo, BigDecimal amount, String message) {
        //此处更新订单状态
        String changeMessage = OrderConstants.ORDER_LOG_MESSAGE_REFUND_PRICE.replace("{amount}", amount.toString());
        if(StrUtil.isNotEmpty(message)){
            changeMessage += message;
        }
        StoreOrderStatus storeOrderStatus = new StoreOrderStatus();
        storeOrderStatus.setMerOrderNo(orderNo);
        storeOrderStatus.setChangeType(OrderConstants.ORDER_LOG_REFUND_PRICE);
        storeOrderStatus.setChangeMessage(changeMessage);
        return save(storeOrderStatus);
    }

    /**
     * 创建记录日志
     * @param orderNo  订单号
     * @param type String 类型
     * @param message String 消息
     * @return Boolean
     */
    @Override
    public Boolean createLog(String orderNo, String type, String message) {
        StoreOrderStatus storeOrderStatus = new StoreOrderStatus();
        storeOrderStatus.setMerOrderNo(orderNo);
        storeOrderStatus.setChangeType(type);
        storeOrderStatus.setChangeMessage(message);
        storeOrderStatus.setCreateTime(DateUtil.nowDateTime());
        return save(storeOrderStatus);
    }

}

