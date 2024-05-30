package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.user.User;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户表 服务类
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
public interface UserService extends IService<User> {

    /**
     * 平台端用户分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<UserResponse> getPlatformPage(UserSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 用户分组
     * @param request 分组参数
     */
    Boolean group(UserAssignGroupRequest request);

    User getInfo();

    User getInfoException();

    Integer getUserIdException();

    Integer getUserId();

    Map<Object, Object> getAddUserCountGroupDate(String date);

    /**
     * 个人中心-用户信息
     * @return UserCenterResponse
     */
    UserCenterResponse getUserCenter();

    /**
     * 根据用户id获取用户列表 map模式
     * @param uidList uidList
     * @return HashMap
     */
    HashMap<Integer, User> getMapListInUid(List<Integer> uidList);

    /**
     * 用户分配标签
     * @param request 标签参数
     */
    Boolean tag(UserAssignTagRequest request);

    /**
     * 会员详情页Top数据
     * @param userId 用户uid
     * @return TopDetail
     */
    TopDetail getTopDetail(Integer userId);

    String getValidateCodeRedisKey(String phone);

    Integer getCountByPayCount(int minPayCount, int maxPayCount);

    /**
     * 手机号注册用户
     * @param phone 手机号
     * @return User
     */
    User registerPhone(String phone, String countryCode, String password);

    /**
     * 更新用户
     * @param userRequest 用户参数
     */
    Boolean updateUser(UserUpdateRequest userRequest);

    /**
     * 获取用户总人数
     */
    Integer getTotalNum();

    /**
     * 根据日期获取注册用户数量
     * @param date 日期
     * @return Integer
     */
    Integer getRegisterNumByDate(String date);

    /**
     * 根据日期段获取注册用户数量
     * @param startDate 日期
     * @param endDate 日期
     * @return UserOverviewResponse
     */
    Integer getRegisterNumByPeriod(String startDate, String endDate);

    /**
     * 获取用户性别数据
     * @return List
     */
    List<User> getSexData();

    /**
     * 获取用户渠道数据
     * @return List
     */
    List<User> getChannelData();

    /**
     * 获取所有用户的id跟地址
     * @return List
     */
    List<User> findIdAndAddresList();

    /**
     * 修改个人资料
     * @param request 修改信息
     */
    Boolean editUser(UserEditRequest request);

    /**
     * 获取用户详情
     * @param id 用户uid
     */
    User getInfoByUid(Integer id);

    /**
     * 通过邮箱跟用户subject获取用户信息
     * @param email 邮箱
     * @param identity 用户标识
     * @param userType 用户类型
     * @return User
     */
    User getByEmailAndIdentityAndType(String email, String identity, String userType);

    /**
     * 公共注册
     * @param identity 用户标识
     * @param email 邮箱
     * @param nickName 昵称
     * @param avatar 头像
     * @param country 锅家
     * @param userType 用户类型
     * @return User
     */
    User commonRegister(String identity, String email, String nickName, String avatar, String country, String userType);

    /**
     * 通过手机号获取用户信息
     * @param phone 手机号
     * @param countryCode 国标区号
     * @param userType 用户类型
     * @return User
     */
    User getByPhoneAndType(String phone, String countryCode, String userType);

    /**
     * 手机号修改密码
     * @param request 修改密码请求对象
     * @return Boolean
     */
    Boolean phoneUpdatePassword(UpdatePasswordRequest request);

    /**
     * 邮箱修改密码
     * @param request 修改密码请求对象
     * @return Boolean
     */
    Boolean emailUpdatePassword(UpdatePasswordRequest request);

    /**
     * 邮箱注册
     * @param email 邮箱
     * @param password 密码
     * @param userType 用户类型
     * @return User
     */
    User emailRegister(String email, String password, String userType);

    /**
     * 游客注册
     */
    User visitorRegister(VisitorRegisterRequest registerRequest);

    /**
     * 查询用户信息
     * @param identity 用户标识
     * @param type 用户类型
     */
    User getByIdentityAndType(String identity, String type);

    /**
     * 获取用户消费记录
     * @param uid 用户uid
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<ExpensesRecordResponse> getExpensesRecord(Integer uid, PageParamRequest pageParamRequest);

    /**
     * 获取用户持有优惠券
     * @param uid 用户uid
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<UserHaveCouponResponse> getHaveCoupons(Integer uid, PageParamRequest pageParamRequest);

    /**
     * 商户端用户分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<UserResponse> getMerchantPage(MerchantUserSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 是否用户使用标签
     * @param tagId 标签id
     * @return Boolean
     */
    Boolean isUsedTag(Integer tagId);
}
