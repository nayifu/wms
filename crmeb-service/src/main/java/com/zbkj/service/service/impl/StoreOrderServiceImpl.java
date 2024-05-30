package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.OrderConstants;
import com.zbkj.common.constants.UserConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.express.Express;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.order.OrderLogistics;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.model.user.User;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.StoreOrderInfoVo;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.StoreOrderDao;
import com.zbkj.service.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * StoreOrderServiceImpl 接口实现
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
public class StoreOrderServiceImpl extends ServiceImpl<StoreOrderDao, StoreOrder> implements StoreOrderService {

    @Resource
    private StoreOrderDao dao;

    @Autowired
    private StoreOrderInfoService StoreOrderInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreOrderStatusService storeOrderStatusService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private SmsService smsService;

    @Autowired
    private OrderLogisticsService logisticsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderLogisticsService orderLogisticsService;

    @Autowired
    private MerchantService merchantService;

    /**
     * H5订单列表
     * @param uid 用户uid
     * @param status 评价等级|0=未支付,1=待发货,2=待收货,3=待评价,4=已完成,-3=售后/退款
     * @param pageParamRequest 分页参数
     * @return 订单结果列表
     */
    @Override
    public PageInfo<StoreOrder> getUserOrderList(Integer uid, Integer status, PageParamRequest pageParamRequest) {
        Page<StoreOrder> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StoreOrder> lqw = new LambdaQueryWrapper<>();
        statusApiByWhere(lqw, status);
        lqw.eq(StoreOrder::getUid, uid);
        lqw.orderByDesc(StoreOrder::getId);
        List<StoreOrder> orderList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, orderList);
    }

    /**
     * h5 订单查询 where status 封装
     * @param queryWrapper 查询条件
     * @param status 状态
     */
    private void statusApiByWhere(LambdaQueryWrapper<StoreOrder> queryWrapper, Integer status){
        switch (status){
            case OrderConstants.ORDER_STATUS_H5_UNPAID: // 未支付
                queryWrapper.eq(StoreOrder::getPaid, false);
                queryWrapper.eq(StoreOrder::getStatus, OrderConstants.ORDER_STATUS_SHIPPING);
                queryWrapper.eq(StoreOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_NO_REFUND);
                break;
            case OrderConstants.ORDER_STATUS_H5_NOT_SHIPPED: // 待发货
                queryWrapper.eq(StoreOrder::getPaid, true);
                queryWrapper.eq(StoreOrder::getStatus, OrderConstants.ORDER_STATUS_SHIPPING);
                queryWrapper.eq(StoreOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_NO_REFUND);
                break;
            case OrderConstants.ORDER_STATUS_H5_SPIKE: // 待收货
                queryWrapper.eq(StoreOrder::getStatus, OrderConstants.ORDER_STATUS_AWAIT_RECEIVING);
                queryWrapper.eq(StoreOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_NO_REFUND);
                break;
            case OrderConstants.ORDER_STATUS_H5_COMPLETE: // 已收货/已完成
                queryWrapper.eq(StoreOrder::getPaid, true);
                queryWrapper.eq(StoreOrder::getStatus, OrderConstants.ORDER_STATUS_OVER);
                queryWrapper.eq(StoreOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_NO_REFUND);
                break;
        }
        queryWrapper.eq(StoreOrder::getIsUserDel, false);
        queryWrapper.eq(StoreOrder::getIsMerchantDel, false);
    }

    /**
     * 按开始结束时间分组订单
     * @param date String 时间范围
     * @param lefTime int 截取创建时间长度
     * @author Mr.Zhang
     * @since 2020-05-16
     * @return HashMap<String, Object>
     */
    public List<StoreOrder> getOrderGroupByDate(String date, int lefTime) {
        QueryWrapper<StoreOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(pay_price) as pay_price", "left(create_time, "+lefTime+") as orderId", "count(id) as id");
        if (StringUtils.isNotBlank(date)) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(date);
            queryWrapper.between("create_time", dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        queryWrapper.groupBy("orderId").orderByAsc("orderId");
        return dao.selectList(queryWrapper);
    }

    /**
     * 订单详情（PC）
     * @param orderNo 订单编号
     * @return StoreOrderInfoResponse
     */
    @Override
    public StoreOrderInfoResponse info(String orderNo) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        StoreOrder storeOrder = getInfoByMerIdException(orderNo, systemAdmin.getMerId());
        if (storeOrder.getIsMerchantDel()) {
            throw new CrmebException("未找到对应订单信息");
        }
        StoreOrderInfoResponse storeOrderInfoResponse = new StoreOrderInfoResponse();
        BeanUtils.copyProperties(storeOrder, storeOrderInfoResponse);
        List<StoreOrderInfoVo> orderInfos = StoreOrderInfoService.getVoListByOrderNo(storeOrder.getOrderNo());
        storeOrderInfoResponse.setOrderInfo(orderInfos);

        //用户信息
        User user = userService.getById(storeOrder.getUid());
        storeOrderInfoResponse.setNikeName(user.getNickname());
        storeOrderInfoResponse.setUserEmail(user.getEmail());
        return storeOrderInfoResponse;
    }

    /** 发送货物
     * @param request StoreOrderSendRequest 发货参数
     */
    @Override
    public Boolean send(StoreOrderSendRequest request) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        StoreOrder storeOrder = getInfoByMerIdException(request.getOrderNo(), systemAdmin.getMerId());
        if (storeOrder.getIsMerchantDel()) throw new CrmebException("订单已删除,不能发货!");
        if (storeOrder.getStatus() > 0) throw new CrmebException("订单已发货请勿重复操作!");
        return express(request, storeOrder);
    }

    /**
     * 订单备注
     * @return Boolean
     */
    @Override
    public Boolean mark(OrderRemarkRequest request) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        StoreOrder storeOrder = getInfoByMerIdException(request.getOrderNo(), systemAdmin.getMerId());
        storeOrder.setMerRemark(request.getRemark());
        return updateById(storeOrder);
    }

    /**
     * 获取订单快递信息
     * @param orderNo 订单编号
     * @return LogisticsResultVo
     */
    @Override
    public OrderLogistics getLogisticsInfo(String orderNo) {
        getInfoException(orderNo);
        return orderLogisticsService.getByOrderNo(orderNo);
    }

    /**
     * 订单 top 查询参数
     * @param status 状态参数
     * @return 订单查询结果
     */
    @Override
    public Integer getTopDataUtil(Integer status, Integer userId) {
        LambdaQueryWrapper<StoreOrder> lqw = new LambdaQueryWrapper<>();
        statusApiByWhere(lqw, status);
        lqw.eq(StoreOrder::getUid,userId);
        return dao.selectCount(lqw);
    }

    @Override
    public StoreOrder getByOderNo(String orderNo) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreOrder::getOrderNo, orderNo);
        lqw.last(" limit 1");
        return dao.selectOne(lqw);
    }

    /**
     * 更新支付结果
     * @param masterNo 订单编号
     * @return Boolean
     */
    @Override
    public Boolean updatePaidByMasterNo(String masterNo) {
        LambdaUpdateWrapper<StoreOrder> lqw = new LambdaUpdateWrapper<>();
        lqw.set(StoreOrder::getPaid, true);
        lqw.set(StoreOrder::getPayTime, DateUtil.nowDateTime());
        lqw.eq(StoreOrder::getMasterOrderNo, masterNo);
        lqw.eq(StoreOrder::getPaid,false);
        return update(lqw);
    }

    /**
     * 获取用户消费记录
     * @param userId 用户uid
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<ExpensesRecordResponse> getExpensesRecord(Integer userId, PageParamRequest pageParamRequest) {
        Page<StoreOrder> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StoreOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StoreOrder::getUid, userId);
        lqw.eq(StoreOrder::getPaid, true);
        lqw.lt(StoreOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_REFUND);
        lqw.orderByDesc(StoreOrder::getId);
        List<StoreOrder> orderList = dao.selectList(lqw);
        List<ExpensesRecordResponse> responseList = orderList.stream().map(order -> {
            ExpensesRecordResponse response = new ExpensesRecordResponse();
            BeanUtils.copyProperties(order, response);
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(page, responseList);
    }

    /**
     * 获取订单总数量
     * @param uid 用户uid
     * @return Integer
     */
    @Override
    public Integer getOrderCountByUid(Integer uid) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreOrder::getPaid, true);
        lqw.eq(StoreOrder::getUid, uid);
        lqw.lt(StoreOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_REFUND);
        return dao.selectCount(lqw);
    }

    /**
     * 获取用户总消费金额
     * @param userId 用户uid
     * @return BigDecimal
     */
    @Override
    public BigDecimal getSumPayPriceByUid(Integer userId) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreOrder::getPayPrice);
        lqw.eq(StoreOrder::getPaid, true);
        lqw.eq(StoreOrder::getUid, userId);
        lqw.lt(StoreOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_REFUND);
        List<StoreOrder> orderList = dao.selectList(lqw);
        return orderList.stream().map(StoreOrder::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 获取订单数量(时间)
     * @param uid 用户uid
     * @return Integer
     */
    @Override
    public Integer getOrderCountByUidAndDate(Integer uid, String date) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreOrder::getPaid, true);
        lqw.eq(StoreOrder::getUid, uid);
        lqw.lt(StoreOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_REFUND);
        if (StrUtil.isNotBlank(date)) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(date);
            lqw.between(StoreOrder::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        return dao.selectCount(lqw);
    }

    /**
     * 获取用户消费金额(时间)
     * @param userId 用户uid
     * @return BigDecimal
     */
    @Override
    public BigDecimal getSumPayPriceByUidAndDate(Integer userId, String date) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreOrder::getPayPrice);
        lqw.eq(StoreOrder::getPaid, true);
        lqw.eq(StoreOrder::getUid, userId);
        lqw.lt(StoreOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_REFUND);
        if (StrUtil.isNotBlank(date)) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(date);
            lqw.between(StoreOrder::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        List<StoreOrder> orderList = dao.selectList(lqw);
        return orderList.stream().map(StoreOrder::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 订单删除
     * @param orderNo 订单编号
     * @return Boolean
     */
    @Override
    public Boolean delete(String orderNo) {
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();

        StoreOrder storeOrder = getInfoByMerIdException(orderNo, admin.getMerId());
        if (!storeOrder.getIsUserDel()) {
            throw new CrmebException("无法删除用户未删除的订单！");
        }
        if (storeOrder.getIsMerchantDel()) {
            throw new CrmebException("此订单已经被删除了!");
        }
        storeOrder.setIsMerchantDel(true);
        return updateById(storeOrder);
    }

    /**
     * 通过日期获取商品交易件数
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    @Override
    public Integer getOrderProductNumByDate(String date) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(sum(total_num), 0) as total_num");
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        StoreOrder storeOrder = dao.selectOne(wrapper);
        return storeOrder.getTotalNum();
    }

    /**
     * 通过日期获取商品交易成功件数
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    @Override
    public Integer getOrderSuccessProductNumByDate(String date) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(sum(total_num), 0) as total_num");
        wrapper.eq("paid", 1);
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        StoreOrder storeOrder = dao.selectOne(wrapper);
        return storeOrder.getTotalNum();
    }

    /**
     * 通过日期获取订单数量
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    @Override
    public Integer getOrderNumByDate(Integer merId, String date) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        wrapper.eq("paid", 1);
        if (merId > 0) {
            wrapper.eq("mer_id", merId);
        }
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        return dao.selectCount(wrapper);
    }

    /**
     * 通过日期获取支付订单数量
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    @Override
    public Integer getPayOrderNumByDate(String date) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        wrapper.eq("paid", 1);
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        return dao.selectCount(wrapper);
    }

    /**
     * 通过日期获取支付订单金额
     * @param date 日期，yyyy-MM-dd格式
     * @return BigDecimal
     */
    @Override
    public BigDecimal getPayOrderAmountByDate(Integer merId, String date) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("pay_price");
        wrapper.eq("paid", 1);
        if (merId > 0) {
            wrapper.eq("mer_id", merId);
        }
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        List<StoreOrder> orderList = dao.selectList(wrapper);
        if (CollUtil.isEmpty(orderList)) {
            return BigDecimal.ZERO;
        }
        return orderList.stream().map(StoreOrder::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 通过日期获取支付订单金额
     * @param startDate 日期
     * @param endDate 日期
     * @return BigDecimal
     */
    @Override
    public BigDecimal getPayOrderAmountByPeriod(String startDate, String endDate) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(sum(pay_price), 0) as pay_price");
        wrapper.eq("paid", 1);
        wrapper.apply("date_format(create_time, '%Y-%m-%d') between {0} and {1}", startDate, endDate);
        StoreOrder storeOrder = dao.selectOne(wrapper);
        return storeOrder.getPayPrice();
    }

    /**
     * 获取累计消费金额
     * @return BigDecimal
     */
    @Override
    public BigDecimal getTotalPrice() {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(sum(pay_price), 0) as pay_price");
        wrapper.eq("paid", 1);
        StoreOrder storeOrder = dao.selectOne(wrapper);
        return storeOrder.getPayPrice();
    }

    /**
     * 根据日期获取下单用户数量
     * @param date 日期
     * @return Integer
     */
    @Override
    public Integer getOrderUserNumByDate(String date) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        wrapper.groupBy("uid");
        List<StoreOrder> orderList = dao.selectList(wrapper);
        if (CollUtil.isEmpty(orderList)) {
            return 0;
        }
        return orderList.size();
    }

    /**
     * 根据日期获取下单用户数量
     * @param startDate 日期
     * @param endDate 日期
     * @return Integer
     */
    @Override
    public Integer getOrderUserNumByPeriod(String startDate, String endDate) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        wrapper.apply("date_format(create_time, '%Y-%m-%d') between {0} and {1}", startDate, endDate);
        wrapper.groupBy("uid");
        List<StoreOrder> orderList = dao.selectList(wrapper);
        if (CollUtil.isEmpty(orderList)) {
            return 0;
        }
        return orderList.size();
    }

    /**
     * 根据日期获取成交用户数量
     * @param date 日期
     * @return Integer
     */
    @Override
    public Integer getOrderPayUserNumByDate(String date) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        wrapper.eq("paid", 1);
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        wrapper.groupBy("uid");
        List<StoreOrder> orderList = dao.selectList(wrapper);
        if (CollUtil.isEmpty(orderList)) {
            return 0;
        }
        return orderList.size();
    }

    /**
     * 根据日期获取成交用户数量
     * @param startDate 日期
     * @param endDate 日期
     * @return Integer
     */
    @Override
    public Integer getOrderPayUserNumByPeriod(String startDate, String endDate) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        wrapper.eq("paid", 1);
        wrapper.apply("date_format(create_time, '%Y-%m-%d') between {0} and {1}", startDate, endDate);
        wrapper.groupBy("uid");
        List<StoreOrder> orderList = dao.selectList(wrapper);
        if (CollUtil.isEmpty(orderList)) {
            return 0;
        }
        return orderList.size();
    }

    /**
     * 根据用户uid列表获取成交用户数量
     * @param uidList 用户列表
     * @return Integer
     */
    @Override
    public Integer getOrderPayUserNumByUidList(List<Integer> uidList) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        wrapper.eq("paid", 1);
        wrapper.in("uid", uidList);
        wrapper.groupBy("uid");
        List<StoreOrder> orderList = dao.selectList(wrapper);
        if (CollUtil.isEmpty(orderList)) {
            return 0;
        }
        return orderList.size();
    }

    /**
     * 根据用户uid列表获取支付金额
     * @param uidList 用户列表
     * @return BigDecimal
     */
    @Override
    public BigDecimal getPayOrderAmountByUidList(List<Integer> uidList) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<>();
        wrapper.select("IFNULL(sum(pay_price), 0.00) as pay_price");
