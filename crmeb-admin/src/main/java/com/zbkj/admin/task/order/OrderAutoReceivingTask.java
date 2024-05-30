package com.zbkj.admin.task.order;

import com.zbkj.common.utils.DateUtil;
import com.zbkj.service.service.OrderTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单自动收货Task
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
@Component("OrderAutoReceivingTask")
public class OrderAutoReceivingTask {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(OrderAutoReceivingTask.class);

    @Autowired
    private OrderTaskService orderTaskService;

    /**
     * 每天处理一次
     */
    public void autoReceiving() {
        // cron : 0 0 0 */1 * ?
        logger.info("---OrderAutoReceivingTask task------produce Data with fixed rate task: Execution Time - {}", DateUtil.nowDateTime());
        try {
            orderTaskService.autoReceiving();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("OrderAutoReceivingTask.task" + " | msg : " + e.getMessage());
        }

    }

}
