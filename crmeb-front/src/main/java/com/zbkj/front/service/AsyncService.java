package com.zbkj.front.service;

/**
 * 异步调用服务
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
public interface AsyncService {

    /**
     * 商品详情统计
     * @param proId 商品id
     * @param uid 用户uid
     */
    void productDetailStatistics(Integer proId, Integer uid);

    /**
     * 保存用户访问记录
     * @param userId 用户id
     * @param visitType 访问类型
     */
    void saveUserVisit(Integer userId, Integer visitType);
}
