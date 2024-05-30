package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.response.PlatformProductListResponse;

import java.util.List;
import java.util.Map;

/**
 * 商品表 Mapper 接口
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
public interface StoreProductDao extends BaseMapper<StoreProduct> {

    /**
     * 平台端商品分页列表
     * @param map 查询参数
     */
    List<PlatformProductListResponse> getPlatformPageList(Map<String, Object> map);

}
