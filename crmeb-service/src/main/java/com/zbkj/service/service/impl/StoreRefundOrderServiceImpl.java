package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.OrderConstants;
import com.zbkj.common.constants.PayConstants;
import com.zbkj.common.constants.TaskConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreOrderProfitSharing;
import com.zbkj.common.model.order.StoreRefundOrder;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.MerchantRefundOrderPageResponse;
import com.zbkj.common.response.PlatformRefundOrderPageResponse;
import com.zbkj.common.response.RefundOrderCountItemResponse;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.StoreRefundOrderDao;
import com.zbkj.service.service.*;
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
 * StoreRefundOrderServiceImpl 接口实现
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
public class StoreRefundOrderServiceImpl extends ServiceImpl<StoreRefundOrderDao, StoreRefundOrder> implements StoreRefundOrderService {

    @Resource
    private StoreRefundOrderDao dao;

    @Autowired
    private StoreOrderService storeOrderService;
    @Autowired
    private StoreOrderRefundStatusService orderRefundStatusService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private PaypalService paypalService;
    @Autowired
    private StoreOrderStatusService storeOrderStatusService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StoreOrderProfitSharingService orderProfitSharingService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private UserService userService;
    @Autowired
    private StripeService stripeService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private CrmebConfig crmebConfig;
    @Autowired
    private MasterOrderService masterOrderService;

