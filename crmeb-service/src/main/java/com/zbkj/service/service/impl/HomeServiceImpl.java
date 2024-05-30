package com.zbkj.service.service.impl;

import com.zbkj.common.constants.DateConstants;
import com.zbkj.common.model.order.StoreOrder;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.model.user.User;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.service.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户表 服务实现类
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
public class HomeServiceImpl implements HomeService {

    @Autowired
    private StoreOrderService storeOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserVisitRecordService userVisitRecordService;
    @Autowired
    private StoreProductService storeProductService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private StoreRefundOrderService storeRefundOrderService;

    /**
     * 按开始结束时间查询每日新增订单数量
     *
     * @param list List<StoreOrder> 时间范围
     * @return HashMap<String, Object>
     * @author Mr.Zhang
     * @since 2020-05-16
     */
    private Map<Object, Object> getOrderCountGroupByDate(List<StoreOrder> list) {
        Map<Object, Object> map = new HashMap<>();

        if (list.size() < 1) {
            return map;
        }

        for (StoreOrder storeOrder : list) {
            map.put(storeOrder.getOrderNo(), storeOrder.getId());
        }

        return map;
    }

    /**
     * 按开始结束时间查询每日新增订单销售额
     *
     * @param list List<StoreOrder> 时间范围
     * @return HashMap<String, Object>
     * @author Mr.Zhang
     * @since 2020-05-16
     */
    private Map<Object, Object> getOrderPriceGroupByDate(List<StoreOrder> list) {
        Map<Object, Object> map = new HashMap<>();

        if (list.size() < 1) {
            return map;
        }

        for (StoreOrder storeOrder : list) {
            map.put(storeOrder.getOrderNo(), storeOrder.getPayPrice());
        }

        return map;
    }

    /**
     * 日期和数量格式化
     *
     * @return Map<String, Integer>
     * @author Mr.Zhang
     * @since 2020-05-16
     */
    private Map<Object, Object> dataFormat(Map<Object, Object> countGroupDate, String dateLimit) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<String> listDate = DateUtil.getListDate(dateLimit);

