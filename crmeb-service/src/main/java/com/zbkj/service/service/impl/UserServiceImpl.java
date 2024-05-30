package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.*;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.coupon.StoreCoupon;
import com.zbkj.common.model.coupon.StoreCouponUser;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.model.user.User;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.token.FrontTokenComponent;
import com.zbkj.common.utils.*;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.UserDao;
import com.zbkj.service.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户表 服务实现类
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
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private FrontTokenComponent tokenComponet;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private UserTagService userTagService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private StoreOrderService storeOrderService;

    @Autowired
    private StoreCouponUserService storeCouponUserService;

    @Autowired
    private StoreCouponService storeCouponService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private StoreProductRelationService storeProductRelationService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private EmailService emailService;

    /**
     * 分页显示用户表
     *
     * @param request          搜索条件
     * @param pageParamRequest 分页参数
     */
    @Override
    public PageInfo<UserResponse> getPlatformPage(UserSearchRequest request, PageParamRequest pageParamRequest) {
        Page<User> pageUser = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        Map<String, Object> map = CollUtil.newHashMap();
        if (StrUtil.isNotEmpty(request.getNikename())) {
            map.put("nikename", request.getNikename());
        }
        if (StrUtil.isNotEmpty(request.getPhone())) {
            map.put("phone", request.getPhone());
        }
        if (StrUtil.isNotEmpty(request.getEmail())) {
            map.put("email", request.getEmail());
        }
        if (StrUtil.isNotBlank(request.getLabelId())) {
            String tagIdSql = CrmebUtil.getFindInSetSql("u.tag_id", request.getLabelId());
            map.put("tagIdSql", tagIdSql);
        }
        if (StrUtil.isNotBlank(request.getUserType())) {
            map.put("userType", request.getUserType());
        }
        if (StrUtil.isNotBlank(request.getPayCount())) {
            map.put("payCount", Integer.valueOf(request.getPayCount()));
        }
        if (ObjectUtil.isNotNull(request.getStatus())) {
            map.put("status", request.getStatus() ? 1 : 0);
        }
        dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
        if (StrUtil.isNotBlank(dateLimit.getStartTime())) {
            map.put("startTime", dateLimit.getStartTime());
            map.put("endTime", dateLimit.getEndTime());
            map.put("accessType", request.getAccessType());
        }
        List<User> userList = userDao.findAdminList(map);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : userList) {
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);
            if (StrUtil.isNotEmpty(userResponse.getPhone())) {
                userResponse.setPhone(CrmebUtil.maskMobile(userResponse.getPhone()));
            }
            userResponses.add(userResponse);
        }
        return CommonPage.copyPageInfo(pageUser, userResponses);
    }

    /**
     * 用户分组
     * @param request 分组参数
     */
    @Override
    public Boolean group(UserAssignGroupRequest request) {
        //循环id处理
        List<Integer> idList = CrmebUtil.stringToArray(request.getUids());
        idList = idList.stream().distinct().collect(Collectors.toList());
        List<User> list = getListInUid(idList);
        if (CollUtil.isEmpty(list)) throw new CrmebException("没有找到用户信息");
        for (User user : list) {
            user.setGroupId(request.getGroupId().toString());
        }
        return updateBatchById(list, 100);
    }

    /**
     * 用户id in list
     *
     * @param uidList List<Integer> id
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    private List<User> getListInUid(List<Integer> uidList) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(User::getUid, uidList);
        return userDao.selectList(lambdaQueryWrapper);
    }

    /**
     * 获取个人资料
     * @return User
     */
    @Override
    public User getInfo() {
        if (getUserId() == 0) {
            return null;
        }
        return getById(getUserId());
    }

    /**
     * 获取个人资料
     * @return User
     */
    @Override
    public User getInfoException() {
        User user = getInfo();
        if (ObjectUtil.isNull(user)) {
            throw new CrmebException("User information does not exist！");
        }

        if (!user.getStatus()) {
            throw new CrmebException("User has been disabled！");
        }
        return user;
    }

    /**
     * 获取当前用户id
     *
     * @return Integer
     */
    @Override
    public Integer getUserIdException() {
        Integer id = tokenComponet.getUserId();
        if (ObjectUtil.isNull(id)) {
            throw new CrmebException("Your login information has expired, please log in again！");
        }
        return id;
    }

    /**
     * 获取当前用户id
     * @return Integer
     */
    @Override
    public Integer getUserId() {
        Integer id = tokenComponet.getUserId();
        return ObjectUtil.isNotNull(id) ? id : 0;
    }


    /**
     * 按开始结束时间查询每日新增用户数量
     *
     * @param date String 时间范围
     * @return HashMap<String, Object>
     */
    @Override
    public Map<Object, Object> getAddUserCountGroupDate(String date) {
        Map<Object, Object> map = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("count(uid) as uid", "left(create_time, 10) as create_time");
        if (StrUtil.isNotBlank(date)) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(date);
            queryWrapper.between("create_time", dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        queryWrapper.groupBy("left(create_time, 10)").orderByAsc("create_time");
        List<User> list = userDao.selectList(queryWrapper);
        if (list.size() < 1) {
            return map;
        }

        for (User user : list) {
            map.put(DateUtil.dateToStr(user.getCreateTime(), DateConstants.DATE_FORMAT_DATE), user.getUid());
        }
        return map;
    }

    /**
     * 用户中心
     * @return UserCenterResponse
     */
    @Override
    public UserCenterResponse getUserCenter() {
        User currentUser = getInfo();
        if (ObjectUtil.isNull(currentUser)) {
            throw new CrmebException("Your login has expired, please login first");
        }
        UserCenterResponse userCenterResponse = new UserCenterResponse();
        BeanUtils.copyProperties(currentUser, userCenterResponse);
        // 优惠券数量
        userCenterResponse.setCouponCount(storeCouponUserService.getUseCount(currentUser.getUid()));
        // 收藏数量
        userCenterResponse.setCollectCount(storeProductRelationService.getCollectCountByUid(currentUser.getUid()));

        return userCenterResponse;
    }

    /**
     * 根据用户id获取用户列表 map模式
     *
     * @return HashMap<Integer, User>
     */
    @Override
    public HashMap<Integer, User> getMapListInUid(List<Integer> uidList) {
        List<User> userList = getListInUid(uidList);
        return getMapByList(userList);
    }

    /**
     * 根据用户id获取用户列表 map模式
     *
     * @return HashMap<Integer, User>
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    private HashMap<Integer, User> getMapByList(List<User> list) {
        HashMap<Integer, User> map = new HashMap<>();
        if (null == list || list.size() < 1) {
            return map;
        }

        for (User user : list) {
            map.put(user.getUid(), user);
        }

        return map;
    }

    /**
     * 用户分配标签
     * @param request 标签参数
     */
    @Override
    public Boolean tag(UserAssignTagRequest request) {
        //循环id处理
        List<Integer> idList = CrmebUtil.stringToArray(request.getUids());
        idList = idList.stream().distinct().collect(Collectors.toList());
        List<User> list = getListInUid(idList);
        if (CollUtil.isEmpty(list)) throw new CrmebException("没有找到用户信息");
        if (list.size() < 1) {
            throw new CrmebException("没有找到用户信息");
        }
        for (User user : list) {
            user.setTagId(request.getTagIds());
        }
        return updateBatchById(list, 100);
    }

    /**
     * 检测手机验证码
     */
    private void checkValidateCode(String phone, String value) {
        Object validateCode = redisUtil.get(getValidateCodeRedisKey(phone));
        if (validateCode == null) {
            throw new CrmebException("Verification code has expired");
        }

        if (!validateCode.toString().equals(value)) {
            throw new CrmebException("Verification code error");
        }
    }

    /**
     * 检测手机验证码key
     *
     * @param phone String 手机号
     * @return String
     */
    @Override
    public String getValidateCodeRedisKey(String phone) {
        return SmsConstants.SMS_VALIDATE_PHONE + phone;
    }

    /**
     * 手机号注册用户
     *
     * @param phone     手机号
     * @param countryCode 国标区号
     * @return User
     */
    @Override
    public User registerPhone(String phone, String countryCode, String password) {
        User user = new User();
        user.setAccount(phone);
        user.setPwd(CrmebUtil.encryptPassword(password, countryCode.concat(phone)));
        user.setPhone(phone);
        user.setCountryCode(countryCode);
        user.setUserType(UserConstants.USER_LOGIN_TYPE_PHONE);
        user.setNickname(CommonUtil.createNickName(phone));
        user.setAvatar(systemConfigService.getValueByKey(SysConfigConstants.USER_DEFAULT_AVATAR_CONFIG_KEY));
        Date nowDate = DateUtil.nowDateTime();
        user.setCreateTime(nowDate);
        user.setLastLoginTime(nowDate);
        user.setIdentity(CommonUtil.createIdentity());
        // 推广人
        user.setSpreadUid(0);

        // 查询是否有新人注册赠送优惠券
        List<StoreCouponUser> couponUserList = CollUtil.newArrayList();
        List<StoreCoupon> couponList = storeCouponService.findRegisterList();
        if (CollUtil.isNotEmpty(couponList)) {
            userRegisterCouponInit(couponList, couponUserList);
        }
        Boolean execute = transactionTemplate.execute(e -> {
            save(user);
            // 赠送客户优惠券
            if (CollUtil.isNotEmpty(couponUserList)) {
                couponUserList.forEach(couponUser -> couponUser.setUid(user.getUid()));
                storeCouponUserService.saveBatch(couponUserList);
                couponList.forEach(coupon -> storeCouponService.deduction(coupon.getId(), 1, coupon.getIsLimited()));
            }
            return Boolean.TRUE;
        });
        if (!execute) {
            throw new CrmebException("Failed to create user!");
        }
        return user;
    }

    /**
     * 会员详情顶部数据
     *
     * @param userId Integer 用户id
     * @return TopDetail
     */
    @Override
    public TopDetail getTopDetail(Integer userId) {
        TopDetail topDetail = new TopDetail();
        topDetail.setMothConsumeCount(storeOrderService.getSumPayPriceByUidAndDate(userId, DateConstants.SEARCH_DATE_MONTH));
        topDetail.setAllConsumeCount(storeOrderService.getSumPayPriceByUid(userId));
        topDetail.setMothOrderCount(storeOrderService.getOrderCountByUidAndDate(userId, DateConstants.SEARCH_DATE_MONTH));
        topDetail.setAllOrderCount(storeOrderService.getOrderCountByUid(userId));
        return topDetail;
    }

    /**
     * 用户购买统计
     *
     * @param minPayCount int 最小消费次数
     * @param maxPayCount int 最大消费次数
     * @return Integer
     */
    @Override
    public Integer getCountByPayCount(int minPayCount, int maxPayCount) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.between(User::getPayCount, minPayCount, maxPayCount);
        return userDao.selectCount(lambdaQueryWrapper);
    }

    /**
     * 更新用户
     *
     * @param userRequest 用户参数
     * @return Boolean
     */
    @Override
    public Boolean updateUser(UserUpdateRequest userRequest) {
        User tempUser = getById(userRequest.getUid());
        if (ObjectUtil.isNull(tempUser)) {
            throw new CrmebException("用户不存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userRequest, user);
        return updateById(user);
    }

    /**
     * 获取用户总人数
     */
    @Override
    public Integer getTotalNum() {
        LambdaQueryWrapper<User> lqw = Wrappers.lambdaQuery();
        lqw.select(User::getUid);
        return userDao.selectCount(lqw);
    }

    /**
     * 根据日期段获取注册用户数量
     * @param startDate 日期
     * @param endDate 日期
     * @return UserOverviewResponse
     */
    @Override
    public Integer getRegisterNumByPeriod(String startDate, String endDate) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.select("uid");
        wrapper.apply("date_format(create_time, '%Y-%m-%d') between {0} and {1}", startDate, endDate);
        return userDao.selectCount(wrapper);
    }

    /**
     * 获取用户性别数据
     * @return List
     */
    @Override
    public List<User> getSexData() {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.select("sex", "count(uid) as pay_count");
        wrapper.groupBy("sex");
        return userDao.selectList(wrapper);
    }

    /**
     * 获取用户渠道数据
     * @return List
     */
    @Override
    public List<User> getChannelData() {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.select("user_type", "count(uid) as pay_count");
        wrapper.groupBy("user_type");
        return userDao.selectList(wrapper);
    }

    /**
     * 获取所有用户的id跟地址
     * @return List
     */
    @Override
    public List<User> findIdAndAddresList() {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.select("uid", "addres");
        return userDao.selectList(wrapper);
    }

    /**
     * 修改个人资料
     * @param request 修改信息
     */
    @Override
    public Boolean editUser(UserEditRequest request) {
        User user = getInfoException();
        user.setAvatar(systemAttachmentService.clearPrefix(request.getAvatar()));
        user.setNickname(request.getNickname());
        return updateById(user);
    }

    /**
     * 获取用户详情
     * @param id 用户uid
     */
    @Override
    public User getInfoByUid(Integer id) {
        User user = getById(id);
        if (ObjectUtil.isNull(user)) {
            throw new CrmebException("用户不存在");
        }
        return user;
    }

    /**
     * 通过邮箱跟用户subject获取用户信息
     * @param email 邮箱
     * @param identity 用户标识
     * @param userType 用户类型
     * @return User
     */
    @Override
    public User getByEmailAndIdentityAndType(String email, String identity, String userType) {
        if (StrUtil.isBlank(email) && StrUtil.isBlank(identity)) {
            throw new CrmebException("email or identity can't all be empty");
        }
        LambdaQueryWrapper<User> lqw = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(email)) {// 目前只有facebook没有邮箱
            lqw.eq(User::getEmail, email);
        }
        lqw.eq(User::getUserType, userType);
        if (StrUtil.isNotBlank(identity)) {
            lqw.eq(User::getIdentity, identity);
        }
        lqw.last(" limit 1");
        return userDao.selectOne(lqw);
    }

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
    @Override
    public User commonRegister(String identity, String email, String nickName, String avatar, String country, String userType) {
        User user = new User();
        user.setIdentity(StrUtil.isNotBlank(identity) ? identity : CommonUtil.createIdentity());
        user.setEmail(Optional.ofNullable(email).orElse(""));
        user.setNickname(StrUtil.isNotBlank(nickName) ? nickName : CommonUtil.createNickName(user.getIdentity()));
        user.setAvatar(StrUtil.isNotBlank(avatar) ? avatar : systemConfigService.getValueByKey(SysConfigConstants.USER_DEFAULT_AVATAR_CONFIG_KEY));
        if (StrUtil.isNotBlank(country)) {
            user.setCountry(country);
        }

        user.setPwd(CommonUtil.createPwd(user.getIdentity()));
        user.setUserType(userType);
        user.setLoginType(userType);
        Date nowDate = DateUtil.nowDateTime();
        user.setCreateTime(nowDate);
        user.setLastLoginTime(nowDate);
        user.setSpreadUid(0);
        // 查询是否有新人注册赠送优惠券
        List<StoreCouponUser> couponUserList = CollUtil.newArrayList();
        List<StoreCoupon> couponList = storeCouponService.findRegisterList();
        if (CollUtil.isNotEmpty(couponList)) {
            userRegisterCouponInit(couponList, couponUserList);
        }
        Boolean execute = transactionTemplate.execute(e -> {
            save(user);
            // 赠送客户优惠券
            if (CollUtil.isNotEmpty(couponUserList)) {
                couponUserList.forEach(couponUser -> couponUser.setUid(user.getUid()));
                storeCouponUserService.saveBatch(couponUserList);
                couponList.forEach(coupon -> storeCouponService.deduction(coupon.getId(), 1, coupon.getIsLimited()));
            }
            return Boolean.TRUE;
        });
        if (!execute) {
            throw new CrmebException("Failed to create user!");
        }
        return user;
    }

    /**
     * 用户注册优惠券初始化
     * @param couponList 优惠券列表
     */
    private void userRegisterCouponInit(List<StoreCoupon> couponList, List<StoreCouponUser> couponUserList) {
        couponList.forEach(storeCoupon -> {
            //是否有固定的使用时间
            if (!storeCoupon.getIsFixedTime()) {
                String endTime = DateUtil.addDay(DateUtil.nowDate(DateConstants.DATE_FORMAT), storeCoupon.getDay(), DateConstants.DATE_FORMAT);
                storeCoupon.setUseEndTime(DateUtil.strToDate(endTime, DateConstants.DATE_FORMAT));
                storeCoupon.setUseStartTime(DateUtil.nowDateTimeReturnDate(DateConstants.DATE_FORMAT));
            }
            StoreCouponUser storeCouponUser = new StoreCouponUser();
            storeCouponUser.setCouponId(storeCoupon.getId());
            storeCouponUser.setName(storeCoupon.getName());
            storeCouponUser.setMoney(storeCoupon.getMoney());
            storeCouponUser.setMinPrice(storeCoupon.getMinPrice());
            storeCouponUser.setStartTime(storeCoupon.getUseStartTime());
            storeCouponUser.setEndTime(storeCoupon.getUseEndTime());
            storeCouponUser.setUseType(storeCoupon.getUseType());
            storeCouponUser.setType(CouponConstants.STORE_COUPON_USER_TYPE_REGISTER);
            if (storeCoupon.getUseType() > 1) {
                storeCouponUser.setPrimaryKey(storeCoupon.getPrimaryKey());
            }
            couponUserList.add(storeCouponUser);
        });
    }

    /**
     * 通过手机号获取用户信息
     * @param phone 手机号
     * @param countryCode 国标区号
     * @param userType 用户类型
     * @return User
     */
    @Override
    public User getByPhoneAndType(String phone, String countryCode, String userType) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getPhone, phone);
        lqw.eq(User::getCountryCode, countryCode);
        lqw.eq(User::getUserType, userType);
        lqw.last(" limit 1");
        return userDao.selectOne(lqw);
    }

    /**
     * 手机号修改密码
     * @param request 修改密码请求对象
     * @return Boolean
     */
    @Override
    public Boolean phoneUpdatePassword(UpdatePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getPasswordAgain())) {
            throw new CrmebException("The two entered passwords do not match");
        }
        User user = getInfoException();
        if (!user.getUserType().equals(UserConstants.USER_LOGIN_TYPE_PHONE)) {
            throw new CrmebException("wrong user type");
        }
        checkValidateCode(user.getCountryCode().concat(user.getPhone()), request.getCaptcha());
        String password = CrmebUtil.encryptPassword(request.getNewPassword(), user.getCountryCode().concat(user.getPhone()));
        if (password.equals(user.getPwd())) {
            return Boolean.TRUE;
        }
        user.setPwd(password);
        return updateById(user);
    }

    /**
     * 邮箱修改密码
     * @param request 修改密码请求对象
     * @return Boolean
     */
    @Override
    public Boolean emailUpdatePassword(UpdatePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getPasswordAgain())) {
            throw new CrmebException("The two entered passwords do not match");
        }
        User user = getInfoException();
        if (!user.getUserType().equals(UserConstants.USER_LOGIN_TYPE_EMAIL)) {
            throw new CrmebException("wrong user type");
        }
        emailService.checkEmailValidateCode(user.getEmail(), request.getCaptcha());
        String password = CrmebUtil.encryptPassword(request.getNewPassword(), user.getIdentity());
        if (password.equals(user.getPwd())) {
            return Boolean.TRUE;
        }
        user.setPwd(password);
        return updateById(user);
    }

    /**
     * 邮箱注册
     * @param email 邮箱
     * @param password 密码
     * @param userType 用户类型
     * @return User
     */
    @Override
    public User emailRegister(String email, String password, String userType) {
        User user = new User();
        user.setIdentity(CommonUtil.createIdentity());
        user.setEmail(email);
        user.setNickname(CommonUtil.createNickName(user.getIdentity()));
        user.setAvatar(systemConfigService.getValueByKey(SysConfigConstants.USER_DEFAULT_AVATAR_CONFIG_KEY));
        user.setPwd(CrmebUtil.encryptPassword(password, user.getIdentity()));
        user.setUserType(userType);
        user.setLoginType(userType);
        Date nowDate = DateUtil.nowDateTime();
        user.setCreateTime(nowDate);
        user.setLastLoginTime(nowDate);
        user.setSpreadUid(0);
        // 查询是否有新人注册赠送优惠券
        List<StoreCouponUser> couponUserList = CollUtil.newArrayList();
        List<StoreCoupon> couponList = storeCouponService.findRegisterList();
        if (CollUtil.isNotEmpty(couponList)) {
            userRegisterCouponInit(couponList, couponUserList);
        }

        Boolean execute = transactionTemplate.execute(e -> {
            save(user);
            // 赠送客户优惠券
            if (CollUtil.isNotEmpty(couponUserList)) {
                couponUserList.forEach(couponUser -> couponUser.setUid(user.getUid()));
                storeCouponUserService.saveBatch(couponUserList);
                couponList.forEach(coupon -> storeCouponService.deduction(coupon.getId(), 1, coupon.getIsLimited()));
            }
            return Boolean.TRUE;
        });
        if (!execute) {
            throw new CrmebException("Failed to create user!");
        }
        return user;
    }

    /**
     * 游客注册
     */
    @Override
    public User visitorRegister(VisitorRegisterRequest registerRequest) {
        User user = new User();
        user.setIdentity(CommonUtil.createIdentity());
        user.setEmail(registerRequest.getEmail());
        user.setNickname(CommonUtil.createNickName(user.getIdentity()));
        user.setAvatar(systemConfigService.getValueByKey(SysConfigConstants.USER_DEFAULT_AVATAR_CONFIG_KEY));
        user.setPwd(CommonUtil.createPwd(user.getIdentity()));
        user.setUserType(UserConstants.USER_LOGIN_TYPE_VISITOR);
        user.setLoginType(UserConstants.USER_LOGIN_TYPE_VISITOR);

        if (StrUtil.isNotBlank(registerRequest.getPhone())) {
            user.setPhone(registerRequest.getPhone());
            user.setCountryCode(registerRequest.getCountryCode());
        }
        Date nowDate = DateUtil.nowDateTime();
        user.setCreateTime(nowDate);
        user.setLastLoginTime(nowDate);
        user.setSpreadUid(0);
        boolean save = save(user);
        if (!save) {
            throw new CrmebException("Failed to create visitor!");
        }
        return user;
    }

    /**
     * 查询用户信息
     * @param identity 用户标识
     * @param type 用户类型
     */
    @Override
    public User getByIdentityAndType(String identity, String type) {
        LambdaQueryWrapper<User> lqw = Wrappers.lambdaQuery();
        lqw.eq(User::getIdentity, identity);
        lqw.eq(User::getUserType, type);
        lqw.last(" limit 1");
        return userDao.selectOne(lqw);
    }

    /**
     * 获取用户消费记录
     * @param uid 用户uid
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<ExpensesRecordResponse> getExpensesRecord(Integer uid, PageParamRequest pageParamRequest) {
        return storeOrderService.getExpensesRecord(uid, pageParamRequest);
    }

    /**
     * 获取用户持有优惠券
     * @param uid 用户uid
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<UserHaveCouponResponse> getHaveCoupons(Integer uid, PageParamRequest pageParamRequest) {
        return storeCouponUserService.getHaveCouponsByUid(uid, pageParamRequest);
    }

    /**
     * 商户端用户分页列表
     * @param request 查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<UserResponse> getMerchantPage(MerchantUserSearchRequest request, PageParamRequest pageParamRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Page<User> pageUser = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        Map<String, Object> map = CollUtil.newHashMap();
        map.put("merId", systemAdmin.getMerId());
        if (StrUtil.isNotBlank(request.getUserType())) {
            map.put("userType", request.getUserType());
        }
        if (StrUtil.isNotBlank(request.getSex())) {
            map.put("sex", Integer.valueOf(request.getSex()));
        }
        dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
        if (StrUtil.isNotBlank(dateLimit.getStartTime())) {
            map.put("startTime", dateLimit.getStartTime());
            map.put("endTime", dateLimit.getEndTime());
        }
        if (StrUtil.isNotBlank(request.getEmail())) {
            map.put("email", request.getEmail());
        }
        if (StrUtil.isNotBlank(request.getKeywords() )) {
            map.put("keywords", request.getKeywords());
        }
        List<User> userList = userDao.findMerchantList(map);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : userList) {
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);
//            // 获取分组信息
//            if (StrUtil.isNotBlank(user.getGroupId())) {
//                userResponse.setGroupName(userGroupService.getGroupNameInId(user.getGroupId()));
//                userResponse.setGroupId(user.getGroupId());
//            }
//            // 获取标签信息
//            if (StrUtil.isNotBlank(user.getTagId())) {
//                userResponse.setTagName(userTagService.getGroupNameInId(user.getTagId()));
//                userResponse.setTagId(user.getTagId());
//            }
            userResponse.setPhone(CrmebUtil.maskMobile(userResponse.getPhone()));
            userResponses.add(userResponse);
        }
        return CommonPage.copyPageInfo(pageUser, userResponses);
    }

    /**
     * 是否用户使用标签
     * @param tagId 标签id
     * @return Boolean
     */
    @Override
    public Boolean isUsedTag(Integer tagId) {
        LambdaQueryWrapper<User> lqw = Wrappers.lambdaQuery();
        lqw.select(User::getUid);
        lqw.apply(StrUtil.format("find_in_set('{}', tag_id)", tagId));
        lqw.last(" limit 1");
        User user = userDao.selectOne(lqw);
        return ObjectUtil.isNotNull(user);
    }

    /**
     * 根据日期获取注册用户数量
     * @param date 日期
     * @return Integer
     */
    @Override
    public Integer getRegisterNumByDate(String date) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.select("uid");
        if (StrUtil.isNotEmpty(date)) {
            wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        }
        return userDao.selectCount(wrapper);
    }

    /**
     * 更新用户等级
     * @param uid 用户id
     * @param levelId 会员等级id
     * @return Boolean
     */
    private Boolean updateLevel(Integer uid, Integer levelId) {
        LambdaUpdateWrapper<User> luw = Wrappers.lambdaUpdate();
        luw.set(User::getLevel, levelId);
        luw.eq(User::getUid, uid);
        return update(luw);
    }
}
