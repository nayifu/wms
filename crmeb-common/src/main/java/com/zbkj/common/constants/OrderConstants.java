package com.zbkj.common.constants;

/**
 *  订单相关常量类
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
public class OrderConstants {

    /** 订单号前缀-平台 */
    public static final String ORDER_PREFIX_PLATFORM = "PT";

    /** 订单号前缀-商户 */
    public static final String ORDER_PREFIX_MERCHANT = "SH";

    /** 订单号前缀-退款 */
    public static final String ORDER_PREFIX_REFUND = "RE";

    /** 订单支付渠道-PayPal */
    public static final Integer ORDER_CHANNEL_PAYPAL = 8;

    /** 订单活动类型-普通 */
    public static final String ORDER_ACTIVITY_TYPE_NORMAL_STR = "普通";

    /** 订单状态-待发货/已支付 */
    public static final Integer ORDER_STATUS_SHIPPING = 0;
    /** 订单状态-待收货 */
    public static final Integer ORDER_STATUS_AWAIT_RECEIVING = 1;
    /** 订单状态-待评价 */
    public static final Integer ORDER_STATUS_AWAIT_REPLY = 2;
    /** 订单状态-已完成 */
    public static final Integer ORDER_STATUS_OVER = 3;
    /** 订单状态-已取消 */
    public static final Integer ORDER_STATUS_CANCEL = 9;

    /** 订单退款状态-申请退款 */
    public static final Integer ORDER_REFUND_STATUS_APPLY = 1;
    /** 订单退款状态-退款中 */
    public static final Integer ORDER_REFUND_STATUS_REFUNDING = 2;
    /** 订单退款状态-已退款 */
    public static final Integer ORDER_REFUND_STATUS_REFUND = 3;
    /** 订单退款状态-未退款 */
    public static final Integer ORDER_REFUND_STATUS_NO_REFUND = 0;

    /** 订单查询状态-所有 */
    public static final String ORDER_STATUS_ALL = "all";
    /** 订单查询状态-未支付 */
    public static final String ORDER_STATUS_UNPAID = "unPaid";
    /** 订单查询状态-未发货 */
    public static final String ORDER_STATUS_NOT_SHIPPED = "notShipped";
    /** 订单查询状态-待收货 */
    public static final String ORDER_STATUS_SPIKE = "spike";
    /** 订单查询状态-已收货待评价 */
    public static final String ORDER_STATUS_BARGAIN = "bargain";
    /** 订单查询状态-交易完成 */
    public static final String ORDER_STATUS_COMPLETE = "complete";
    /** 订单查询状态-申请退款 */
    public static final String ORDER_STATUS_APPLY_REFUNDING = "applyRefund";
    /** 订单查询状态-退款中 */
    public static final String ORDER_STATUS_REFUNDING = "refunding";
    /** 订单查询状态-已退款 */
    public static final String ORDER_STATUS_REFUNDED = "refunded";
    /** 订单查询状态-已删除 */
    public static final String ORDER_STATUS_DELETED = "deleted";

    /** 订单状态字符-待支付 */
    public static final String ORDER_STATUS_NO_PAID_STR = "待支付";
    /** 订单状态字符-申请退款 */
    public static final String ORDER_STATUS_APPLY_REFUND_STR = "申请退款中";
    /** 订单状态字符-已退款 */
    public static final String ORDER_STATUS_REFUND_STR = "已退款";
    /** 订单状态字符-退款中 */
    public static final String ORDER_STATUS_REFUNDING_STR = "退款中";
    /** 订单状态字符-待发货 */
    public static final String ORDER_STATUS_SHIPPING_STR = "待发货";
    /** 订单状态字符-待收货 */
    public static final String ORDER_STATUS_AWAIT_RECEIVING_STR = "待收货";
    /** 订单状态字符-待评价 */
    public static final String ORDER_STATUS_AWAIT_REPLY_STR = "待评价";
    /** 订单状态字符-已完成 */
    public static final String ORDER_STATUS_OVER_STR = "已完成";
    /** 订单状态字符-已删除 */
    public static final String ORDER_STATUS_REMOVE_STR = "已删除";

    /** H5端订单状态-未支付 */
    public static final int ORDER_STATUS_H5_UNPAID = 0;
    /** H5端订单状态-待发货 */
    public static final int ORDER_STATUS_H5_NOT_SHIPPED = 1;
    /** H5端订单状态-待收货 */
    public static final int ORDER_STATUS_H5_SPIKE = 2;
    /** H5端订单状态-已完成 */
    public static final int ORDER_STATUS_H5_COMPLETE = 3;
    /** H5端订单状态-退款中 */
    public static final int ORDER_STATUS_H5_REFUNDING = -1;
    /** H5端订单状态-已退款 */
    public static final int ORDER_STATUS_H5_REFUNDED = -2;
    /** H5端订单状态-退款 */
    public static final int ORDER_STATUS_H5_REFUND = -3;


    /** 商户端订单查询状态-所有 */
    public static final String MERCHANT_ORDER_STATUS_ALL = "all";
    /** 商户端订单查询状态-未支付 */
    public static final String MERCHANT_ORDER_STATUS_UNPAID = "unPaid";
    /** 商户端订单查询状态-未发货 */
    public static final String MERCHANT_ORDER_STATUS_NOT_SHIPPED = "notShipped";
    /** 商户端订单查询状态-待收货 */
    public static final String MERCHANT_ORDER_STATUS_SPIKE = "spike";
    /** 商户端订单查询状态-交易完成 */
    public static final String MERCHANT_ORDER_STATUS_COMPLETE = "complete";
    /** 商户端订单查询状态-申请退款 */
//    public static final String MERCHANT_ORDER_STATUS_APPLY_REFUNDING = "applyRefund";
    /** 商户端订单查询状态-退款中 */
