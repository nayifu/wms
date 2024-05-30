package com.zbkj.common.constants;

/**
 *  商品常量类
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
public class ProductConstants {

    /** 商品删除类型-回收站 */
    public static final String PRODUCT_DELETE_TYPE_RECYCLE = "recycle";
    /** 商品删除类型-删除 */
    public static final String PRODUCT_DELETE_TYPE_DELETE = "delete";

    /** 商品活动类型-普通商品 */
    public static final Integer PRODUCT_ACTIVITY_TYPE_NORMAL = 0;

    /** 商品评论类型-所有 */
    public static final String PRODUCT_REPLY_TYPE_ALL = "all";
    /** 商品评论类型-好评 */
    public static final String PRODUCT_REPLY_TYPE_GOOD = "good";
    /** 商品评论类型-中评 */
    public static final String PRODUCT_REPLY_TYPE_MEDIUM = "medium";
    /** 商品评论类型-差评 */
    public static final String PRODUCT_REPLY_TYPE_POOR = "poor";

    /** 商品关系类型-收藏 */
    public static final String PRODUCT_RELATION_TYPE_COLLECT = "collect";

    /** 商品审核状态-待审核 */
    public static final Integer AUDIT_STATUS_WAIT = 0;
    /** 商品审核状态-审核成功 */
    public static final Integer AUDIT_STATUS_SUCCESS = 1;
    /** 商品审核状态-审核拒绝 */
    public static final Integer AUDIT_STATUS_FAIL = 2;

}
