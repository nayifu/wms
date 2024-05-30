package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.coupon.StoreCoupon;
import com.zbkj.common.request.CouponSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreCouponRequest;
import com.zbkj.common.request.StoreCouponSearchRequest;
import com.zbkj.common.response.StoreCouponFrontResponse;
import com.zbkj.common.response.StoreCouponInfoResponse;
import com.zbkj.common.vo.CouponSimpleVo;

import java.util.List;

/**
 * StoreCouponService 接口
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
public interface StoreCouponService extends IService<StoreCoupon> {

    /**
     * 创建优惠券
     */
    boolean create(StoreCouponRequest request);

    /**
     * 优惠券详情带异常
     */
    StoreCoupon getInfoException(Integer id);

    /**
     * 优惠券详情
     */
    StoreCouponInfoResponse info(Integer id);

    /**
     * 扣减数量
     * @param id 优惠券id
     * @param num 数量
     * @param isLimited 是否限量
     */
    Boolean deduction(Integer id, Integer num, Boolean isLimited);

    /**
     * 获取用户注册赠送新人券
     * @return 优惠券列表
     */
    List<StoreCoupon> findRegisterList();

    /**
     * 删除优惠券
     * @param id 优惠券id
     * @return Boolean
     */
    Boolean delete(Integer id);

    /**
     * 移动端优惠券列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return List<StoreCouponFrontResponse>
     */
    PageInfo<StoreCouponFrontResponse> getH5List(CouponSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 修改优惠券状态
     * @param id 优惠券id
     */
    Boolean updateStatus(Integer id);

    /**
     * 商户端优惠券分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<StoreCoupon> getMerchantPageList(StoreCouponSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 商品可用优惠券列表（商品创建时选择使用）
     * @return List
     */
    List<StoreCoupon> getProductUsableList();

    /**
     * 获取优惠券简单对象列表
     * @param idList id列表
     * @return List
     */
    List<CouponSimpleVo> findSimpleListByIdList(List<Integer> idList);
}