        String[] weekList = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        int i = 0;
        for (String date : listDate) {
            Object count = 0;
            if (countGroupDate.containsKey(date)) {
                count = countGroupDate.get(date);
            }
            String key;

            //周格式化
            switch (dateLimit) {
                //格式化周
                case DateConstants.SEARCH_DATE_WEEK:
                case DateConstants.SEARCH_DATE_PRE_WEEK:
                    key = weekList[i];
                    break;
                //格式化月
                case DateConstants.SEARCH_DATE_PRE_MONTH:
                case DateConstants.SEARCH_DATE_MONTH:
                    key = i + 1 + "";
                    break;
                //默认显示两位日期
                default:
                    key = date.substring(5, 10);
            }
            map.put(key, count);
            i++;
        }
        return map;
    }

    /**
     * 日期和数量格式化
     *
     * @return Map<String, Integer>
     * @author Mr.Zhang
     * @since 2020-05-16
     */
    private Map<Object, Object> dataFormatYear(Map<Object, Object> countGroupDate, String dateLimit) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<Object> listDate = new ArrayList<>();
        String year = "";
        if (dateLimit.equals(DateConstants.SEARCH_DATE_YEAR)) {
            year = DateUtil.nowYear();
        }

        if (dateLimit.equals(DateConstants.SEARCH_DATE_PRE_YEAR)) {
            year = DateUtil.lastYear();
        }

        //处理年
        //12个月份数据
        for (int i = 1; i <= 12; i++) {
            String month = i + "";
            if (i < 10) {
                month = "0" + i;
            }
            listDate.add(year + "-" + month);
        }

        String[] monthList = new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

        int i = 0;
        for (Object date : listDate) {
            Object count = 0;
            if (countGroupDate.containsKey(date)) {
                count = countGroupDate.get(date);
            }
            map.put(monthList[i], count);
            i++;
        }
        return map;
    }

    /**
     * 首页数据
     *
     * @return HomeRateResponse
     */
    @Override
    public HomeRateResponse indexMerchantDate() {
        String today = cn.hutool.core.date.DateUtil.date().toString(DateConstants.DATE_FORMAT_DATE);
        String yesterday = cn.hutool.core.date.DateUtil.yesterday().toString(DateConstants.DATE_FORMAT_DATE);
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        HomeRateResponse response = new HomeRateResponse();
        response.setSales(storeOrderService.getPayOrderAmountByDate(systemAdmin.getMerId(), today));
        response.setYesterdaySales(storeOrderService.getPayOrderAmountByDate(systemAdmin.getMerId(), yesterday));
        response.setOrderNum(storeOrderService.getOrderNumByDate(systemAdmin.getMerId(), today));
        response.setYesterdayOrderNum(storeOrderService.getOrderNumByDate(systemAdmin.getMerId(), yesterday));
        return response;
    }

    /**
     * 经营数据：
     * 1.待发货订单，2.退款中订单，4.库存预警
     * 5.上架商品数，6.库存中商品数
     *
     * @return HomeOperatingMerDataResponse
     */
    @Override
    public HomeOperatingMerDataResponse operatingMerchantData() {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        HomeOperatingMerDataResponse response = new HomeOperatingMerDataResponse();
        response.setNotShippingOrderNum(storeOrderService.getNotShippingNum(systemAdmin.getMerId()));
        response.setRefundingOrderNum(storeRefundOrderService.getAwaitAuditNum(systemAdmin.getMerId()));
        response.setVigilanceInventoryNum(storeProductService.getVigilanceInventoryNum(systemAdmin.getMerId()));
        response.setOnSaleProductNum(storeProductService.getOnSaleNum(systemAdmin.getMerId()));
        response.setNotSaleProductNum(storeProductService.getNotSaleNum(systemAdmin.getMerId()));
        return response;
    }

    /**
     * 平台端首页数据
     *
     * @return PlatformHomeRateResponse
     */
    @Override
    public PlatformHomeRateResponse indexPlatformDate() {
        String today = cn.hutool.core.date.DateUtil.date().toString(DateConstants.DATE_FORMAT_DATE);
        String yesterday = cn.hutool.core.date.DateUtil.yesterday().toString(DateConstants.DATE_FORMAT_DATE);
        PlatformHomeRateResponse response = new PlatformHomeRateResponse();
        response.setSales(storeOrderService.getPayOrderAmountByDate(0, today));
        response.setYesterdaySales(storeOrderService.getPayOrderAmountByDate(0, yesterday));
        response.setPageviews(userVisitRecordService.getPageviewsByDate(today));
        response.setYesterdayPageviews(userVisitRecordService.getPageviewsByDate(yesterday));
        response.setOrderNum(storeOrderService.getOrderNumByDate(0, today));
        response.setYesterdayOrderNum(storeOrderService.getOrderNumByDate(0, yesterday));
        response.setUserNum(userService.getTotalNum());
        response.setMerchantNum(merchantService.getAllCount());
        return response;
    }

    /**
     * 平台端首页经营数据
     *
     * @return HomeOperatingDataResponse
     */
    @Override
    public HomeOperatingDataResponse operatingPlatformData() {
        HomeOperatingDataResponse response = new HomeOperatingDataResponse();
        response.setNotShippingOrderNum(storeOrderService.getNotShippingNum(0));
        response.setRefundingOrderNum(storeRefundOrderService.getAwaitAuditNum(0));
        response.setOnSaleProductNum(storeProductService.getOnSaleNum(0));
        response.setAwaitAuditProductNum(storeProductService.getAwaitAuditNum());
        return response;
    }

    /**
     * 平台端首页获取用户渠道数据
     *
     * @return
     */
    @Override
    public List<UserChannelDataResponse> getUserChannelData() {
        List<User> userList = userService.getChannelData();
        return userList.stream().map(e -> {
            UserChannelDataResponse response = new UserChannelDataResponse();
            response.setUserType(e.getUserType());
            response.setNum(e.getPayCount());
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 组装订单统计返回数据
     *
     * @return Map<String, Object>
     * @author Mr.Zhang
     * @since 2020-05-16
     */
    private Map<String, Object> returnOrderDate(String dateLimit, String preDateLimit, int leftTime) {
        Map<String, Object> map = new HashMap<>();

        //查询本周周订单量
        List<StoreOrder> list = storeOrderService.getOrderGroupByDate(dateLimit, leftTime);

        map.put("quality",
                dataFormat(getOrderCountGroupByDate(list), dateLimit)
        );
        map.put("price",
                dataFormat(getOrderPriceGroupByDate(list), dateLimit)
        );

        //查询上周周订单量
        List<StoreOrder> preList = storeOrderService.getOrderGroupByDate(preDateLimit, leftTime);

        map.put("preQuality",
                dataFormat(getOrderCountGroupByDate(preList), preDateLimit)
        );
        map.put("prePrice",
                dataFormat(getOrderPriceGroupByDate(preList), preDateLimit)
        );

        return map;
    }

}