//        wrapper.select("ifnull(if(sum(pay_price) = 0.00, 0, sum(pay_price)), 0) as pay_price");
        wrapper.eq("paid", 1);
        wrapper.in("uid", uidList);
        List<StoreOrder> orderList = dao.selectList(wrapper);
        if (CollUtil.isEmpty(orderList)) {
            return BigDecimal.ZERO;
        }
        return orderList.stream().map(StoreOrder::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 获取待发货订单数量
     * @return Integer
     */
    @Override
    public Integer getNotShippingNum(Integer merId) {
        return getCount("", OrderConstants.MERCHANT_ORDER_STATUS_NOT_SHIPPED, merId);
    }

    /**
     * 获取退款中订单数量
     */
    @Override
    public Integer getRefundingNum(Integer merId) {
        return getCount("", OrderConstants.ORDER_STATUS_REFUNDING, merId);
    }

    /**
     * 通过订单号列表获取订单列表
     */
    @Override
    public List<StoreOrder> findByOrderNoList(List<String> orderNoList) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.in(StoreOrder::getOrderNo, orderNoList);
        return dao.selectList(lqw);
    }

    /**
     * 取消订单
     * @param masterNo 主订单号
     * @param isUser 是否用户取消
     * @return Boolean
     */
    @Override
    public Boolean cancelByMasterNo(String masterNo, Boolean isUser) {
        LambdaUpdateWrapper<StoreOrder> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(StoreOrder::getStatus, OrderConstants.ORDER_STATUS_CANCEL);
        wrapper.set(StoreOrder::getIsUserCancel, isUser);
        wrapper.eq(StoreOrder::getMasterOrderNo, masterNo);
        return update(wrapper);
    }

    /**
     * 根据主订单号获取订单列表
     * @param masterNo 主订单号
     * @return List
     */
    @Override
    public List<StoreOrder> getListByMasterNo(String masterNo) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreOrder::getMasterOrderNo, masterNo);
        return dao.selectList(lqw);
    }

    /**
     * 商户端后台分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MerchantOrderPageResponse> getMerchantAdminPage(StoreOrderSearchRequest request, PageParamRequest pageParamRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();

        Page<StoreOrder> startPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        QueryWrapper<StoreOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("order_no", "master_order_no", "real_name", "pay_price", "pay_type", "paid",
                "status", "refund_status", "is_user_del", "user_remark", "create_time", "mer_remark");
        queryWrapper.eq("mer_id", systemAdmin.getMerId());
        if (StrUtil.isNotBlank(request.getOrderNo())) {
            queryWrapper.eq("order_no", request.getOrderNo());
        }
        if (StrUtil.isNotEmpty(request.getDateLimit())) {
            getRequestTimeWhere(queryWrapper, request.getDateLimit());
        }
        getMerchantStatusWhere(queryWrapper, request.getStatus());
        queryWrapper.orderByDesc("id");
        List<StoreOrder> orderList = dao.selectList(queryWrapper);
        if (CollUtil.isEmpty(orderList)) {
            return CommonPage.copyPageInfo(startPage, CollUtil.newArrayList());
        }
        List<MerchantOrderPageResponse> pageResponses = orderList.stream().map(e -> {
            MerchantOrderPageResponse pageResponse = new MerchantOrderPageResponse();
            BeanUtils.copyProperties(e, pageResponse);
            return pageResponse;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(startPage, pageResponses);
    }

    /**
     * 获取商户端订单各状态数量
     * @param dateLimit 时间参数
     */
    @Override
    public StoreOrderCountItemResponse getMerchantOrderStatusNum(String dateLimit) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        StoreOrderCountItemResponse response = new StoreOrderCountItemResponse();
        // 全部订单
        response.setAll(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_ALL, systemAdmin.getMerId()));
        // 未支付订单
        response.setUnPaid(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_UNPAID, systemAdmin.getMerId()));
        // 未发货订单
        response.setNotShipped(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_NOT_SHIPPED, systemAdmin.getMerId()));
        // 待收货订单
        response.setSpike(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_SPIKE, systemAdmin.getMerId()));
        // 交易完成订单
        response.setComplete(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_COMPLETE, systemAdmin.getMerId()));
        // 已退款订单
        response.setRefunded(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_REFUNDED, systemAdmin.getMerId()));
        // 已删除订单
        response.setDeleted(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_DELETED, systemAdmin.getMerId()));
        return response;
    }

    /**
     * 平台端后台分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<PlatformOrderPageResponse> getPlatformAdminPage(PlatformOrderSearchRequest request, PageParamRequest pageParamRequest) {
        Page<StoreOrder> startPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        QueryWrapper<StoreOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("mer_id", "order_no", "master_order_no", "real_name", "pay_price", "pay_type", "paid",
                "status", "refund_status", "is_user_del", "is_merchant_del", "user_remark", "mer_remark", "platform_remark", "create_time");
        if (ObjectUtil.isNotNull(request.getMerId())) {
            queryWrapper.eq("mer_id", request.getMerId());
        }
        if (StrUtil.isNotBlank(request.getMasterOrderNo())) {
            queryWrapper.eq("master_order_no", request.getMasterOrderNo());
        }
        if (StrUtil.isNotBlank(request.getOrderNo())) {
            queryWrapper.eq("order_no", request.getOrderNo());
        }
        if (StrUtil.isNotEmpty(request.getDateLimit())) {
            getRequestTimeWhere(queryWrapper, request.getDateLimit());
        }
        getMerchantStatusWhere(queryWrapper, request.getStatus());
        queryWrapper.orderByDesc("id");
        List<StoreOrder> orderList = dao.selectList(queryWrapper);
        if (CollUtil.isEmpty(orderList)) {
            return CommonPage.copyPageInfo(startPage, CollUtil.newArrayList());
        }
        List<Integer> merIdList = orderList.stream().map(StoreOrder::getMerId).collect(Collectors.toList());
        Map<Integer, Merchant> merchantMap = merchantService.getMerIdMapByIdList(merIdList);
        List<PlatformOrderPageResponse> pageResponses = orderList.stream().map(e -> {
            PlatformOrderPageResponse pageResponse = new PlatformOrderPageResponse();
            BeanUtils.copyProperties(e, pageResponse);
            Merchant merchant = merchantMap.get(e.getMerId());
            pageResponse.setMerName(merchant.getName());
            return pageResponse;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(startPage, pageResponses);
    }

    /**
     * 获取平台端订单各状态数量
     * @param dateLimit 时间参数
     */
    @Override
    public StoreOrderCountItemResponse getPlatformOrderStatusNum(String dateLimit) {
        StoreOrderCountItemResponse response = new StoreOrderCountItemResponse();
        // 全部订单
        response.setAll(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_ALL, 0));
        // 未支付订单
        response.setUnPaid(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_UNPAID, 0));
        // 未发货订单
        response.setNotShipped(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_NOT_SHIPPED, 0));
        // 待收货订单
        response.setSpike(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_SPIKE, 0));
        // 交易完成订单
        response.setComplete(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_COMPLETE, 0));
        // 已退款订单
        response.setRefunded(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_REFUNDED, 0));
        // 已删除订单
        response.setDeleted(getCount(dateLimit, OrderConstants.MERCHANT_ORDER_STATUS_DELETED, 0));
        return response;
    }

    /**
     * 订单备注
     * @return Boolean
     */
    @Override
    public Boolean platformMark(OrderRemarkRequest request) {
        StoreOrder storeOrder = getInfoException(request.getOrderNo());
        storeOrder.setPlatformRemark(request.getRemark());
        return updateById(storeOrder);
    }

    /**
     * 订单详情（平台）
     * @param orderNo 订单编号
     * @return StoreOrderInfoResponse
     */
    @Override
    public PlatformOrderInfoResponse platformInfo(String orderNo) {
        StoreOrder storeOrder = getInfoException(orderNo);
        PlatformOrderInfoResponse orderInfoResponse = new PlatformOrderInfoResponse();
        BeanUtils.copyProperties(storeOrder, orderInfoResponse);
        List<StoreOrderInfoVo> orderInfos = StoreOrderInfoService.getVoListByOrderNo(storeOrder.getOrderNo());
        orderInfoResponse.setOrderInfo(orderInfos);

        //用户信息
        User user = userService.getById(storeOrder.getUid());
        orderInfoResponse.setNikeName(user.getNickname());
        orderInfoResponse.setUserEmail(user.getEmail());

        // 商户信息
        Merchant merchant = merchantService.getById(storeOrder.getMerId());
        orderInfoResponse.setMerName(merchant.getName());
        orderInfoResponse.setMerIsSelf(merchant.getIsSelf());
        return orderInfoResponse;
    }

    /**
     * 根据日期获取支付订单数量
     * @param merId 商户id，0为全部商户
     * @param date 日期：年-月-日
     * @return Integer
     */
    @Override
    public Integer findPayNumByDate(Integer merId, String date) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        if (merId > 0) {
            lqw.eq(StoreOrder::getMerId, merId);
        }
        lqw.eq(StoreOrder::getPaid, 1);
        lqw.apply("date_format(pay_time, '%Y-%m-%d') = {0}", date);
        return dao.selectCount(lqw);
    }

    /**
     * 根据日期获取所有支付订单
     * @param merId 商户id，0为全部商户
     * @param date 日期：年-月-日
     * @return List
     */
    @Override
    public List<StoreOrder> findPayByDate(Integer merId, String date) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        if (merId > 0) {
            lqw.eq(StoreOrder::getMerId, merId);
        }
        lqw.eq(StoreOrder::getPaid, 1);
        lqw.apply("date_format(pay_time, '%Y-%m-%d') = {0}", date);
        return dao.selectList(lqw);
    }

    /**
     * 根据月份获取支付订单数量
     * @param merId 商户id，0为全部商户
     * @param month 日期：年-月
     * @return Integer
     */
    @Override
    public Integer findPayNumByMonth(Integer merId, String month) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        if (merId > 0) {
            lqw.eq(StoreOrder::getMerId, merId);
        }
        lqw.eq(StoreOrder::getPaid, 1);
        lqw.apply("date_format(pay_time, '%Y-%m') = {0}", month);
        return dao.selectCount(lqw);
    }

    /**
     * 根据日期获取所有支付订单
     * @param merId 商户id，0为全部商户
     * @param month 日期：年-月
     * @return List
     */
    @Override
    public List<StoreOrder> findPayByMonth(Integer merId, String month) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        if (merId > 0) {
            lqw.eq(StoreOrder::getMerId, merId);
        }
        lqw.eq(StoreOrder::getPaid, 1);
        lqw.apply("date_format(pay_time, '%Y-%m') = {0}", month);
        return dao.selectList(lqw);
    }

    private StoreOrder getInfoException(String orderNo) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreOrder::getOrderNo, orderNo);
        lqw.last(" limit 1");
        StoreOrder storeOrder = dao.selectOne(lqw);
        if (ObjectUtil.isNull(storeOrder)) {
            throw new CrmebException("没有找到订单信息");
        }
        return storeOrder;
    }

    private StoreOrder getInfoByMerIdException(String orderNo, Integer merId) {
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreOrder::getOrderNo, orderNo);
        lqw.eq(StoreOrder::getMerId, merId);
        lqw.last(" limit 1");
        StoreOrder storeOrder = dao.selectOne(lqw);
        if (ObjectUtil.isNull(storeOrder)) {
            throw new CrmebException("没有找到订单信息");
        }
        return storeOrder;
    }

    /** 快递
     * @param request StoreOrderSendRequest 发货参数
     * @param storeOrder StoreOrder 订单信息
     */
    private Boolean express(StoreOrderSendRequest request, StoreOrder storeOrder) {
        //快递公司信息
        Express express = expressService.getByCode(request.getExpressCode());
        storeOrder.setDeliveryId(request.getExpressNumber());
        storeOrder.setDeliveryCode(express.getCode());
        storeOrder.setDeliveryName(express.getName());
        storeOrder.setStatus(OrderConstants.ORDER_STATUS_AWAIT_RECEIVING);

        // 初始化订单物流
        OrderLogistics orderLogistics = new OrderLogistics();
        orderLogistics.setOrderNo(storeOrder.getOrderNo());
        orderLogistics.setExpNo(request.getExpressNumber());
        orderLogistics.setExpCode(express.getCode());
        orderLogistics.setExpName(express.getName());
        orderLogistics.setLogisticsInfo("");
        orderLogistics.setState(0);

        Boolean execute = transactionTemplate.execute(i -> {
            updateById(storeOrder);
            //订单记录增加
            storeOrderStatusService.createLog(storeOrder.getOrderNo(), OrderConstants.ORDER_LOG_EXPRESS,
                    OrderConstants.ORDER_LOG_MESSAGE_EXPRESS.replace("{deliveryName}", express.getName()).replace("{deliveryCode}", storeOrder.getDeliveryId()));
            logisticsService.save(orderLogistics);
            return Boolean.TRUE;
        });
        if (!execute) throw new CrmebException("快递发货失败！");
        sendGoodsNotify(storeOrder);
        return execute;
    }

    /**
     * 发货通知
     * @param storeOrder 订单
     */
    private void sendGoodsNotify(StoreOrder storeOrder) {
        User user = userService.getById(storeOrder.getUid());
        if (ObjectUtil.isNull(user)) {
            return;
        }
        if (user.getUserType().equals(UserConstants.USER_LOGIN_TYPE_PHONE)) {
            // 发送短信通知
            smsService.sendOrderDeliverNotice(user.getCountryCode(), user.getPhone(), storeOrder.getOrderNo());
            return;
        }
        if (StrUtil.isNotBlank(user.getEmail())) {
            emailService.sendOrderDeliver(user.getEmail(), storeOrder.getOrderNo());
        }
    }

    /**
     * 获取订单总数
     * @param dateLimit 时间端
     * @param status String 状态
     * @return Integer
     */
    private Integer getCount(String dateLimit, String status) {
        //总数只计算时间
        QueryWrapper<StoreOrder> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(dateLimit)) {
            getRequestTimeWhere(queryWrapper, dateLimit);
        }
        getMerchantStatusWhere(queryWrapper, status);
        return dao.selectCount(queryWrapper);
    }

    /**
     * 获取订单总数
     * @param dateLimit 时间端
     * @param status String 状态
     * @return Integer
     */
    private Integer getCount(String dateLimit, String status, Integer merId) {
        //总数只计算时间
        QueryWrapper<StoreOrder> queryWrapper = new QueryWrapper<>();
        if (merId > 0) {
            queryWrapper.eq("mer_id", merId);
        }
        if (StrUtil.isNotBlank(dateLimit)) {
            getRequestTimeWhere(queryWrapper, dateLimit);
        }
        getMerchantStatusWhere(queryWrapper, status);
        return dao.selectCount(queryWrapper);
    }

    /**
     * 获取request的where条件
     * @param queryWrapper QueryWrapper<StoreOrder> 表达式
     * @param dateLimit 时间区间参数
     */
    private void getRequestTimeWhere(QueryWrapper<StoreOrder> queryWrapper, String dateLimit) {
        dateLimitUtilVo dateLimitUtilVo = DateUtil.getDateLimit(dateLimit);
        queryWrapper.between("create_time", dateLimitUtilVo.getStartTime(), dateLimitUtilVo.getEndTime());
    }

    /**
     * 根据订单状态获取where条件(商户端)
     * @param queryWrapper QueryWrapper<StoreOrder> 表达式
     * @param status String 类型
     */
    private void getMerchantStatusWhere(QueryWrapper<StoreOrder> queryWrapper, String status) {
        if (StrUtil.isBlank(status)) {
            return;
        }
        switch (status) {
            case OrderConstants.MERCHANT_ORDER_STATUS_ALL: //全部
                break;
            case OrderConstants.MERCHANT_ORDER_STATUS_UNPAID: //未支付
                queryWrapper.eq("paid", 0);//支付状态
                queryWrapper.eq("status", 0); //订单状态
                queryWrapper.eq("is_user_del", 0);//删除状态
                break;
            case OrderConstants.MERCHANT_ORDER_STATUS_NOT_SHIPPED: //未发货
                queryWrapper.eq("paid", 1);
                queryWrapper.eq("status", 0);
                queryWrapper.ne("refund_status", 3);
                queryWrapper.eq("is_user_del", 0);
                break;
            case OrderConstants.MERCHANT_ORDER_STATUS_SPIKE: //待收货
                queryWrapper.eq("paid", 1);
                queryWrapper.eq("status", 1);
                queryWrapper.ne("refund_status", 3);
                queryWrapper.eq("is_user_del", 0);
                break;
            case OrderConstants.MERCHANT_ORDER_STATUS_COMPLETE: //交易完成
                queryWrapper.eq("paid", 1);
                queryWrapper.eq("status", 3);
                queryWrapper.ne("refund_status", 3);
                queryWrapper.eq("is_user_del", 0);
                break;
            case OrderConstants.MERCHANT_ORDER_STATUS_REFUNDED: //已退款
                queryWrapper.eq("paid", 1);
                queryWrapper.eq("refund_status", 3);
                queryWrapper.eq("is_user_del", 0);
                break;
            case OrderConstants.MERCHANT_ORDER_STATUS_DELETED: //已删除
                queryWrapper.eq("is_user_del", 1);
                break;
        }
        queryWrapper.eq("is_merchant_del", 0);
    }

}

