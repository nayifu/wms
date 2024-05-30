package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.order.StoreOrderRefundStatus;
import com.zbkj.common.model.order.StoreOrderStatus;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.dao.StoreOrderRefundStatusDao;
import com.zbkj.service.service.StoreOrderRefundStatusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
*  StoreOrderRefundStatusServiceImpl 接口实现
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
public class StoreOrderRefundStatusServiceImpl extends ServiceImpl<StoreOrderRefundStatusDao, StoreOrderRefundStatus> implements StoreOrderRefundStatusService {

    @Resource
    private StoreOrderRefundStatusDao dao;

    /**
     * 添加订单日志
     * @param refundOrderNo 退款订单号
     * @param type 类型
     * @param message 备注
     * @return Boolean
     */
    @Override
    public Boolean createLog(String refundOrderNo, String type, String message) {
        StoreOrderRefundStatus orderRefundStatus = new StoreOrderRefundStatus();
        orderRefundStatus.setRefundOrderNo(refundOrderNo);
        orderRefundStatus.setChangeType(type);
        orderRefundStatus.setChangeMessage(message);
        orderRefundStatus.setCreateTime(DateUtil.nowDateTime());
        return save(orderRefundStatus);
    }
}

