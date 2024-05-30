package com.zbkj.common.constants;

/**
 *  系统设置常量类
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
public class SysConfigConstants {

    /** 网站名称 */
    public static final String CONFIG_KEY_SITE_NAME = "site_name";

    /** 商品警戒库存 */
    public static final String CONFIG_KEY_STORE_STOCK = "store_stock";

    /** 下单支付金额按比例赠送积分（实际支付1元赠送多少积分 */
    public static final String CONFIG_KEY_INTEGRAL_RATE_ORDER_GIVE = "order_give_integral";

    /** CRMEB chat 统计 */
    public static final String JS_CONFIG_CRMEB_CHAT_TONGJI = "crmeb_tongji_js";

    /** 用户默认头像 */
    public static final String USER_DEFAULT_AVATAR_CONFIG_KEY = "h5_avatar";
    /** 系统颜色配置 */
    public static final String CONFIG_CHANGE_COLOR_CONFIG = "change_color_config";

    /** 验证码过期时间 */
    public static final String CONFIG_KEY_SMS_CODE_EXPIRE = "sms_code_expire";

    /** 阿里云短信签名 */
    public static final String CONFIG_KEY_ALIYUN_SMS_SIGN_NAME = "aliyun_sms_sign_name";
    /** 阿里云短信access key id */
    public static final String CONFIG_KEY_ALIYUN_SMS_KEY_ID = "aliyun_sms_key_id";
    /** 阿里云短信access key secret */
    public static final String CONFIG_KEY_ALIYUN_SMS_KEY_SECRET = "aliyun_sms_key_secret";

    /** 积分冻结时间 */
    public static final String CONFIG_KEY_STORE_INTEGRAL_EXTRACT_TIME = "freeze_integral_day";

    /** 企业logo地址 */
    public static final String CONFIG_KEY_SITE_LOGO = "mobile_top_logo";
    /** 商品分类页配置 */
    public static final String CONFIG_CATEGORY_CONFIG = "category_page_config";

    /** 底部导航—是否自定义 */
    public static final String CONFIG_BOTTOM_NAVIGATION_IS_CUSTOM = "bottom_navigation_is_custom";

    /** 是否隐藏一级分类 */
    public static final String CONFIG_IS_SHOW_CATEGORY = "is_show_category";
    /** 首页商品列表模板配置 */
    public static final String CONFIG_IS_PRODUCT_LIST_STYLE = "homePageSaleListStyle";

    /** Twitter客户端Key */
    public static final String CONFIG_KEY_TWITTER_CONSUMER_KEY = "twitter_consumer_key";
    /** Twitter客户端Secret */
    public static final String CONFIG_KEY_TWITTER_CONSUMER_SECRET = "twitter_consumer_secret";
    /** Twitter H5商城登录后回调H5地址 */
    public static final String CONFIG_KEY_TWITTER_CALLBACK_URL_H5 = "twitter_h5_callback_url";
    /** Twitter PC商城登录后回调地址 */
    public static final String CONFIG_KEY_TWITTER_CALLBACK_URL_PC = "twitter_pc_callback_url";
    /** Twitter开关 */
    public static final String CONFIG_KEY_TWITTER_OPEN = "twitter_open";

    /** Google开关 */
    public static final String CONFIG_KEY_GOOGLE_OPEN = "google_open";
    /** Google客户端ID */
    public static final String CONFIG_KEY_GOOGLE_CLIENT_ID = "google_client_id";

    /** FaceBook开关 */
    public static final String CONFIG_KEY_FACEBOOK_OPEN = "facebook_open";
    /** FaceBook app id */
    public static final String CONFIG_KEY_FACEBOOK_APPID = "facebook_appid";

    /** 匿名下单开关 */
    public static final String CONFIG_KEY_VISITOR_OPEN = "visitor_open";

    //后台首页登录图片
    /** 登录页LOGO，左侧 */
    public static final String CONFIG_KEY_ADMIN_LOGIN_LOGO_LEFT_TOP = "site_logo_lefttop";
    /** 登录页LOGO */
    public static final String CONFIG_KEY_ADMIN_LOGIN_LOGO_LOGIN = "site_logo_login";
    /** 登录页背景图 */
    public static final String CONFIG_KEY_ADMIN_LOGIN_BACKGROUND_IMAGE = "admin_login_bg_pic";

    /** 退款理由 */
    public static final String CONFIG_KEY_STOR_REASON = "stor_reason";

    /** 积分抵用比例(1积分抵多少金额) */
    public static final String CONFIG_KEY_INTEGRAL_RATE = "integral_ratio";

    /** 图片上传类型 1本地 2七牛云 3OSS 4COS, 默认本地 */
    public static final String CONFIG_UPLOAD_TYPE = "uploadType";
    /** 文件上传是否保存本地 */
    public static final String CONFIG_FILE_IS_SAVE = "file_is_save";
    /** 全局本地图片域名 */
    public static final String CONFIG_LOCAL_UPLOAD_URL = "localUploadUrl";
    /** 图片上传,拓展名 */
    public static final String UPLOAD_IMAGE_EXT_STR_CONFIG_KEY = "image_ext_str";
    /** 图片上传,最大尺寸 */
    public static final String UPLOAD_IMAGE_MAX_SIZE_CONFIG_KEY = "image_max_size";
    /** 文件上传,拓展名 */
    public static final String UPLOAD_FILE_EXT_STR_CONFIG_KEY = "file_ext_str";
    /** 文件上传,最大尺寸 */
    public static final String UPLOAD_FILE_MAX_SIZE_CONFIG_KEY = "file_max_size";

    /** 七牛云上传URL */
    public static final String CONFIG_QN_UPLOAD_URL = "qnUploadUrl";
    /** 七牛云Access Key */
    public static final String CONFIG_QN_ACCESS_KEY = "qnAccessKey";
    /** 七牛云Secret Key */
    public static final String CONFIG_QN_SECRET_KEY = "qnSecretKey";
    /** 七牛云存储名称 */
    public static final String CONFIG_QN_STORAGE_NAME = "qnStorageName";
    /** 七牛云存储区域 */
    public static final String CONFIG_QN_STORAGE_REGION = "qnStorageRegion";

    /** 阿里云上传URL */
    public static final String CONFIG_AL_UPLOAD_URL = "alUploadUrl";
    /** 阿里云Access Key */
    public static final String CONFIG_AL_ACCESS_KEY = "alAccessKey";
    /** 阿里云Secret Key */
    public static final String CONFIG_AL_SECRET_KEY = "alSecretKey";
    /** 阿里云存储名称 */
    public static final String CONFIG_AL_STORAGE_NAME = "alStorageName";
    /** 阿里云存储区域 */
    public static final String CONFIG_AL_STORAGE_REGION = "alStorageRegion";

    /** 腾讯云上传URL */
    public static final String CONFIG_TX_UPLOAD_URL = "txUploadUrl";
    /** 腾讯云Access Key */
    public static final String CONFIG_TX_ACCESS_KEY = "txAccessKey";
    /** 腾讯云Secret Key */
    public static final String CONFIG_TX_SECRET_KEY = "txSecretKey";
    /** 腾讯云存储名称 */
    public static final String CONFIG_TX_STORAGE_NAME = "txStorageName";
    /** 腾讯云存储区域 */
    public static final String CONFIG_TX_STORAGE_REGION = "txStorageRegion";


    /** 客服H5链接 */
    public static final String CONFIG_CONSUMER_H5_URL = "consumer_h5_url";
    /** 客服电话 */
    public static final String CONFIG_CONSUMER_HOTLINE = "consumer_hotline";
    /** 客服电话 */
    public static final String CONFIG_CONSUMER_MESSAGE = "consumer_message";
    /** 客服邮箱 */
    public static final String CONFIG_CONSUMER_EMAIL = "consumer_email";
    /** 客服类型 */
    public static final String CONFIG_CONSUMER_TYPE = "consumer_type";


    /** 商品导入平台地址-淘宝 */
    public static final String CONFIG_IMPORT_PRODUCT_TB = "importProductTB";
    /** 商品导入平台地址-京东 */
    public static final String CONFIG_IMPORT_PRODUCT_JD = "importProductJD";
    /** 商品导入平台地址-苏宁 */
    public static final String CONFIG_IMPORT_PRODUCT_SN = "importProductSN";
    /** 商品导入平台地址-拼多多 */
    public static final String CONFIG_IMPORT_PRODUCT_PDD = "importProductPDD";
    /** 商品导入平台地址-天猫 */
    public static final String CONFIG_IMPORT_PRODUCT_TM = "importProductTM";
    /** 商品导入99Api Key */
    public static final String CONFIG_COPY_PRODUCT_APIKEY = "copy_product_apikey";

    /** 阿里云全国物流App key */
    public static final String CONFIG_ALIYUN_LOGISTICS_APP_KEY = "logistics_app_key";
    /** 阿里云全国物流App secret */
    public static final String CONFIG_ALIYUN_LOGISTICS_APP_SECRET = "logistics_app_secret";

    /** 移动端文章顶部的banner图最大数量 配置数据最小3最大10 */
    public static final String ARTICLE_BANNER_LIMIT = "news_slides_limit";

    /** 订单取消时间 */
    public static final String CONFIG_ORDER_CANCEL_TIME = "order_cancel_time";

    /** PayPal客户端id */
    public static final String CONFIG_PAYPAL_CLIENT_ID = "paypal_client_id";
    /** PayPal客户端secret */
    public static final String CONFIG_PAYPAL_CLIENT_SECRET = "paypal_client_secret";
    /** PayPal支付模式:sandbox-沙盒,live-正式 */
    public static final String CONFIG_PAYPAL_MODE = "paypal_mode";
    /** PayPal支付开关:false,true */
    public static final String CONFIG_PAYPAL_SWITCH = "paypal_switch";

    /** Stripe ApiKey */
    public static final String CONFIG_STRIPE_API_KEY = "stripe_api_key";
    /** Stripe支付开关:false,true */
    public static final String CONFIG_STRIPE_SWITCH = "stripe_switch";

   /** Wechat支付开关:false,true */
    public static final String CONFIG_WECHAT_PAY_SWITCH = "wechat_pay_switch";

    /** 商户保证金额 */
    public static final String MERCHANT_GUARANTEED_AMOUNT = "guaranteed_amount";
    /** 商户每笔最小转账额度 */
    public static final String MERCHANT_TRANSFER_MIN_AMOUNT = "transfer_min_amount";
    /** 商户每笔最高转账额度 */
    public static final String MERCHANT_TRANSFER_MAX_AMOUNT = "transfer_max_amount";
    /** 商户余额冻结期 */
    public static final String MERCHANT_BALANCE_FREEZE_DAY = "balance_freeze_day";

    /** 商户入驻协议 */
    public static final String MERCHANT_SETTLEMENT_AGREEMENT = "merSettlementAgreement";
    /** 用户注册协议 */
    public static final String USER_REGISTER_AGREEMENT = "merLoginAgreement";

    /** 首页banner类型 */
    public static final String FORNT_INDEX_BANNER_TYPE = "index_banner_type";

//    /** PC首页店铺街背景图 */
//    public static final String PC_HOME_SHOP_STREET_BACK_IMAGE = "pc_home_shop_street_back";
    /** PC首页推荐商品竖图 */
    public static final String PC_HOME_RECOMMEND_IMAGE = "pc_home_recommend_image";
    /** PC店铺街头图 */
    public static final String PC_SHOP_STREET_HEADER_IMAGE = "pc_shop_street_header_image";
    /** PC顶部logo */
    public static final String PC_TOP_LOGO = "pc_top_logo";
    /** PC登录页左侧展示图 */
    public static final String PC_LOGIN_LEFT_IMAGE = "pc_login_left_image";


    /** 系统配置列表 */
    public static final String CONFIG_LIST = "config_list";
}
