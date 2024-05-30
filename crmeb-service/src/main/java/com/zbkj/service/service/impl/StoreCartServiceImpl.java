package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.constants.DateConstants;
import com.zbkj.common.constants.ProductConstants;
import com.zbkj.common.constants.RedisConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.cat.StoreCart;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.model.product.StoreProductAttrValue;
import com.zbkj.common.model.user.User;
import com.zbkj.common.request.CartNumRequest;
import com.zbkj.common.request.CartRequest;
import com.zbkj.common.request.CartResetRequest;
import com.zbkj.common.response.CartInfoResponse;
import com.zbkj.common.response.CartMerchantResponse;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.service.dao.StoreCartDao;
import com.zbkj.service.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * StoreCartServiceImpl 接口实现
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
public class StoreCartServiceImpl extends ServiceImpl<StoreCartDao, StoreCart> implements StoreCartService {

    @Resource
    private StoreCartDao dao;

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreProductAttrValueService storeProductAttrValueService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
    * 列表
    * @param isValid 是否失效
    * @return List<CartMerchantResponse>
    */
    @Override
    public List<CartMerchantResponse> getList(boolean isValid) {
        Integer userId = userService.getUserIdException();
        //带 StoreCart 类的多条件查询
        LambdaQueryWrapper<StoreCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StoreCart::getUid, userId);
        lambdaQueryWrapper.eq(StoreCart::getStatus, isValid);
        lambdaQueryWrapper.orderByDesc(StoreCart::getId);
        List<StoreCart> cartList = dao.selectList(lambdaQueryWrapper);
        if (CollUtil.isEmpty(cartList)) {
            return CollUtil.newArrayList();
        }

