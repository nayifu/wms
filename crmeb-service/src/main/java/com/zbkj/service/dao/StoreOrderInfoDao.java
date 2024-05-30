package com.zbkj.service.dao;

import com.zbkj.common.model.order.StoreOrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单购物详情表 Mapper 接口
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
public interface StoreOrderInfoDao extends BaseMapper<StoreOrderInfo> {

    /**
     * 根据时间、商品id获取销售件数
     * @param date 时间，格式'yyyy-MM-dd'
     * @param proId 商品id
     */
    Integer getSalesNumByDateAndProductId(@Param("date") String date, @Param("proId")  Integer proId);

    /**
     * 根据时间、商品id获取销售额
     * @param date 时间，格式'yyyy-MM-dd'
     * @param proId 商品id
     */
    BigDecimal getSalesByDateAndProductId(@Param("date") String date, @Param("proId")  Integer proId);

    /**
     * 获取评论列表
     * @param userId 用户id
     */
    List<StoreOrderInfo> findReplyList(@Param("userId") Integer userId);

    /**
     * 获取待评论数量
     * @param userId 用户uid
     */
    Integer getAwaitNumByUid(@Param("userId") Integer userId);
}
