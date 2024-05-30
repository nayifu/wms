package com.zbkj.service.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.user.UserAddress;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.UserAddressRequest;
import com.zbkj.common.utils.RestTemplateUtil;
import com.zbkj.common.utils.ValidateFormUtil;
import com.zbkj.service.dao.UserAddressDao;
import com.zbkj.service.service.UserAddressService;
import com.zbkj.service.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * UserAddressServiceImpl 接口实现
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
public class UserAddressServiceImpl extends ServiceImpl<UserAddressDao, UserAddress> implements UserAddressService {

    @Resource
    private UserAddressDao dao;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    /**
     * 列表
     *
     * @return List<UserAddress>
     */
    @Override
    public PageInfo<UserAddress> getList(PageParamRequest pageParamRequest) {
        Integer UserId = userService.getUserIdException();
        Page<Object> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<UserAddress> lqw = Wrappers.lambdaQuery();
        lqw.select(UserAddress::getId, UserAddress::getRealName, UserAddress::getPhone, UserAddress::getCountryCode,
                UserAddress::getCountry, UserAddress::getEmail, UserAddress::getDetail, UserAddress::getPostCode, UserAddress::getIsDefault);
        lqw.eq(UserAddress::getUid, UserId);
        lqw.eq(UserAddress::getIsDel, false);
        lqw.orderByDesc(UserAddress::getIsDefault);
        lqw.orderByDesc(UserAddress::getId);
        List<UserAddress> list = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, list);
    }

    /**
     * 创建地址
     *
     * @param request UserAddressRequest 参数
     * @return List<UserAddress>
     */
    @Override
    public UserAddress create(UserAddressRequest request) {
        if (StrUtil.isNotBlank(request.getCountryCode()) && request.getCountryCode().equals("86")) {
            if (StrUtil.isNotBlank(request.getPhone())) {
                ValidateFormUtil.isPhone(request.getPhone(), "wrong mobile number");
            }
        }

        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(request, userAddress);
        userAddress.setUid(userService.getUserIdException());
        if (userAddress.getIsDefault()) {
            //把当前用户其他默认地址取消
            cancelDefault(userAddress.getUid());
        }
        saveOrUpdate(userAddress);
        return userAddress;
    }

    /**
     * 设置默认
     *
     * @param id Integer id
     * @return UserAddress
     */
    @Override
    public Boolean def(Integer id) {
        //把当前用户其他默认地址取消
        cancelDefault(userService.getUserIdException());
        UserAddress userAddress = new UserAddress();
        userAddress.setId(id);
        userAddress.setUid(userService.getUserIdException());
        userAddress.setIsDefault(true);
        return updateById(userAddress);
    }

    /**
     * 删除
     *
     * @param id Integer id
     * @return UserAddress
     */
    @Override
    public Boolean delete(Integer id) {
        //把当前用户其他默认地址取消
        LambdaQueryWrapper<UserAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAddress::getId, id);
        lambdaQueryWrapper.eq(UserAddress::getUid, userService.getUserIdException());
        return dao.delete(lambdaQueryWrapper) > 0;
    }

    /**
     * 获取默认地址
     *
     * @return UserAddress
     */
    @Override
    public UserAddress getDefault() {
        //把当前用户其他默认地址取消
        LambdaQueryWrapper<UserAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAddress::getIsDefault, true);
        lambdaQueryWrapper.eq(UserAddress::getUid, userService.getUserId());
        lambdaQueryWrapper.eq(UserAddress::getIsDel, false);
        return dao.selectOne(lambdaQueryWrapper);
    }

    @Override
    public UserAddress getById(Integer addressId) {
        LambdaQueryWrapper<UserAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAddress::getId, addressId);
        lambdaQueryWrapper.eq(UserAddress::getIsDel, false);
        return dao.selectOne(lambdaQueryWrapper);
    }

    /**
     * 获取地址详情
     *
     * @param id 地址id
     * @return UserAddress
     */
    @Override
    public UserAddress getDetail(Integer id) {
        Integer UserId = userService.getUserIdException();
        LambdaQueryWrapper<UserAddress> lqw = Wrappers.lambdaQuery();
        lqw.select(UserAddress::getId, UserAddress::getRealName, UserAddress::getPhone, UserAddress::getCountryCode,
                UserAddress::getCountry, UserAddress::getEmail, UserAddress::getDetail, UserAddress::getPostCode, UserAddress::getIsDefault);
        lqw.eq(UserAddress::getId, id);
        lqw.eq(UserAddress::getUid, UserId);
        lqw.eq(UserAddress::getIsDel, false);
        return dao.selectOne(lqw);
    }

    /**
     * 获取默认地址
     *
     * @return UserAddress
     */
    @Override
    public UserAddress getDefaultByUid(Integer uid) {
        LambdaQueryWrapper<UserAddress> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserAddress::getIsDefault, true);
        lambdaQueryWrapper.eq(UserAddress::getUid, uid);
        return dao.selectOne(lambdaQueryWrapper);
    }

    /**
     * 根据用户Ip获取地址信息
     */
    @Override
    public JSONObject ipInfo(HttpServletRequest request) {
        String clientIP = ServletUtil.getClientIP(request, null);
        if (StrUtil.isBlank(clientIP)) {
            throw new CrmebException("Can't get IP information");
        }
        System.out.println("获取到的Ip是: " + clientIP);
//        String pattern = "/^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$/";
//        if (!ReUtil.isMatch(pattern, clientIP)) {
//            throw new CrmebException("获取的不是正确的ip地址");
//        }
        String url = StrUtil.format(Constants.IP_INFO_URI, clientIP);
        return restTemplateUtil.getData(url);
    }

    /**
     * 获取所有的用户地址
     *
     * @return List
     */
    @Override
    public List<UserAddress> getAllList() {
        Integer UserId = userService.getUserIdException();
        LambdaQueryWrapper<UserAddress> lqw = Wrappers.lambdaQuery();
        lqw.select(UserAddress::getId, UserAddress::getRealName, UserAddress::getPhone, UserAddress::getCountryCode,
                UserAddress::getCountry, UserAddress::getEmail, UserAddress::getDetail, UserAddress::getPostCode, UserAddress::getIsDefault);
        lqw.eq(UserAddress::getUid, UserId);
        lqw.eq(UserAddress::getIsDel, false);
        lqw.orderByDesc(UserAddress::getIsDefault);
        lqw.orderByDesc(UserAddress::getId);
        return dao.selectList(lqw);
    }

    /**
     * 取消默认地址
     *
     * @param userId Integer 城市id
     */
    private void cancelDefault(Integer userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setIsDefault(false);
        LambdaQueryWrapper<UserAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserAddress::getUid, userId);
        update(userAddress, lambdaQueryWrapper);
    }

}

