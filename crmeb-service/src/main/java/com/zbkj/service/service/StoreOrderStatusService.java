package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.order.StoreOrderStatus;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreOrderStatusSearchRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * StoreOrderStatusService 接口
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
public interface StoreOrderStatusService extends IService<StoreOrderStatus> {

    /**
     * 订单操作记录列表
     * @param request 请求参数
     * @param pageParamRequest 分页参数
     * @return List
     */
    List<StoreOrderStatus> getList(StoreOrderStatusSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 保存订单退款记录
     * @param orderNo 订单编号
     * @param amount 金额
     * @param message 备注
     * @return Boolean
     */
    Boolean saveRefund(String orderNo, BigDecimal amount, String message);

    /**
     * 添加订单日志
     * @param orderNo 订单id
     * @param type 类型
     * @param message 备注
     * @return Boolean
     */
    Boolean createLog(String orderNo, String type, String message);
}
