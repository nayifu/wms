package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zbkj.common.constants.DateConstants;
import com.zbkj.common.constants.RedisConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.bill.MerchantDailyStatement;
import com.zbkj.common.model.bill.MerchantMonthStatement;
import com.zbkj.common.model.bill.PlatformDailyStatement;
import com.zbkj.common.model.bill.PlatformMonthStatement;
import com.zbkj.common.model.order.MasterOrder;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.order.StoreOrderProfitSharing;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.model.record.ProductDayRecord;
import com.zbkj.common.model.record.ShoppingProductDayRecord;
import com.zbkj.common.model.record.TradingDayRecord;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.common.vo.MyRecord;
import com.zbkj.service.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StatisticsTaskService 接口实现
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
public class StatisticsTaskServiceImpl implements StatisticsTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsTaskServiceImpl.class);

    @Autowired
    private ShoppingProductDayRecordService shoppingProductDayRecordService;
    @Autowired
    private ProductDayRecordService productDayRecordService;
    @Autowired
    private TradingDayRecordService tradingDayRecordService;
    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private StoreProductRelationService productRelationService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private StoreOrderService storeOrderService;
    @Autowired
    private StoreOrderInfoService storeOrderInfoService;
    @Autowired
    private StoreOrderStatusService storeOrderStatusService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private PlatformDailyStatementService platformDailyStatementService;
    @Autowired
    private PlatformMonthStatementService platformMonthStatementService;
    @Autowired
    private MerchantDailyStatementService merchantDailyStatementService;
    @Autowired
    private MerchantMonthStatementService merchantMonthStatementService;
    @Autowired
    private MasterOrderService masterOrderService;
    @Autowired
    private StoreOrderProfitSharingService storeOrderProfitSharingService;
    @Autowired
    private StoreRefundOrderService storeRefundOrderService;

    /**
     * 每天零点的自动统计前一天的数据
     */
    @Override
    public void autoStatistics() {
        // 获取昨天的日期
        String yesterdayStr = DateUtil.yesterday().toString(DateConstants.DATE_FORMAT_DATE);


       //  ========================================
       //  商品商城部分
       //  ========================================

        // 商品新增量
        Integer newProductNum = storeProductService.getNewProductByDate(yesterdayStr);

        // 浏览量
        int pageViewNum = 0;
        Object pageViewObject = redisUtil.get(RedisConstants.PRO_PAGE_VIEW_KEY + yesterdayStr);
        if (ObjectUtil.isNotNull(pageViewObject)) {
            pageViewNum = (Integer) pageViewObject;
        }

        // 收藏量
        Integer collectNum = productRelationService.getCountByDate(yesterdayStr);

        // 加购量
        int addCartNum = 0;
        Object addCartObject = redisUtil.get(RedisConstants.PRO_ADD_CART_KEY + yesterdayStr);
        if (ObjectUtil.isNotNull(addCartObject)) {
            addCartNum = (Integer) addCartObject;
        }

        // 交易总件数
        Integer orderProductNum = storeOrderService.getOrderProductNumByDate(yesterdayStr);

        // 交易成功件数
        Integer orderSuccessProductNum = storeOrderService.getOrderSuccessProductNumByDate(yesterdayStr);

        // 保存商品统计数据（商城每日）
        ShoppingProductDayRecord shoppingProductDayRecord = new ShoppingProductDayRecord();
        shoppingProductDayRecord.setDate(yesterdayStr);
        shoppingProductDayRecord.setAddProductNum(newProductNum);
        shoppingProductDayRecord.setPageView(pageViewNum);
        shoppingProductDayRecord.setCollectNum(collectNum);
        shoppingProductDayRecord.setAddCartNum(addCartNum);
        shoppingProductDayRecord.setOrderProductNum(orderProductNum);
        shoppingProductDayRecord.setOrderSuccessProductNum(orderSuccessProductNum);

        //  ========================================
        //  商品个体部分
        //  ========================================

        // 获取所有未删除的商品
        List<StoreProduct> allProduct = storeProductService.findAllProductByNotDelte();
        List<ProductDayRecord> productDayRecordList = allProduct.stream().map(e -> {
            ProductDayRecord productDayRecord = new ProductDayRecord();
            productDayRecord.setProductId(e.getId());
            productDayRecord.setMerId(e.getMerId());
            productDayRecord.setDate(yesterdayStr);
            return productDayRecord;
        }).collect(Collectors.toList());

        productDayRecordList.forEach(e -> {
            // 浏览量
            int pageViewDetailNum = 0;
            Object pageViewObject1 = redisUtil.get(StrUtil.format(RedisConstants.PRO_PRO_PAGE_VIEW_KEY, yesterdayStr, e.getProductId()));
            if (ObjectUtil.isNotNull(pageViewObject1)) {
                pageViewDetailNum = (Integer) pageViewObject1;
            }
            e.setPageView(pageViewDetailNum);
            // 收藏量
            Integer collectDetailNum = productRelationService.getCountByDateAndProId(yesterdayStr, e.getProductId());
            e.setCollectNum(collectDetailNum);
            // 加购件数
            int addCartDetailNum = 0;
            Object addCartObject1 = redisUtil.get(StrUtil.format(RedisConstants.PRO_PRO_ADD_CART_KEY, yesterdayStr, e.getProductId()));
            if (ObjectUtil.isNotNull(addCartObject1)) {
                addCartDetailNum = (Integer) addCartObject1;
            }
            e.setAddCartNum(addCartDetailNum);
            // 销量
            e.setOrderProductNum(storeOrderInfoService.getSalesNumByDateAndProductId(yesterdayStr, e.getProductId()));
            // 销售额
            e.setOrderSuccessProductFee(storeOrderInfoService.getSalesByDateAndProductId(yesterdayStr, e.getProductId()));
        });

        //  ========================================
        //  交易记录/日
        //  ========================================

        // 订单数量
        Integer orderNum = storeOrderService.getOrderNumByDate(0, yesterdayStr);

        // 订单支付数量
        Integer payOrderNum = storeOrderService.getPayOrderNumByDate(yesterdayStr);

        // 订单支付金额
        BigDecimal payOrderAmount = storeOrderService.getPayOrderAmountByDate(0, yesterdayStr);

        // 订单退款数量
        Integer refundOrderNum = storeRefundOrderService.getRefundOrderNumByDate(yesterdayStr);

        // 订单退款金额
        BigDecimal refundOrderAmount = storeRefundOrderService.getRefundOrderAmountByDate(yesterdayStr);

        TradingDayRecord tradingDayRecord = new TradingDayRecord();
        tradingDayRecord.setDate(yesterdayStr);
        tradingDayRecord.setProductOrderNum(orderNum);
        tradingDayRecord.setProductOrderPayNum(payOrderNum);
        tradingDayRecord.setProductOrderPayFee(payOrderAmount);
        tradingDayRecord.setProductOrderRefundNum(refundOrderNum);
        tradingDayRecord.setProductOrderRefundFee(refundOrderAmount);

        Boolean execute = transactionTemplate.execute(e -> {
            shoppingProductDayRecordService.save(shoppingProductDayRecord);
            productDayRecordService.saveBatch(productDayRecordList, 100);
            tradingDayRecordService.save(tradingDayRecord);
            return Boolean.TRUE;
        });
        if (!execute) {
            LOGGER.error("每日统计任务保存数据库失败");
            throw new CrmebException("每日统计任务保存数据库失败");
        }
        redisUtil.delete(RedisConstants.PRO_PAGE_VIEW_KEY + yesterdayStr);
        redisUtil.delete(RedisConstants.PRO_ADD_CART_KEY + yesterdayStr);
    }

    /**
     * 每日帐单定时任务
     * 每天1点执行
     *
     * 生成当天数据记录，值为0
     * 为上一天的数据记录，计入数据
     */
    @Override
    public void dailyStatement() {
        List<Integer> merIdList = merchantService.getAllId();
        // 写入记录
        MyRecord record = writeDailyStatement();
        Integer status = record.getInt("status");
        if (status.equals(0)) {
            // 初始化当天数据
            initDailyStatement(merIdList);
            return;
        }
        PlatformDailyStatement platformDailyStatement = record.get("platformDailyStatement");
        Integer listSize = record.getInt("listSize");
        transactionTemplate.execute(e -> {
            boolean update = platformDailyStatementService.updateById(platformDailyStatement);
            if (!update) {
                LOGGER.error("每日帐单定时任务,更新平台日帐单失败,触发回滚");
                e.setRollbackOnly();
                return Boolean.FALSE;
            }
            if (listSize > 0) {
                List<MerchantDailyStatement> merchantDailyStatementList = record.get("merchantDailyStatementList");
                update = merchantDailyStatementService.updateBatchById(merchantDailyStatementList, 100);
                if (!update) {
                    LOGGER.error("每日帐单定时任务,批量更新商户日帐单失败,触发回滚");
                    e.setRollbackOnly();
                    return Boolean.FALSE;
                }
            }
            // 初始化当天数据
            update = initDailyStatement(merIdList);
            if (!update) {
                LOGGER.error("每日帐单定时任务,初始化当天数据,触发回滚");
                e.setRollbackOnly();
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        });

    }

    /**
     * 每月帐单定时任务
     * 每月第一天2点执行
     * 获取上个月所有的帐单记录，然后通过累加获取月帐单记录
     * 生成当月帐单记录，数值为0
     */
    @Override
    public void monthStatement() {
        String lastMonth = DateUtil.lastMonth().toString(DateConstants.DATE_FORMAT_MONTH);
        List<Integer> merIdList = merchantService.getAllId();
        PlatformMonthStatement platformMonthStatement = platformMonthStatementService.getByMonth(lastMonth);
        if (ObjectUtil.isNull(platformMonthStatement)) {
            return;
        }
        writePlatformMonthStatement(platformMonthStatement);
        List<MerchantMonthStatement> merchantMonthStatementList = merchantMonthStatementService.findByMonth(lastMonth);
        if (CollUtil.isEmpty(merchantMonthStatementList)) {
            transactionTemplate.execute(e -> {
                boolean save = platformMonthStatementService.updateById(platformMonthStatement);
                if (!save) {
                    LOGGER.error("每月帐单定时任务,更新平台月帐单失败,触发回滚");
                    e.setRollbackOnly();
                    return Boolean.FALSE;
                }
                save = initMonthStatement(merIdList);
                if (!save) {
                    LOGGER.error("每月帐单定时任务,初始化平台当月帐单失败,触发回滚");
                    e.setRollbackOnly();
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            });
            return;
        }
        writeMerchantMonthStatement(merchantMonthStatementList, lastMonth);
        transactionTemplate.execute(e -> {
            boolean save = platformMonthStatementService.updateById(platformMonthStatement);
            if (!save) {
                LOGGER.error("每月帐单定时任务,更新平台月帐单失败,触发回滚");
                e.setRollbackOnly();
                return Boolean.FALSE;
            }
            save = merchantMonthStatementService.updateBatchById(merchantMonthStatementList, 100);
            if (!save) {
                LOGGER.error("每月帐单定时任务,更新商户月帐单失败,触发回滚");
                e.setRollbackOnly();
                return Boolean.FALSE;
            }
            save = initMonthStatement(merIdList);
            if (!save) {
                LOGGER.error("每月帐单定时任务,初始化平台当月帐单失败,触发回滚");
                e.setRollbackOnly();
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        });
    }

    private Boolean initMonthStatement(List<Integer> merIdList) {
        String nowMonth = DateUtil.date().toString(DateConstants.DATE_FORMAT_MONTH);
        PlatformMonthStatement platformMonthStatement = new PlatformMonthStatement();
        platformMonthStatement.setDataDate(nowMonth);
        if (CollUtil.isEmpty(merIdList)) {
            return platformMonthStatementService.save(platformMonthStatement);
        }
        List<MerchantMonthStatement> merchantMonthStatementList = CollUtil.newArrayList();
        if (CollUtil.isNotEmpty(merIdList)) {
            merIdList.forEach(merId -> {
                MerchantMonthStatement merchantMonthStatement = new MerchantMonthStatement();
                merchantMonthStatement.setMerId(merId);
                merchantMonthStatement.setDataDate(nowMonth);
                merchantMonthStatementList.add(merchantMonthStatement);
            });
        }
        boolean save = platformMonthStatementService.save(platformMonthStatement);
        if (!save) {
            return save;
        }
        return merchantMonthStatementService.saveBatch(merchantMonthStatementList, 100);
    }

    private void writeMerchantMonthStatement(List<MerchantMonthStatement> merchantMonthStatementList, String lastMonth) {
        List<MerchantDailyStatement> merchantDailyStatementList = merchantDailyStatementService.findByMonth(lastMonth);
        if (CollUtil.isEmpty(merchantDailyStatementList)) {
            return;
        }
        merchantMonthStatementList.forEach(monthStatement -> {
            List<MerchantDailyStatement> dailyStatementList = merchantDailyStatementList.stream().filter(e -> e.getMerId().equals(monthStatement.getMerId())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(dailyStatementList)) {
                BigDecimal orderIncomeAmount = dailyStatementList.stream().map(MerchantDailyStatement::getOrderIncomeAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal orderPayAmount = dailyStatementList.stream().map(MerchantDailyStatement::getOrderPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                int orderNum = dailyStatementList.stream().mapToInt(MerchantDailyStatement::getOrderNum).sum();
                BigDecimal handlingFee = dailyStatementList.stream().map(MerchantDailyStatement::getHandlingFee).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal payoutAmount = dailyStatementList.stream().map(MerchantDailyStatement::getPayoutAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                int payoutNum = dailyStatementList.stream().mapToInt(MerchantDailyStatement::getPayoutNum).sum();
                BigDecimal refundAmount = dailyStatementList.stream().map(MerchantDailyStatement::getRefundAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                int refundNum = dailyStatementList.stream().mapToInt(MerchantDailyStatement::getRefundNum).sum();
                BigDecimal incomeExpenditure = dailyStatementList.stream().map(MerchantDailyStatement::getIncomeExpenditure).reduce(BigDecimal.ZERO, BigDecimal::add);
                monthStatement.setOrderIncomeAmount(orderIncomeAmount);
                monthStatement.setOrderPayAmount(orderPayAmount);
                monthStatement.setOrderNum(orderNum);
                monthStatement.setHandlingFee(handlingFee);
                monthStatement.setPayoutAmount(payoutAmount);
                monthStatement.setPayoutNum(payoutNum);
                monthStatement.setRefundAmount(refundAmount);
                monthStatement.setRefundNum(refundNum);
                monthStatement.setIncomeExpenditure(incomeExpenditure);
            }
        });
    }

    private void writePlatformMonthStatement(PlatformMonthStatement platformMonthStatement) {
        List<PlatformDailyStatement> dailyStatementList = platformDailyStatementService.findByMonth(platformMonthStatement.getDataDate());
        if (CollUtil.isEmpty(dailyStatementList)) {
            return;
        }
        BigDecimal orderPayAmount = dailyStatementList.stream().map(PlatformDailyStatement::getOrderPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalOrderNum = dailyStatementList.stream().mapToInt(PlatformDailyStatement::getTotalOrderNum).sum();
        int merchantOrderNum = dailyStatementList.stream().mapToInt(PlatformDailyStatement::getMerchantOrderNum).sum();
        BigDecimal handlingFee = dailyStatementList.stream().map(PlatformDailyStatement::getHandlingFee).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal payoutAmount = dailyStatementList.stream().map(PlatformDailyStatement::getPayoutAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        int payoutNum = dailyStatementList.stream().mapToInt(PlatformDailyStatement::getPayoutNum).sum();
        BigDecimal merchantTransferAmount = dailyStatementList.stream().map(PlatformDailyStatement::getMerchantTransferAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        int merchantTransferNum = dailyStatementList.stream().mapToInt(PlatformDailyStatement::getMerchantTransferNum).sum();
        BigDecimal refundAmount = dailyStatementList.stream().map(PlatformDailyStatement::getRefundAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        int refundNum = dailyStatementList.stream().mapToInt(PlatformDailyStatement::getRefundNum).sum();
        BigDecimal incomeExpenditure = dailyStatementList.stream().map(PlatformDailyStatement::getIncomeExpenditure).reduce(BigDecimal.ZERO, BigDecimal::add);
        platformMonthStatement.setOrderPayAmount(orderPayAmount);
        platformMonthStatement.setTotalOrderNum(totalOrderNum);
        platformMonthStatement.setMerchantOrderNum(merchantOrderNum);
        platformMonthStatement.setHandlingFee(handlingFee);
        platformMonthStatement.setPayoutAmount(payoutAmount);
        platformMonthStatement.setPayoutNum(payoutNum);
        platformMonthStatement.setMerchantTransferAmount(merchantTransferAmount);
        platformMonthStatement.setMerchantTransferNum(merchantTransferNum);
        platformMonthStatement.setRefundAmount(refundAmount);
        platformMonthStatement.setRefundNum(refundNum);
        platformMonthStatement.setIncomeExpenditure(incomeExpenditure);
    }

    /**
     * 写入记录
     * @return MyRecord
     */
    private MyRecord writeDailyStatement() {
        MyRecord myRecord = new MyRecord();
        /*为上一天的记录写入数据*/
        String yesterday = DateUtil.yesterday().toDateStr();
        // 平台帐单
        PlatformDailyStatement platformDailyStatement = platformDailyStatementService.getByDate(yesterday);
        if (ObjectUtil.isNull(platformDailyStatement)) {
            myRecord.set("status", 0);
            return myRecord;
        }
        List<MasterOrder> masterOrderList = masterOrderService.findPayByDate(yesterday);
        List<StoreOrderProfitSharing> sharingList = storeOrderProfitSharingService.findByDate(0, yesterday);
        // 平台记录写入
        platformDataWrite(platformDailyStatement, masterOrderList, sharingList);
        myRecord.set("status", 1);
        myRecord.set("platformDailyStatement", platformDailyStatement);
        // 商户记录写入
        List<MerchantDailyStatement> merchantDailyStatementList = merchantDailyStatementService.findByDate(yesterday);
        if (CollUtil.isEmpty(merchantDailyStatementList)) {
            myRecord.set("listSize", 0);
            return myRecord;
        }
        merchantDataWrite(merchantDailyStatementList, sharingList);
        myRecord.set("listSize", merchantDailyStatementList.size());
        myRecord.set("merchantDailyStatementList", merchantDailyStatementList);
        return myRecord;
    }

    /**
     * 商户记录写入
     * @param merchantDailyStatementList 商户日账单列表
     * @param sharingList 分账列表
     */
    private void merchantDataWrite(List<MerchantDailyStatement> merchantDailyStatementList, List<StoreOrderProfitSharing> sharingList) {
        if (CollUtil.isEmpty(merchantDailyStatementList)) {
            return;
        }
        merchantDailyStatementList.forEach(statement -> {
            List<StoreOrder> storeOrderList = storeOrderService.findPayByDate(statement.getMerId(), statement.getDataDate());
            // 订单收入金额
            BigDecimal orderIncomeAmount = BigDecimal.ZERO;
            // 平台手续费
            BigDecimal handlingFee = BigDecimal.ZERO;
            // 商户退款金额
            BigDecimal refundAmount = BigDecimal.ZERO;
            // 退款笔数
            int refundNum = 0;
            if (CollUtil.isNotEmpty(sharingList)) {
                List<StoreOrderProfitSharing> merchantSharingList = sharingList.stream().filter(e -> e.getMerId().equals(statement.getMerId())).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(merchantSharingList)) {
                    orderIncomeAmount = merchantSharingList.stream().map(StoreOrderProfitSharing::getProfitSharingMerPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                    handlingFee = merchantSharingList.stream().map(StoreOrderProfitSharing::getProfitSharingPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                }
                List<StoreOrderProfitSharing> refundSharingList = merchantSharingList.stream().filter(e -> e.getStatus().equals(2)).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(refundSharingList)) {
                    refundAmount = refundSharingList.stream().map(StoreOrderProfitSharing::getProfitSharingMerPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                    refundNum = refundSharingList.size();
                }
            }
            // 订单支付总金额
            BigDecimal orderPayAmount = BigDecimal.ZERO;
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
        });
    }

    /**
     * 平台记录写入
     * @param platformDailyStatement 平台日帐单
     * @param masterOrderList 主订单列表
     * @param sharingList 分账列表
     */
    private void platformDataWrite(PlatformDailyStatement platformDailyStatement, List<MasterOrder> masterOrderList, List<StoreOrderProfitSharing> sharingList) {
        // 订单支付总金额
        BigDecimal orderPayAmount = BigDecimal.ZERO;
        // 总订单支付笔数
        int orderPayNum = 0;
        if (CollUtil.isNotEmpty(masterOrderList)) {
            orderPayAmount = masterOrderList.stream().map(MasterOrder::getPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            orderPayNum = masterOrderList.size();
        }
        // 分订单支付笔数
        int merchantOrderPayNum = 0;
        merchantOrderPayNum = storeOrderService.findPayNumByDate(0, platformDailyStatement.getDataDate());
        // 日手续费收入
        BigDecimal handlingFee = BigDecimal.ZERO;
        // 商户分账金额
        BigDecimal merchantTransferAmount = BigDecimal.ZERO;
        // 商户分账笔数
        int merchantTransferNum = 0;
        // 平台退款金额
        BigDecimal refundAmount = BigDecimal.ZERO;
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

        platformDailyStatement.setOrderPayAmount(orderPayAmount);
        platformDailyStatement.setTotalOrderNum(orderPayNum);
        platformDailyStatement.setMerchantOrderNum(merchantOrderPayNum);
        platformDailyStatement.setHandlingFee(handlingFee);
        platformDailyStatement.setPayoutAmount(payoutAmount);
        platformDailyStatement.setPayoutNum(payoutNum);
        platformDailyStatement.setMerchantTransferAmount(merchantTransferAmount);
        platformDailyStatement.setMerchantTransferNum(merchantTransferNum);
        platformDailyStatement.setRefundAmount(refundAmount);
        platformDailyStatement.setRefundNum(refundOrderNum);
        platformDailyStatement.setIncomeExpenditure(incomeExpenditure);
    }

    /**
     * 初始化当天记录（数值为0，为分页查询设计）
     */
    private Boolean initDailyStatement(List<Integer> merIdList) {
        String today = DateUtil.date().toDateStr();
        PlatformDailyStatement platformDailyStatement = new PlatformDailyStatement();
        platformDailyStatement.setDataDate(today);
        if (CollUtil.isEmpty(merIdList)) {
            return platformDailyStatementService.save(platformDailyStatement);
        }
        List<MerchantDailyStatement> merchantDailyStatementList = merIdList.stream().map(merId -> {
            MerchantDailyStatement statement = new MerchantDailyStatement();
            statement.setMerId(merId);
            statement.setDataDate(today);
            return statement;
        }).collect(Collectors.toList());
        boolean save = platformDailyStatementService.save(platformDailyStatement);
        if (!save) {
            return save;
        }
        return merchantDailyStatementService.saveBatch(merchantDailyStatementList, 100);
    }
}