        List<Integer> merIdList = cartList.stream().map(StoreCart::getMerId).distinct().collect(Collectors.toList());
        Map<Integer, Merchant> merchantMap = merchantService.getMerIdMapByIdList(merIdList);
        List<CartMerchantResponse> responseList = CollUtil.newArrayList();
        merIdList.forEach(merId -> {
            CartMerchantResponse merchantResponse = new CartMerchantResponse();
            merchantResponse.setMerId(merId);
            merchantResponse.setMerName(merchantMap.get(merId).getName());
            List<StoreCart> merCartList = cartList.stream().filter(e -> e.getMerId().equals(merId)).collect(Collectors.toList());
            List<CartInfoResponse> infoResponseList =  merCartList.stream().map(storeCart -> {
                CartInfoResponse cartInfoResponse = new CartInfoResponse();
                BeanUtils.copyProperties(storeCart, cartInfoResponse);
                // 获取商品信息
                StoreProduct storeProduct = storeProductService.getCartByProId(storeCart.getProductId());
                cartInfoResponse.setImage(storeProduct.getImage());
                cartInfoResponse.setStoreName(storeProduct.getStoreName());
                if (!isValid) {// 失效商品直接掠过
                    cartInfoResponse.setAttrStatus(false);
                    return cartInfoResponse;
                }
                // 获取对应的商品规格信息(只会有一条信息)
                StoreProductAttrValue attrValue = storeProductAttrValueService.getByProductIdAndAttrId(storeCart.getProductId(),
                        storeCart.getProductAttrUnique(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
                // 规格不存在即失效
                if (ObjectUtil.isNull(attrValue)) {
                    cartInfoResponse.setAttrStatus(false);
                    return cartInfoResponse;
                }
                if (StrUtil.isNotBlank(attrValue.getImage())) {
                    cartInfoResponse.setImage(attrValue.getImage());
                }
                cartInfoResponse.setSuk(attrValue.getSku());
                cartInfoResponse.setPrice(attrValue.getPrice());
                cartInfoResponse.setAttrId(attrValue.getId());
                cartInfoResponse.setAttrStatus(attrValue.getStock() > 0);
                cartInfoResponse.setStock(attrValue.getStock());
                return cartInfoResponse;
            }).collect(Collectors.toList());
            merchantResponse.setCartInfoList(infoResponseList);
            responseList.add(merchantResponse);
        });
        return responseList;
    }

    /**
     * 购物车数量
     * @param request 请求参数
     * @return Map<String, Integer>
     */
    @Override
    public Map<String, Integer> getUserCount(CartNumRequest request) {
        Integer userId = userService.getUserIdException();
        Map<String, Integer> map = new HashMap<>();
        int num;
        if (request.getType().equals("total")) {
            num = getUserCountByStatus(userId, request.getNumType());
        } else {
            num = getUserSumByStatus(userId, request.getNumType());
        }
        map.put("count", num);
        return map;
    }

    /**
     * 新增商品至购物车
     * @param storeCartListRequest 购物车参数
     * @return 添加后的成功标识
     */
    @Override
    public Boolean saveCate(List<CartRequest> storeCartListRequest) {
        if (CollUtil.isEmpty(storeCartListRequest)) {
            throw new CrmebException("cart data is null");
        }
        User currentUser = userService.getInfoException();
        return transactionTemplate.execute(exec -> {
            storeCartListRequest.forEach(storeCartRequest -> {
                // 判断商品正常
                StoreProduct product = storeProductService.getById(storeCartRequest.getProductId());
                if (ObjectUtil.isNull(product) || product.getIsDel() || !product.getIsShow()) {
                    throw new CrmebException("No matching product found");
                }
                StoreProductAttrValue attrValue = storeProductAttrValueService.getByProductIdAndAttrId(product.getId(),
                        storeCartRequest.getProductAttrUnique(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
                if (ObjectUtil.isNull(attrValue)) {
                    throw new CrmebException("No corresponding product SKU found");
                }
//                if (attrValue.getStock() < storeCartRequest.getCartNum()) {
//                    throw new CrmebException("Insufficient stock of goods");
//                }

                // 普通商品部分(只有普通商品才能添加购物车)
                // 是否已经有同类型商品在购物车，有则添加数量没有则新增
                StoreCart forUpdateStoreCart = getByUniqueAndUid(storeCartRequest.getProductAttrUnique(), currentUser.getUid());
                if (ObjectUtil.isNotNull(forUpdateStoreCart)) { // 购物车添加数量
                    forUpdateStoreCart.setCartNum(forUpdateStoreCart.getCartNum() + storeCartRequest.getCartNum());
                    boolean updateResult = updateById(forUpdateStoreCart);
                    if (!updateResult) throw new CrmebException("fail to add in order cart");
                } else {// 新增购物车数据
                    StoreCart storeCart = new StoreCart();
                    BeanUtils.copyProperties(storeCartRequest, storeCart);
                    storeCart.setUid(currentUser.getUid());
                    storeCart.setMerId(product.getMerId());
                    if (dao.insert(storeCart) <= 0) throw new CrmebException("fail to add in order cart");
                }
                purchaseStatistics(storeCartRequest.getProductId());
            });
            return Boolean.TRUE;
        });
    }

    /**
     * 加购商品统计
     * @param productId 商品id
     */
    private void purchaseStatistics(Integer productId) {
        String todayStr = DateUtil.date().toString(DateConstants.DATE_FORMAT_DATE);
        // 商品加购量统计(每日/商城)
        redisUtil.incrAndCreate(RedisConstants.PRO_ADD_CART_KEY + todayStr);
        // 商品加购量统计(每日/个体)
        redisUtil.incrAndCreate(StrUtil.format(RedisConstants.PRO_PRO_ADD_CART_KEY, todayStr, productId));
    }

    /**
     * 获取购物车信息
     * @param productAttrUnique 商品规制值
     * @param uid uid
     * @return StoreCart
     */
    private StoreCart getByUniqueAndUid(Integer productAttrUnique, Integer uid) {
        LambdaQueryWrapper<StoreCart> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreCart::getProductAttrUnique, productAttrUnique);
        lqw.eq(StoreCart::getUid, uid);
        lqw.last(" limit 1");
        return dao.selectOne(lqw);
    }

    /**
     * 删除购物车信息
     * @param ids 待删除id
     * @return 删除结果状态
     */
    @Override
    public Boolean deleteCartByIds(List<Integer> ids) {
        return dao.deleteBatchIds(ids) > 0;
    }

    /**
     * 检测商品是否有效 更新购物车商品状态
     * @param productId 商品id
     * @return 跟新结果
     */
    @Override
    public Boolean productStatusNotEnable(Integer productId) {
        LambdaUpdateWrapper<StoreCart> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(StoreCart::getStatus, false);
        wrapper.eq(StoreCart::getProductId, productId);
        wrapper.eq(StoreCart::getStatus, true);
        return update(wrapper);
    }

    /**
     * 购物车重选
     * @param resetRequest 重选数据
     * @return 重选结果
     */
    @Override
    public Boolean resetCart(CartResetRequest resetRequest) {
        LambdaQueryWrapper<StoreCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StoreCart::getId, resetRequest.getId());
        StoreCart storeCart = dao.selectOne(lqw);
        if (ObjectUtil.isNull(storeCart)) throw new CrmebException("Cart does not exist");
        // 判断商品正常
        StoreProduct product = storeProductService.getById(resetRequest.getProductId());
        if (ObjectUtil.isNull(product) || product.getIsDel() || !product.getIsShow()) {
            throw new CrmebException("No matching product found");
        }
        StoreProductAttrValue attrValue = storeProductAttrValueService.getByProductIdAndAttrId(product.getId(),
                resetRequest.getUnique(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        if (ObjectUtil.isNull(attrValue)) {
            throw new CrmebException("No corresponding product SKU found");
        }
//        if (attrValue.getStock() < resetRequest.getNum()) {
//            throw new CrmebException("Insufficient stock of goods");
//        }
        storeCart.setCartNum(resetRequest.getNum());
        storeCart.setProductAttrUnique(resetRequest.getUnique());
        storeCart.setStatus(true);
        storeCart.setMerId(product.getMerId());
        boolean updateResult = dao.updateById(storeCart) > 0;
        if (!updateResult) throw new CrmebException("Failed to reselect add to cart");
        return updateResult;
    }

    /**
     * 对应sku购物车生效
     * @param skuIdList skuIdList
     * @return Boolean
     */
    @Override
    public Boolean productStatusNoEnable(List<Integer> skuIdList) {
        LambdaUpdateWrapper<StoreCart> lqw = new LambdaUpdateWrapper<>();
        lqw.set(StoreCart::getStatus, true);
        lqw.in(StoreCart::getProductAttrUnique, skuIdList);
        return update(lqw);
    }

    /**
     * 删除商品对应的购物车
     * @param productId 商品id
     */
    @Override
    public Boolean productDelete(Integer productId) {
        LambdaUpdateWrapper<StoreCart> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(StoreCart::getProductId, productId);
        return dao.delete(wrapper) > 0;
    }

    /**
     * 通过id和uid获取购物车信息
     * @param id 购物车id
     * @param uid 用户uid
     * @return StoreCart
     */
    @Override
    public StoreCart getByIdAndUid(Integer id, Integer uid) {
        LambdaQueryWrapper<StoreCart> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreCart::getId, id);
        lqw.eq(StoreCart::getUid, uid);
        lqw.eq(StoreCart::getStatus, true);
        return dao.selectOne(lqw);
    }

    /**
     * 修改购物车商品数量
     * @param id 购物车id
     * @param number 数量
     */
    @Override
    public Boolean updateCartNum(Integer id, Integer number) {
        if (number <=0 || number > 99) throw new CrmebException("The number of items cannot be less than 1 and greater than 99");
        StoreCart storeCart = getById(id);
        if (ObjectUtil.isNull(storeCart)) throw new CrmebException("The current cart does not exist");
        if (storeCart.getCartNum().equals(number)) {
            return Boolean.TRUE;
        }
        StoreProductAttrValue attrValue = storeProductAttrValueService.getByProductIdAndAttrId(storeCart.getProductId(),
                storeCart.getProductAttrUnique(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        if (ObjectUtil.isNull(attrValue)) {
            throw new CrmebException("No corresponding product SKU found");
        }
//        if (attrValue.getStock() < number) {
//            throw new CrmebException("Insufficient stock of goods");
//        }
        storeCart.setCartNum(number);
        return updateById(storeCart);
    }

    ///////////////////////////////////////////////////////////////////自定义方法
    /**
     * 购物车商品数量（条数）
     * @param userId Integer 用户id
     * @param status Boolean 商品类型：true-有效商品，false-无效商品
     * @return Integer
     */
    private Integer getUserCountByStatus(Integer userId, Boolean status) {
        //购物车商品种类数量
        LambdaQueryWrapper<StoreCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StoreCart::getUid, userId);
        lambdaQueryWrapper.eq(StoreCart::getStatus, status);
        return dao.selectCount(lambdaQueryWrapper);
    }

    /**
     * 购物车购买商品总数量
     * @param userId Integer 用户id
     * @param status 商品类型：true-有效商品，false-无效商品
     * @return Integer
     */
    private Integer getUserSumByStatus(Integer userId, Boolean status) {
        QueryWrapper<StoreCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("ifnull(sum(cart_num), 0) as cart_num");
        queryWrapper.eq("uid", userId);
        queryWrapper.eq("status", status);
        StoreCart storeCart = dao.selectOne(queryWrapper);
        if (ObjectUtil.isNull(storeCart)) {
            return 0;
        }
        return storeCart.getCartNum();
    }
}

