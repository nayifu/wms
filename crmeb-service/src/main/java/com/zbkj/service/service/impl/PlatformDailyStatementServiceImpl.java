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
import com.zbkj.common.model.bill.PlatformDailyStatement;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrderProfitSharing;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.PlatformDailyStatementDao;
import com.zbkj.service.service.MasterOrderService;
import com.zbkj.service.service.PlatformDailyStatementService;
import com.zbkj.service.service.StoreOrderProfitSharingService;
import com.zbkj.service.service.StoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
*  PlatformDailyStatementServiceImpl 接口实现
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
public class PlatformDailyStatementServiceImpl extends ServiceImpl<PlatformDailyStatementDao, PlatformDailyStatement> implements PlatformDailyStatementService {

    @Resource
    private PlatformDailyStatementDao dao;

    @Autowired
    private MasterOrderService masterOrderService;
    @Autowired
    private StoreOrderProfitSharingService storeOrderProfitSharingService;
    @Autowired
    private StoreOrderService storeOrderService;

    /**
     * 通过日期获取记录
     * @param date 日期：年-月-日
     */
    @Override
    public PlatformDailyStatement getByDate(String date) {
        LambdaQueryWrapper<PlatformDailyStatement> lqw = Wrappers.lambdaQuery();
        lqw.eq(PlatformDailyStatement::getDataDate, date);
        lqw.last(" limit 1");
        return dao.selectOne(lqw);
    }

    /**
     * 通过月份获取所有记录
     * @param month 月份：年-月
     * @return List
     */
    @Override
    public List<PlatformDailyStatement> findByMonth(String month) {
        LambdaQueryWrapper<PlatformDailyStatement> lqw = Wrappers.lambdaQuery();
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
    public PageInfo<PlatformDailyStatement> getPageList(String dateLimit, PageParamRequest pageParamRequest) {
        Page<PlatformDailyStatement> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<PlatformDailyStatement> lqw = Wrappers.lambdaQuery();
        if (StrUtil.isNotEmpty(dateLimit)){
            dateLimitUtilVo dateLimitVo = com.zbkj.common.utils.DateUtil.getDateLimit(dateLimit);
            String startDate = DateUtil.parse(dateLimitVo.getStartTime()).toDateStr();
            String endDate = DateUtil.parse(dateLimitVo.getEndTime()).toDateStr();
            lqw.between(PlatformDailyStatement::getDataDate, startDate, endDate);
        }
        lqw.orderByDesc(PlatformDailyStatement::getId);
        List<PlatformDailyStatement> list = dao.selectList(lqw);
        if (CollUtil.isEmpty(list)) {
            return CommonPage.copyPageInfo(page, list);
        }
        // 判断是否包含今天
        String today = DateUtil.date().toDateStr();
        for(PlatformDailyStatement dailyStatement : list) {
            if (!dailyStatement.getDataDate().equals(today)) {
                continue;
            }
            writeDailyStatement(dailyStatement);
        }
        return CommonPage.copyPageInfo(page, list);
    }

    /**
     * 为帐单写入数据
     * @param dailyStatement 帐单
     */
    private void writeDailyStatement(PlatformDailyStatement dailyStatement) {
        // 为今天的帐单计算数据
        List<MasterOrder> masterOrderList = masterOrderService.findPayByDate(dailyStatement.getDataDate());
        List<StoreOrderProfitSharing> sharingList = storeOrderProfitSharingService.findByDate(0, dailyStatement.getDataDate());
        // 订单支付总金额
        BigDecimal orderPayAmount = new BigDecimal("0.00");
        // 总订单支付笔数
        int orderPayNum = 0;
        if (CollUtil.isNotEmpty(masterOrderList)) {
            orderPayAmount = masterOrderList.stream().map(MasterOrder::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            orderPayNum = masterOrderList.size();
        }
        // 分订单支付笔数
        int merchantOrderPayNum = 0;
        merchantOrderPayNum = storeOrderService.findPayNumByDate(0, dailyStatement.getDataDate());
        // 日手续费收入
        BigDecimal handlingFee = new BigDecimal("0.00");
        // 商户分账金额
        BigDecimal merchantTransferAmount = new BigDecimal("0.00");
        // 商户分账笔数
        int merchantTransferNum = 0;
        // 平台退款金额
        BigDecimal refundAmount = new BigDecimal("0.00");
        // 退款笔数
        int refundOrderNum = 0;
        if (CollUtil.isNotEmpty(sharingList)) {
            handlingFee = sharingList.stream().map(StoreOrderProfitSharing::getProfitSharingPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            merchantTransferAmount = sharingList.stream().map(StoreOrderProfitSharing::getProfitSharingMerPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            merchantTransferNum = sharingList.size();
            List<StoreOrderProfitSharing> refundSharingList = sharingList.stream().filter(e -> e.getStatus().equals(2)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(refundSharingList)) {
                refundAmount = refundSharingList.stream().map(StoreOrderProfitSharing::getProfitSharingPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                refundOrderNum = refundSharingList.size();
            }
        }

        // 支出总金额 = 商户分账金额 + 平台退款金额
        BigDecimal payoutAmount = merchantTransferAmount.add(refundAmount);
        // 支出笔数
        int payoutNum = merchantTransferNum + refundOrderNum;

        // 平台日收支 = 日手续费收入 - 平台退款金额
        BigDecimal incomeExpenditure = handlingFee.subtract(refundAmount);

        dailyStatement.setOrderPayAmount(orderPayAmount);
        dailyStatement.setTotalOrderNum(orderPayNum);
        dailyStatement.setMerchantOrderNum(merchantOrderPayNum);
        dailyStatement.setHandlingFee(handlingFee);
        dailyStatement.setPayoutAmount(payoutAmount);
        dailyStatement.setPayoutNum(payoutNum);
        dailyStatement.setMerchantTransferAmount(merchantTransferAmount);
        dailyStatement.setMerchantTransferNum(merchantTransferNum);
        dailyStatement.setRefundAmount(refundAmount);
        dailyStatement.setRefundNum(refundOrderNum);
        dailyStatement.setIncomeExpenditure(incomeExpenditure);
    }
}

