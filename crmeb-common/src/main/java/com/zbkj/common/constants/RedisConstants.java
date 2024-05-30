package com.zbkj.common.constants;

/**
 * Redis常量类
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
public class RedisConstants {

    /** 用户注册信息缓存Key */
    public static final String USER_REGISTER_KEY = "USER:REGISTER:";

    /** 用户登token redis存储前缀 */
    public static final String USER_TOKEN_REDIS_KEY_PREFIX = "TOKEN_USER:";

    /** 商品浏览量（每日） */
    public static final String PRO_PAGE_VIEW_KEY = "statistics:product:page_view:";
    public static final String PRO_PRO_PAGE_VIEW_KEY = "statistics:product:pro_page_view:{}:{}";

    /** 商品加购量（每日） */
    public static final String PRO_ADD_CART_KEY = "statistics:product:add_cart:";
    public static final String PRO_PRO_ADD_CART_KEY = "statistics:product:pro_add_cart:{}:{}";

    /** 移动端Token过期时间 3小时 */
    public static final long TOKEN_EXPRESS_MINUTES = (60 * 24);

    /** 验证码redis key前缀 */
    public static final String VALIDATE_REDIS_KEY_PREFIX = "validate_code_";

    /** 预下单Key */
    public static final String USER_READY_ORDER_KEY = "user_order:";

    /** 系统菜单缓存Key */
    public static final String MENU_CACHE_LIST_KEY = "menuList";
    /** 平台端系统菜单缓存Key */
    public static final String PLATFORM_MENU_CACHE_LIST_KEY = "platformMenuList";
    /** 商户端系统菜单缓存Key */
    public static final String MERCHANT_MENU_CACHE_LIST_KEY = "merchantMenuList";

    /** 商品分类缓存Key */
    public static final String PRODUCT_CATEGORY_CACHE_LIST_KEY = "productCategoryList";
    /** 商户端商品分类缓存Key */
    public static final String PRODUCT_CATEGORY_CACHE_MERCHANT_LIST_KEY = "productCategoryMerchantList";

    /** 商品全部品牌缓存列表Key */
    public static final String PRODUCT_ALL_BRAND_LIST_KEY = "productBrandAllList";

    /** 商户商品分类缓存Key */
    public static final String STORE_PRODUCT_CATEGORY_CACHE_LIST_KEY = "store:product:category:list:{}";

    /** 物流公司缓存Key */
    public static final String EXPRESS_CACHE_LIST_KEY = "expressList";
}