//    public static final String MERCHANT_ORDER_STATUS_REFUNDING = "refunding";
    /** 商户端订单查询状态-已退款 */
    public static final String MERCHANT_ORDER_STATUS_REFUNDED = "refunded";
    /** 商户端订单查询状态-已删除 */
    public static final String MERCHANT_ORDER_STATUS_DELETED = "deleted";



    // ========== order_status_log部分常量开始 ==================

    /** 订单操作日志类型-退款 */
    public static final String ORDER_LOG_REFUND_PRICE = "refund";
    /** 订单操作日志类型-快递 */
    public static final String ORDER_LOG_EXPRESS = "express";
    /** 订单操作日志类型-不退款 */
    public static final String ORDER_LOG_REFUND_REFUSE = "refund_refuse";
    /** 订单操作日志类型-申请退款 */
    public static final String ORDER_LOG_REFUND_APPLY = "apply_refund";
    /** 订单操作日志类型-支付成功 */
    public static final String ORDER_LOG_PAY_SUCCESS = "pay";
    /** 订单操作日志类型-编辑订单 */
    public static final String ORDER_LOG_EDIT = "order_edit";
    /** 订单操作日志类型-删除订单 */
    public static final String ORDER_LOG_REMOVE = "delete";
    /** 订单操作日志类型-生成订单 */
    public static final String ORDER_LOG_CREATE = "create";
    /** 订单操作日志类型-取消订单 */
    public static final String ORDER_LOG_CANCEL = "cancel";
    /** 订单操作日志类型-自动取消 */
    public static final String ORDER_LOG_AUTO_CANCEL = "auto_cancel_order";
    /** 订单操作日志类型-订单完成 */
    public static final String ORDER_LOG_OVER = "complete";
    /** 订单操作日志类型-订单收货 */
    public static final String ORDER_LOG_RECEIVING = "receipt";


    /** 订单操作日志类型说明-退款 */
    public static final String ORDER_LOG_MESSAGE_REFUND_PRICE = "退款给用户{amount}元";
    /** 订单操作日志类型说明-快递 */
    public static final String ORDER_LOG_MESSAGE_EXPRESS = "已发货 快递公司：{deliveryName}, 快递单号：{deliveryCode}";
    /** 订单操作日志类型说明-不退款款因 */
    public static final String ORDER_LOG_MESSAGE_REFUND_REFUSE = "不退款款因：{reason}";
    /** 订单操作日志类型说明-付款成功 */
    public static final String ORDER_LOG_MESSAGE_PAY_SUCCESS = "用户付款成功";
    /** 订单操作日志类型说明-删除订单 */
    public static final String ORDER_LOG_MESSAGE_REMOVE = "删除订单";
    /** 订单操作日志类型说明-申请退款原因 */
    public static final String ORDER_LOG_MESSAGE_REFUND_APPLY = "用户申请退款原因：{}";
    /** 订单操作日志类型说明-生成订单 */
    public static final String ORDER_LOG_MESSAGE_CREATE = "订单生成";
    /** 订单操作日志类型说明-取消订单 */
    public static final String ORDER_LOG_MESSAGE_CANCEL = "取消订单";
    /** 订单操作日志类型说明-自动取消 */
    public static final String ORDER_LOG_MESSAGE_AUTO_CANCEL = "到期未支付系统自动取消";
      /** 订单操作日志类型说明-订单完成 */
    public static final String ORDER_LOG_MESSAGE_OVER = "用户评价,订单已完成";
    /** 订单操作日志类型说明-编辑订单 */
    public static final String ORDER_LOG_MESSAGE_EDIT = "订单价格 ${orderPrice} 修改实际支付金额为 ${price} 元";
    /** 订单操作日志类型说明-订单收货 */
    public static final String ORDER_LOG_MESSAGE_RECEIVING = "用户已收货";

    // ========== order_status_log部分常量结束 ==================

    // ========== eb_store_order_profit_sharing 部分常量开始 ==================

    /** 订单分账状态-未分账 */
    public static final Integer ORDER_PROFIT_SHARING_STATUS_AWAIT = 0;
    /** 订单分账状态-已分账 */
    public static final Integer ORDER_PROFIT_SHARING_STATUS_SHARING = 1;
    /** 订单分账状态-已退款 */
    public static final Integer ORDER_PROFIT_SHARING_STATUS_REFUND = 2;


    // ========== eb_store_order_profit_sharing 部分常量结束 ==================

    // ========== eb_store_refund_order 部分常量开始 ==================

    /** 商户退款订单状态-待审核 */
    public static final int MERCHANT_REFUND_ORDER_STATUS_APPLY = 0;
    /** 商户退款订单状态-审核未通过 */
    public static final int MERCHANT_REFUND_ORDER_STATUS_REJECT = 1;
    /** 商户退款订单状态-退款中 */
    public static final int MERCHANT_REFUND_ORDER_STATUS_REFUNDING = 2;
    /** 商户退款订单状态-已退款 */
    public static final int MERCHANT_REFUND_ORDER_STATUS_REFUND = 3;



    // ========== eb_store_refund_order 部分常量结束 ==================


    // ========== eb_store_order_refund_status部分常量开始 ==================

    /** 订单操作日志类型-退款 */
    public static final String REFUND_ORDER_LOG_TYPE_REFUND = "refund";
    /** 订单操作日志类型-申请退款 */
    public static final String REFUND_ORDER_LOG_TYPE_APPLY = "apply";
    /** 订单操作日志类型-决绝退款 */
    public static final String REFUND_ORDER_LOG_TYPE_FAIL = "fail";

    // ========== eb_store_order_refund_status部分常量结束 ==================
}
