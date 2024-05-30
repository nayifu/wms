package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.vo.MerchantConfigInfoVo;
import com.zbkj.common.vo.MerchantTransferInfoVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * MerchantService 接口
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
public interface MerchantService extends IService<Merchant> {

    /**
     * 商户分页列表
     * @param searchRequest 搜索参数
     * @param pageParamRequest 分页参数
     * @return
     */
    PageInfo<MerchantPageResponse> getAdminPage(MerchantSearchRequest searchRequest, PageParamRequest pageParamRequest);

    /**
     * 添加商户
     * @param request 请求参数
     * @return Boolean
     */
    Boolean add(MerchantAddRequest request);

    /**
     * 编辑商户
     * @param request 请求参数
     * @return Boolean
     */
    Boolean edit(MerchantUpdateRequest request);

    /**
     * 修改商户密码
     * @param request 请求对象
     */
    Boolean updatePassword(MerchantUpdatePasswordRequest request);

    /**
     * 修改复制商品数量
     * @param request 请求对象
     * @return Boolean
     */
    Boolean updateCopyProductNum(MerchantUpdateProductNumRequest request);

    /**
     * 平台端商户详情
     * @param id 商户ID
     * @return MerchantPlatformDetailResponse
     */
    MerchantPlatformDetailResponse getPlatformDetail(Integer id);

    /**
     * 商户推荐开关
     * @param id 商户ID
     * @return Boolean
     */
    Boolean recommendSwitch(Integer id);

    /**
     * 关闭商户
     * @param id 商户ID
     * @return Boolean
     */
    Boolean close(Integer id);

    /**
     * 开启商户
     * @param id 商户ID
     * @return Boolean
     */
    Boolean open(Integer id);

    /**
     * 入驻审核成功，初始化商户
     * @param merchantAddRequest 商户添加参数
     * @param auditorId 审核员id
     */
    Boolean auditSuccess(MerchantAddRequest merchantAddRequest, Integer auditorId);

    /**
     * 获取商户详情
     * @param id 商户ID
     * @return Merchant
     */
    Merchant getByIdException(Integer id);

    /**
     * 扣减商户复制商品数量
     * @param id 商户id
     * @return Boolean
     */
    Boolean subCopyProductNum(Integer id);

    /**
     * 操作商户余额
     * @param merId 商户id
     * @param price 金额
     * @param type 操作类型
     * @return Boolean
     */
    Boolean operationBalance(Integer merId, BigDecimal price, String type);

    /**
     * 获取商户列表（商户id）
     * @param merIdList 商户ID列表
     * @return List
     */
    List<Merchant> getListByIdList(List<Integer> merIdList);

    /**
     * 获取商户Id组成的Map
     * @param merIdList 商户ID列表
     * @return Map
     */
    Map<Integer, Merchant> getMerIdMapByIdList(List<Integer> merIdList);

    /**
     * 商户端商户基础信息
     * @return MerchantBaseInfoResponse
     */
    MerchantBaseInfoResponse getBaseInfo();

    /**
     * 商户端商户配置信息
     * @return MerchantConfigInfoResponse
     */
    MerchantConfigInfoVo getConfigInfo();

    /**
     * 商户端商户转账信息
     * @return MerchantTransferInfoResponse
     */
    MerchantTransferInfoVo getTransferInfo();

    /**
     * 商户端商户配置信息编辑
     * @param request 编辑参数
     * @return Boolean
     */
    Boolean configInfoEdit(MerchantConfigInfoVo request);

    /**
     * 商户端商户转账信息编辑
     * @param request 编辑参数
     * @return Boolean
     */
    Boolean transferInfoEdit(MerchantTransferInfoVo request);

    /**
     * 商户端商户开关
     * @return Boolean
     */
    Boolean updateSwitch();

    /**
     * 获取所有的商户id
     * @return List
     */
    List<Integer> getAllId();

    /**
     * 商户入驻申请
     * @param request 申请参数
     * @return Boolean
     */
    Boolean settledApply(MerchantSettledApplyRequest request);

    /**
     * 商户入驻申请记录
     * @param pageParamRequest 分页参数
     * @return List
     */
    PageInfo<MerchantSettledResponse> findSettledRecord(PageParamRequest pageParamRequest);

    /**
     * 商户搜索列表
     * @param request 搜索参数
     * @param pageParamRequest 分页参数
     * @return List
     */
    PageInfo<MerchantSearchResponse> findSearchList(MerchantMoveSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 店铺街
     * @param pageParamRequest 分页参数
     * @return List
     */
    PageInfo<MerchantSearchResponse> getStreet(PageParamRequest pageParamRequest);

    /**
     * 店铺首页信息
     * @param id 商户id
     * @return MerchantIndexInfoResponse
     */
    MerchantIndexInfoResponse getIndexInfo(Integer id);

    /**
     * 店铺详细信息
     * @param id 商户id
     * @return MerchantDetailResponse
     */
    MerchantDetailResponse getDetail(Integer id);

    /**
     * 商户是否使用商户分类
     * @param cid 分类id
     * @return Boolean
     */
    Boolean isExistCategory(Integer cid);

    /**
     * 商户是否使用商户类型
     * @param tid 类型id
     * @return Boolean
     */
    Boolean isExistType(Integer tid);

    /**
     * 商户分页列表表头数量
     * @return MerchantHeaderNumResponse
     */
    MerchantHeaderNumResponse getListHeaderNum();

    /**
     * 获取所有商户数量
     * @return Integer
     */
    Integer getAllCount();

    /**
     * 获取入驻协议
     * @return MerchantAgreementResponse
     */
    MerchantAgreementResponse getSettledAgreement();

    /**
     * 获取商户客服信息
     */
    MerchantServiceInfoResponse getCustomerServiceInfo(Integer id);
}