    /**
     * 商户端退款订单分页列表
     *
     * @param request          查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MerchantRefundOrderPageResponse> getMerchantAdminPage(RefundOrderSearchRequest request, PageParamRequest pageParamRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Page<StoreRefundOrder> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        QueryWrapper<StoreRefundOrder> wrapper = Wrappers.query();
        wrapper.eq("mer_id", systemAdmin.getMerId());
        if (StrUtil.isNotEmpty(request.getRefundOrderNo())) {
            wrapper.eq("refund_order_no", request.getRefundOrderNo());
        }
        if (StrUtil.isNotEmpty(request.getMerOrderNo())) {
            wrapper.eq("mer_order_no", request.getMerOrderNo());
        }
        if (StrUtil.isNotEmpty(request.getDateLimit())) {
            getRequestTimeWhere(wrapper, request.getDateLimit());
        }
        getStatusWhere(wrapper, request.getRefundStatus());
        wrapper.orderByDesc("id");
        List<StoreRefundOrder> refundOrderList = dao.selectList(wrapper);
        if (CollUtil.isEmpty(refundOrderList)) {
            return CommonPage.copyPageInfo(page, CollUtil.newArrayList());
        }
        List<MerchantRefundOrderPageResponse> responseList = refundOrderList.stream().map(order -> {
            MerchantRefundOrderPageResponse response = new MerchantRefundOrderPageResponse();
            BeanUtils.copyProperties(order, response);
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(page, responseList);
    }

    /**
     * 获取商户端退款订单各状态数量
     *
     * @param dateLimit 时间参数
     * @return RefundOrderCountItemResponse
     */
    @Override
    public RefundOrderCountItemResponse getMerchantOrderStatusNum(String dateLimit) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        return getOrderStatusNum(dateLimit, systemAdmin.getMerId());
    }

    /**
     * 获取退款订单各状态数量
     *
     * @param dateLimit 时间参数
     * @param merId     商户id，平台为0
     * @return RefundOrderCountItemResponse
     */
    private RefundOrderCountItemResponse getOrderStatusNum(String dateLimit, Integer merId) {
        RefundOrderCountItemResponse response = new RefundOrderCountItemResponse();
        // 全部订单
        response.setAll(getCount(dateLimit, 9, merId));
        // 待审核
        response.setAwait(getCount(dateLimit, OrderConstants.MERCHANT_REFUND_ORDER_STATUS_APPLY, merId));
        // 审核拒绝
        response.setReject(getCount(dateLimit, OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REJECT, merId));
        // 退款中
        response.setRefunding(getCount(dateLimit, OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUNDING, merId));
        // 已退款
        response.setRefunded(getCount(dateLimit, OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUND, merId));
        return response;
    }

    /**
     * 备注退款单
     *
     * @param request 备注参数
     * @return Boolean
     */
    @Override
    public Boolean mark(RefundOrderRemarkRequest request) {
        StoreRefundOrder refundOrder = getInfoException(request.getRefundOrderNo());
        refundOrder.setMerRemark(request.getRemark());
        return updateById(refundOrder);
    }

    @Override
    public Boolean refund(StoreOrderRefundRequest request) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        StoreRefundOrder refundOrder = getInfoException(request.getRefundOrderNo());
        if (!refundOrder.getRefundStatus().equals(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_APPLY)) {
            throw new CrmebException("退款单状态异常");
        }
        if (!refundOrder.getMerId().equals(systemAdmin.getMerId())) {
            throw new CrmebException("无法操作非自己商户的订单");
        }
        StoreOrder storeOrder = storeOrderService.getByOderNo(refundOrder.getMerOrderNo());
        if (!storeOrder.getPaid()) {
            throw new CrmebException("未支付无法退款");
        }

        if (storeOrder.getPayType().equals(PayConstants.PAY_TYPE_PAYPAL)) {
            try {
                Boolean refund = paypalService.refund(storeOrder);
                if (!refund) {
                    throw new CrmebException("PayPal申请退款失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new CrmebException("PayPal申请退款失败！");
            }
        }
        if (storeOrder.getPayType().equals(PayConstants.PAY_TYPE_STRIPE)) {
            Boolean refund = stripeService.refund(storeOrder);
            if (!refund) {
                throw new CrmebException("Stripe审核退款失败！");
            }
        }
        if (storeOrder.getPayType().equals(PayConstants.PAY_TYPE_WECHAT)) {
            MasterOrder masterOrder = masterOrderService.getByOrderNo(storeOrder.getMasterOrderNo());
            WxPayRefundV3Request v3Request = new WxPayRefundV3Request();
            v3Request.setOutTradeNo(storeOrder.getMasterOrderNo());
            v3Request.setOutRefundNo(refundOrder.getRefundOrderNo());
            v3Request.setNotifyUrl(crmebConfig.getWxpayRefundNotifyUrl());
            WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
            amount.setRefund(refundOrder.getRefundPrice().multiply(new BigDecimal("100")).intValue());
            amount.setTotal(masterOrder.getPayPrice().multiply(new BigDecimal("100")).intValue());
            amount.setCurrency("CNY");
            v3Request.setAmount(amount);
            WxPayRefundV3Result v3Result;
            try {
                v3Result = wxPayService.refundV3(v3Request);
            } catch (WxPayException e) {
                throw new CrmebException("微信申请退款失败！");
            }
            storeOrder.setRefundStatus(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUNDING);
            refundOrder.setRefundStatus(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUNDING);
            Boolean execute = transactionTemplate.execute(e -> {
                updateById(refundOrder);
                storeOrderService.updateById(storeOrder);
                return Boolean.TRUE;
            });
            if (!execute) {
                throw new CrmebException("微信退款操作数据库失败，refund_order_no = " + refundOrder.getRefundOrderNo());
            }
            return execute;
        }
        //修改订单退款状态
        storeOrder.setRefundStatus(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUND);
        refundOrder.setRefundStatus(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUND);
        refundOrder.setRefundTime(DateUtil.nowDateTime());
        // 获取分账信息
        StoreOrderProfitSharing profitSharing = orderProfitSharingService.getByMerOrderNo(refundOrder.getMerOrderNo());
        profitSharing.setStatus(OrderConstants.ORDER_PROFIT_SHARING_STATUS_REFUND);
        profitSharing.setProfitSharingRefund(storeOrder.getPayPrice());

        Boolean execute = transactionTemplate.execute(e -> {
            updateById(refundOrder);
            storeOrderService.updateById(storeOrder);
            //新增日志
            storeOrderStatusService.saveRefund(storeOrder.getOrderNo(), refundOrder.getRefundPrice(), "成功");
            orderRefundStatusService.createLog(refundOrder.getRefundOrderNo(),
                    OrderConstants.REFUND_ORDER_LOG_TYPE_REFUND, OrderConstants.ORDER_LOG_MESSAGE_REFUND_PRICE.replace("{amount}", refundOrder.getRefundPrice().toString()));
            // 分账处理
            orderProfitSharingService.updateById(profitSharing);
            merchantService.operationBalance(profitSharing.getMerId(), profitSharing.getProfitSharingMerPrice(), Constants.OPERATION_TYPE_SUBTRACT);
            // 退款task
            return Boolean.TRUE;
        });
        if (!execute) {
            storeOrderStatusService.saveRefund(storeOrder.getOrderNo(), storeOrder.getPayPrice(), "失败");
            throw new CrmebException("订单更新失败");
        }
        redisUtil.lPush(TaskConstants.ORDER_TASK_REDIS_KEY_AFTER_REFUND_BY_USER, refundOrder.getRefundOrderNo());
        return execute;
    }

    /**
     * 拒绝退款
     *
     * @param request 拒绝退款参数
     * @return Boolean
     */
    @Override
    public Boolean refundRefuse(StoreOrderRefundRequest request) {
        if (StrUtil.isEmpty(request.getReason())) {
            throw new CrmebException("请填写拒绝退款原因");
        }
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        StoreRefundOrder refundOrder = getInfoException(request.getRefundOrderNo());
        if (!refundOrder.getRefundStatus().equals(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_APPLY)) {
            throw new CrmebException("退款单状态异常");
        }
        if (!refundOrder.getMerId().equals(systemAdmin.getMerId())) {
            throw new CrmebException("无法操作非自己商户的订单");
        }
        refundOrder.setRefundStatus(OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REJECT);
        refundOrder.setRefundReason(request.getReason());

        StoreOrder storeOrder = storeOrderService.getByOderNo(refundOrder.getMerOrderNo());
        if (ObjectUtil.isNull(storeOrder)) {
            throw new CrmebException("退款单关联的订单不存在");
        }
        storeOrder.setRefundStatus(OrderConstants.ORDER_REFUND_STATUS_NO_REFUND);

        return transactionTemplate.execute(e -> {
            updateById(refundOrder);
            storeOrderService.updateById(storeOrder);
            orderRefundStatusService.createLog(refundOrder.getRefundOrderNo(), OrderConstants.REFUND_ORDER_LOG_TYPE_FAIL, OrderConstants.ORDER_LOG_MESSAGE_REFUND_REFUSE.replace("{reason}", request.getReason()));
            return Boolean.TRUE;
        });
    }

    /**
     * 获取退款单
     *
     * @param refundOrderNo 退款单号
     * @return StoreRefundOrder
     */
    @Override
    public StoreRefundOrder getByRefundOrderNo(String refundOrderNo) {
        LambdaQueryWrapper<StoreRefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreRefundOrder::getRefundOrderNo, refundOrderNo);
        lqw.last(" limit 1");
        return dao.selectOne(lqw);
    }

    /**
     * 平台端退款订单分页列表
     *
     * @param request          查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<PlatformRefundOrderPageResponse> getPlatformAdminPage(PlatformRefundOrderSearchRequest request, PageParamRequest pageParamRequest) {
        Page<StoreRefundOrder> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        QueryWrapper<StoreRefundOrder> wrapper = Wrappers.query();
        if (ObjectUtil.isNotNull(request.getMerId())) {
            wrapper.eq("mer_id", request.getMerId());
        }
        if (StrUtil.isNotEmpty(request.getRefundOrderNo())) {
            wrapper.eq("refund_order_no", request.getRefundOrderNo());
        }
        if (StrUtil.isNotEmpty(request.getMerOrderNo())) {
            wrapper.eq("mer_order_no", request.getMerOrderNo());
        }
        if (StrUtil.isNotEmpty(request.getDateLimit())) {
            getRequestTimeWhere(wrapper, request.getDateLimit());
        }
        getStatusWhere(wrapper, request.getRefundStatus());
        wrapper.orderByDesc("id");
        List<StoreRefundOrder> refundOrderList = dao.selectList(wrapper);
        if (CollUtil.isEmpty(refundOrderList)) {
            return CommonPage.copyPageInfo(page, CollUtil.newArrayList());
        }
        List<Integer> merIdList = refundOrderList.stream().map(StoreRefundOrder::getMerId).collect(Collectors.toList());
        Map<Integer, Merchant> merchantMap = merchantService.getMerIdMapByIdList(merIdList);
        List<PlatformRefundOrderPageResponse> responseList = refundOrderList.stream().map(order -> {
            PlatformRefundOrderPageResponse response = new PlatformRefundOrderPageResponse();
            BeanUtils.copyProperties(order, response);
            Merchant merchant = merchantMap.get(order.getMerId());
            response.setMerName(merchant.getName());
            return response;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(page, responseList);
    }

    /**
     * 获取平台端退款订单各状态数量
     *
     * @param dateLimit 时间参数
     * @return RefundOrderCountItemResponse
     */
    @Override
    public RefundOrderCountItemResponse getPlatformOrderStatusNum(String dateLimit) {
        return getOrderStatusNum(dateLimit, 0);
    }

    /**
     * 平台备注退款单
     *
     * @param request 备注参数
     * @return Boolean
     */
    @Override
    public Boolean platformMark(RefundOrderRemarkRequest request) {
        StoreRefundOrder refundOrder = getInfoException(request.getRefundOrderNo());
        refundOrder.setPlatformRemark(request.getRemark());
        return updateById(refundOrder);
    }

    /**
     * 获取某一天的所有已退款的退款单
     *
     * @param merId 商户id，0为所有商户
     * @param date  日期：年-月-日
     * @return List
     */
    @Override
    public List<StoreRefundOrder> findRefundedByDate(Integer merId, String date) {
        LambdaQueryWrapper<StoreRefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreRefundOrder::getRefundStatus, OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUND);
        if (merId > 0) {
            lqw.eq(StoreRefundOrder::getMerId, merId);
        }
        lqw.apply("date_format(update_time, '%Y-%m-%d') = {0}", date);
        return dao.selectList(lqw);
    }

    /**
     * 获取退款单
     *
     * @param orderNo 商户订单号
     * @return StoreRefundOrder
     */
    @Override
    public StoreRefundOrder getByMerchantOrderNo(String orderNo) {
        LambdaQueryWrapper<StoreRefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreRefundOrder::getMerOrderNo, orderNo);
        lqw.last(" limit 1");
        return dao.selectOne(lqw);
    }

    /**
     * 退款订单列表
     *
     * @param pageRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<StoreRefundOrder> getH5List(PageParamRequest pageRequest) {
        Integer userId = userService.getUserId();
        Page<Object> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit());
        LambdaQueryWrapper<StoreRefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreRefundOrder::getUid, userId);
        lqw.orderByDesc(StoreRefundOrder::getId);
        List<StoreRefundOrder> list = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, list);
    }

    /**
     * 根据日期获取退款订单数量
     *
     * @param date 日期
     * @return Integer
     */
    @Override
    public Integer getRefundOrderNumByDate(String date) {
        LambdaQueryWrapper<StoreRefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreRefundOrder::getId);
        lqw.eq(StoreRefundOrder::getRefundStatus, OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUND);
        lqw.apply("date_format(update_time, '%Y-%m-%d') = {0}", date);
        return dao.selectCount(lqw);
    }

    /**
     * 根据日期获取退款订单金额
     *
     * @param date 日期
     * @return Integer
     */
    @Override
    public BigDecimal getRefundOrderAmountByDate(String date) {
        LambdaQueryWrapper<StoreRefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreRefundOrder::getRefundPrice);
        lqw.eq(StoreRefundOrder::getRefundStatus, OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUND);
        lqw.apply("date_format(update_time, '%Y-%m-%d') = {0}", date);
        List<StoreRefundOrder> orderList = dao.selectList(lqw);
        return orderList.stream().map(StoreRefundOrder::getRefundPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 待退款订单数量
     *
     * @return Integer
     */
    @Override
    public Integer getAwaitAuditNum(Integer merId) {
        LambdaQueryWrapper<StoreRefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreRefundOrder::getId);
        lqw.eq(StoreRefundOrder::getRefundStatus, OrderConstants.MERCHANT_REFUND_ORDER_STATUS_APPLY);
        if (merId > 0) {
            lqw.eq(StoreRefundOrder::getMerId, merId);
        }
        return dao.selectCount(lqw);
    }

    /**
     * 待退款订单数量(用户)
     *
     * @param userId 用户uid
     * @return Integer
     */
    @Override
    public Integer getAwaitNumByUid(Integer userId) {
        LambdaQueryWrapper<StoreRefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreRefundOrder::getUid, userId);
        lqw.in(StoreRefundOrder::getRefundStatus, OrderConstants.ORDER_REFUND_STATUS_NO_REFUND, OrderConstants.ORDER_REFUND_STATUS_REFUNDING);
        return dao.selectCount(lqw);
    }

    private StoreRefundOrder getInfoException(String refundOrderNo) {
        LambdaQueryWrapper<StoreRefundOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreRefundOrder::getRefundOrderNo, refundOrderNo);
        lqw.last(" limit 1");
        StoreRefundOrder refundOrder = dao.selectOne(lqw);
        if (ObjectUtil.isNull(refundOrder)) {
            throw new CrmebException("退款单不存在");
        }
        return refundOrder;
    }

    /**
     * 获取订单总数
     *
     * @param dateLimit 时间端
     * @param status    String 状态
     * @return Integer
     */
    private Integer getCount(String dateLimit, Integer status, Integer merId) {
        //总数只计算时间
        QueryWrapper<StoreRefundOrder> queryWrapper = new QueryWrapper<>();
        if (merId > 0) {
            queryWrapper.eq("mer_id", merId);
        }
        if (StrUtil.isNotBlank(dateLimit)) {
            getRequestTimeWhere(queryWrapper, dateLimit);
        }
        getStatusWhere(queryWrapper, status);
        return dao.selectCount(queryWrapper);
    }

    /**
     * 获取request的where条件
     *
     * @param queryWrapper QueryWrapper<StoreOrder> 表达式
     * @param dateLimit    时间区间参数
     */
    private void getRequestTimeWhere(QueryWrapper<StoreRefundOrder> queryWrapper, String dateLimit) {
        dateLimitUtilVo dateLimitUtilVo = DateUtil.getDateLimit(dateLimit);
        queryWrapper.between("create_time", dateLimitUtilVo.getStartTime(), dateLimitUtilVo.getEndTime());
    }

    /**
     * 根据订单状态获取where条件
     *
     * @param queryWrapper QueryWrapper<StoreOrder> 表达式
     * @param status       Integer 类型 9-全部
     */
    private void getStatusWhere(QueryWrapper<StoreRefundOrder> queryWrapper, Integer status) {
        if (ObjectUtil.isNull(status)) {
            return;
        }
        switch (status) {
            case 9: //全部
                break;
            case OrderConstants.MERCHANT_REFUND_ORDER_STATUS_APPLY: //待审核
            case OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REJECT: //审核未通过
            case OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUNDING: //退款中
            case OrderConstants.MERCHANT_REFUND_ORDER_STATUS_REFUND: //已退款
                queryWrapper.eq("refund_status", status);
                break;
        }
    }
}

