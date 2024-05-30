package com.zbkj.common.constants;

/**
 *  配置类
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
public class Constants {

    /** 开关-开 */
    public static final String COMMON_SWITCH_OPEN_TYPE_ONE = "'1'";
    /** 开关-关 */
    public static final String COMMON_SWITCH_CLOSE_TYPE_ONE = "'0'";

    public static final int NUM_ZERO = 0;
    public static final int NUM_ONE = 1;
    public static final int NUM_TWO = 2;
    public static final int NUM_THREE = 3;
    public static final int NUM_FIVE = 5;
    public static final int NUM_SEVEN = 7;
    public static final int NUM_TEN = 10;
    public static final int NUM_ONE_HUNDRED = 100;

    /** 头部 token令牌key */
    public static final String HEADER_AUTHORIZATION_KEY = "Authori-zation";

    //用户等级升级
    public static final String USER_LEVEL_OPERATE_LOG_MARK = "尊敬的用户 【{$userName}】, 在{$date}赠送会员等级成为{$levelName}会员";
    public static final String USER_LEVEL_UP_LOG_MARK = "尊敬的用户 【{$userName}】, 在{$date}您升级为为{$levelName}会员";

    //默认分页
    public static final int DEFAULT_PAGE = 1;

    //默认分页
    public static final int DEFAULT_LIMIT = 20;

    //升序排序
    public static final String SORT_ASC = "asc";

    //降序排序
    public static final String SORT_DESC = "desc";

    //导出最大数值
    public static final Integer EXPORT_MAX_LIMIT = 99999;

    //上传类型
    public static final String UPLOAD_TYPE_IMAGE = "crmebimage";

    //上传类型
    public static final String DOWNLOAD_TYPE_FILE = "downloadf"; // 文件导出下载拦截关键字
    public static final String UPLOAD_TYPE_FILE = "uploadf"; // 文件前端上传后下载关键字

    public static final String UPLOAD_MODEL_PATH_EXCEL = "excel";// excel

    //config表数据redis
    public static final String CONFIG_LIST = "config_list"; //配置列表

    //支付方式
    public static final String PAY_TYPE_PAYPAL = "paypal"; //paypal

    //支付方式
    public static final String PAY_TYPE_STR_PAYPAL = "PayPal支付"; //PayPal支付

    // 订单缓存
    public static final long ORDER_CASH_CONFIRM = 5L;

    // 时间类型开始时间
    public static String DATE_TIME_TYPE_BEGIN = "begin";

    // 时间类型结束时间
    public static String DATE_TIME_TYPE_END = "end";

    /** 邮箱验证码redis key */
    public static final String EMAIL_VERIFICATION_CODE = "email:verification:code:";

    /** 获取Ip地址信息url */
    public static final String IP_INFO_URI = "https://ipapi.co/{}/json";

    /** 客户类型-H5 */
    public static final String CONSUMER_TYPE_H5 = "h5";
    /** 客户类型-热线 */
    public static final String CONSUMER_TYPE_HOTLINE = "hotline";
    /** 客户类型-信息 */
    public static final String CONSUMER_TYPE_MESSAGE = "message";
    /** 客户类型-邮箱 */
    public static final String CONSUMER_TYPE_EMAIL = "email";

    /** 系统通知详情类型-国内短信 */
    public static final String NOTIFICATION_DETAIL_TYPE_SMS = "sms";
    /** 系统通知详情类型-国外短信 */
    public static final String NOTIFICATION_DETAIL_TYPE_OVERSEA_SMS = "overseaSms";
    /** 系统通知详情类型-邮箱 */
    public static final String NOTIFICATION_DETAIL_TYPE_EMAIL = "email";

    /** 操作类型-添加 */
    public static final String OPERATION_TYPE_ADD = "add";
    /** 操作类型-扣减 */
    public static final String OPERATION_TYPE_SUBTRACT = "sub";
    /** 商城当前使用的币种 */
    public static final String SHOP_PAY_CURRENCY = "shop_pay_currency";
}
