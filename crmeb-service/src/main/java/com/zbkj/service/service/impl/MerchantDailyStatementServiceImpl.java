package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.bill.MerchantDailyStatement;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreOrderProfitSharing;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.MerchantDailyStatementDao;
import com.zbkj.service.service.MerchantDailyStatementService;
import com.zbkj.service.service.StoreOrderProfitSharingService;
import com.zbkj.service.service.StoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
*  MerchantDailyStatementServiceImpl 接口实现
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
public class MerchantDailyStatementServiceImpl extends ServiceImpl<MerchantDailyStatementDao, MerchantDailyStatement> implements MerchantDailyStatementService {

    @Resource
    private MerchantDailyStatementDao dao;

    @Autowired
    private StoreOrderProfitSharingService storeOrderProfitSharingService;
    @Autowired
    private StoreOrderService storeOrderService;

    /**
     * 查询某一天的所有商户日帐单
     * @param date 日期:年-月-日
     * @return List
     */
    @Override
    public List<MerchantDailyStatement> findByDate(String date) {
        LambdaQueryWrapper<MerchantDailyStatement> lqw = Wrappers.lambdaQuery();
        lqw.eq(MerchantDailyStatement::getDataDate, date);
        return dao.selectList(lqw);
    }

    /**
     * 获取某个月的所有帐单
     * @param month 月份：年-月
     * @return List
     */
    @Override
    public List<MerchantDailyStatement> findByMonth(String month) {
        LambdaQueryWrapper<MerchantDailyStatement> lqw = Wrappers.lambdaQuery();
        lqw.apply("date_format(data_date, '%Y-%m') = {0}", month);
        return dao.selectList(lqw);
    }

    /**
     * 分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MerchantDailyStatement> getPageList(String dateLimit, PageParamRequest pageParamRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Page<MerchantDailyStatement> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<MerchantDailyStatement> lqw = Wrappers.lambdaQuery();
        lqw.eq(MerchantDailyStatement::getMerId, systemAdmin.getMerId());
        if (StrUtil.isNotEmpty(dateLimit)){
            dateLimitUtilVo dateLimitVo = com.zbkj.common.utils.DateUtil.getDateLimit(dateLimit);
            String startDate = DateUtil.parse(dateLimitVo.getStartTime()).toDateStr();
            String endDate = DateUtil.parse(dateLimitVo.getEndTime()).toDateStr();
            lqw.between(MerchantDailyStatement::getDataDate, startDate, endDate);
        }
        lqw.orderByDesc(MerchantDailyStatement::getId);
        List<MerchantDailyStatement> list = dao.selectList(lqw);
        if (CollUtil.isEmpty(list)) {
            return CommonPage.copyPageInfo(page, list);
        }
        // 判断是否包含今天
        String today = DateUtil.date().toDateStr();
        for(MerchantDailyStatement dailyStatement : list) {
            if (!dailyStatement.getDataDate().equals(today)) {
                continue;
            }
            writeDailyStatement(dailyStatement);
        }
        return CommonPage.copyPageInfo(page, list);
    }

    /**
     * 为帐单写入数据
     * @param statement 帐单
     */
    private void writeDailyStatement(MerchantDailyStatement statement) {
        List<StoreOrder> storeOrderList = storeOrderService.findPayByDate(statement.getMerId(), statement.getDataDate());
        List<StoreOrderProfitSharing> sharingList = storeOrderProfitSharingService.findByDate(statement.getMerId(), statement.getDataDate());
        // 订单收入金额
        BigDecimal orderIncomeAmount = new BigDecimal("0.00");
        // 平台手续费
        BigDecimal handlingFee = new BigDecimal("0.00");
        // 商户退款金额
        BigDecimal refundAmount = new BigDecimal("0.00");
        // 退款笔数
        int refundNum = 0;
        if (CollUtil.isNotEmpty(sharingList)) {
            orderIncomeAmount = sharingList.stream().map(StoreOrderProfitSharing::getProfitSharingMerPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            handlingFee = sharingList.stream().map(StoreOrderProfitSharing::getProfitSharingPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            List<StoreOrderProfitSharing> refundSharingList = sharingList.stream().filter(e -> e.getStatus().equals(2)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(refundSharingList)) {
                refundAmount = refundSharingList.stream().map(StoreOrderProfitSharing::getProfitSharingMerPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                refundNum = refundSharingList.size();
            }
        }
        // 订单支付总金额
        BigDecimal orderPayAmount = new BigDecimal("0.00");
        // 订单支付笔数
        int orderNum = 0;
        if (CollUtil.isNotEmpty(storeOrderList)) {
            orderPayAmount = storeOrderList.stream().map(StoreOrder::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            orderNum = storeOrderList.size();
        }

        // 支出总金额
        BigDecimal payoutAmount = refundAmount;
        // 支出笔数
        int payoutNum = refundNum;
        // 商户日收支 = 订单收入金额 - 商户退款金额
        BigDecimal incomeExpenditure = orderIncomeAmount.subtract(refundAmount);

        statement.setOrderIncomeAmount(orderIncomeAmount);
        statement.setOrderPayAmount(orderPayAmount);
        statement.setOrderNum(orderNum);
        statement.setHandlingFee(handlingFee);
        statement.setPayoutAmount(payoutAmount);
        statement.setPayoutNum(payoutNum);
        statement.setRefundAmount(refundAmount);
        statement.setRefundNum(refundNum);
        statement.setIncomeExpenditure(incomeExpenditure);
    }
}

