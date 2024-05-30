package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.activity.ActivityProduct;
import com.zbkj.common.vo.ActivityProductInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 活动商品关联表 Mapper 接口
 * </p>
 *
 * @author HZW
 * @since 2022-03-29
 */
public interface ActivityProductDao extends BaseMapper<ActivityProduct> {

    /**
     * 通过活动ID获取H5商品列表
     * @param aid 活动ID
     */
    List<ActivityProductInfoVo> getH5ListByAid(@Param("aid") Integer aid);
}
