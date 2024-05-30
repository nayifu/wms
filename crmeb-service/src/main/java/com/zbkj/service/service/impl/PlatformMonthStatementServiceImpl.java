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
import com.zbkj.common.constants.DateConstants;
import com.zbkj.common.model.bill.PlatformMonthStatement;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrderProfitSharing;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.PlatformMonthStatementDao;
import com.zbkj.service.service.MasterOrderService;
import com.zbkj.service.service.PlatformMonthStatementService;
import com.zbkj.service.service.StoreOrderProfitSharingService;
import com.zbkj.service.service.StoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
*  PlatformMonthStatementServiceImpl 接口实现
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
public class PlatformMonthStatementServiceImpl extends ServiceImpl<PlatformMonthStatementDao, PlatformMonthStatement> implements PlatformMonthStatementService {

    @Resource
    private PlatformMonthStatementDao dao;

    @Autowired
    private MasterOrderService masterOrderService;
    @Autowired
    private StoreOrderProfitSharingService storeOrderProfitSharingService;
    @Autowired
    private StoreOrderService storeOrderService;

    /**
     * 获取某一月的数据
     * @param month 月份：年-月
     * @return PlatformMonthStatement
     */
    @Override
    public PlatformMonthStatement getByMonth(String month) {
        LambdaQueryWrapper<PlatformMonthStatement> lqw = Wrappers.lambdaQuery();
        lqw.eq(PlatformMonthStatement::getDataDate, month);
        lqw.last("limit 1");
        return dao.selectOne(lqw);
    }

    /**
     * 分页列表
     * @param dateLimit 时间参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<PlatformMonthStatement> getPageList(String dateLimit, PageParamRequest pageParamRequest) {
        Page<PlatformMonthStatement> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<PlatformMonthStatement> lqw = Wrappers.lambdaQuery();
        if (StrUtil.isNotEmpty(dateLimit)){
            dateLimitUtilVo dateLimitVo = com.zbkj.common.utils.DateUtil.getMonthLimit(dateLimit);
            String startMonth = dateLimitVo.getStartTime();
            String endMonth = dateLimitVo.getEndTime();
            lqw.between(PlatformMonthStatement::getDataDate, startMonth, endMonth);
        }
        lqw.orderByDesc(PlatformMonthStatement::getId);
        List<PlatformMonthStatement> list = dao.selectList(lqw);
        if (CollUtil.isEmpty(list)) {
            return CommonPage.copyPageInfo(page, list);
        }
        // 判断是否包含本月
        String nowMonth = DateUtil.date().toString(DateConstants.DATE_FORMAT_MONTH);
        for(PlatformMonthStatement monthStatement : list) {
            if (!monthStatement.getDataDate().equals(nowMonth)) {
                continue;
            }
            writeMonthStatement(monthStatement);
        }
        return CommonPage.copyPageInfo(page, list);

    }

    /**
     * 写入本月数据
     * @param monthStatement 本月帐单
     */
    private void writeMonthStatement(PlatformMonthStatement monthStatement) {
        List<MasterOrder> masterOrderList = masterOrderService.findPayByMonth(monthStatement.getDataDate());
        List<StoreOrderProfitSharing> sharingList = storeOrderProfitSharingService.findByMonth(0, monthStatement.getDataDate());
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
        merchantOrderPayNum = storeOrderService.findPayNumByMonth(0, monthStatement.getDataDate());
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

        monthStatement.setOrderPayAmount(orderPayAmount);
        monthStatement.setTotalOrderNum(orderPayNum);
        monthStatement.setMerchantOrderNum(merchantOrderPayNum);
        monthStatement.setHandlingFee(handlingFee);
        monthStatement.setPayoutAmount(payoutAmount);
        monthStatement.setPayoutNum(payoutNum);
        monthStatement.setMerchantTransferAmount(merchantTransferAmount);
        monthStatement.setMerchantTransferNum(merchantTransferNum);
        monthStatement.setRefundAmount(refundAmount);
        monthStatement.setRefundNum(refundOrderNum);
        monthStatement.setIncomeExpenditure(incomeExpenditure);
    }
}

