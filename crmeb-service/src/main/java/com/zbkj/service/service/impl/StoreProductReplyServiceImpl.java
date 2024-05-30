package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.ProductConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.order.StoreOrderInfo;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.model.product.StoreProductAttrValue;
import com.zbkj.common.model.product.StoreProductReply;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.ProductReplySearchRequest;
import com.zbkj.common.request.ProductReplyVirtualRequest;
import com.zbkj.common.request.StoreProductReplyCommentRequest;
import com.zbkj.common.response.ProductDetailReplyResponse;
import com.zbkj.common.response.ProductReplyResponse;
import com.zbkj.common.response.StoreProductReplyResponse;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.MyRecord;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.StoreProductReplyDao;
import com.zbkj.service.service.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * StoreProductReplyServiceImpl 接口实现
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
public class StoreProductReplyServiceImpl extends ServiceImpl<StoreProductReplyDao, StoreProductReply>
        implements StoreProductReplyService {

    @Resource
    private StoreProductReplyDao dao;

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private StoreOrderInfoService storeOrderInfoService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private StoreProductAttrValueService attrValueService;


    /**
    * 平台端商品评论列表
    * @param request 请求参数
    * @param pageParamRequest 分页类参数
    * @return List<StoreProductReply>
    */
    @Override
    public PageInfo<StoreProductReplyResponse> getAdminPage(ProductReplySearchRequest request, PageParamRequest pageParamRequest) {
        //带 StoreProductReply 类的多条件查询
        LambdaQueryWrapper<StoreProductReply> lqw = new LambdaQueryWrapper<>();
        Integer merId = 0;
        if (ObjectUtil.isNotNull(request.getMerId())) {
            merId = request.getMerId();
        }
        if (!adminPageGetWhere(lqw, request, merId)) {
            Page<StoreProductReply> pageStoreReply = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
            return CommonPage.copyPageInfo(pageStoreReply, CollUtil.newArrayList());
        }

        Page<StoreProductReply> pageStoreReply = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<StoreProductReply> dataList = dao.selectList(lqw);
        if (CollUtil.isEmpty(dataList)) {
            return CommonPage.copyPageInfo(pageStoreReply, new ArrayList<>());
        }
        List<StoreProductReplyResponse> dataResList = new ArrayList<>();
        for (StoreProductReply productReply : dataList) {
            StoreProductReplyResponse productReplyResponse = new StoreProductReplyResponse();
            BeanUtils.copyProperties(productReply, productReplyResponse);
            if (productReply.getOrderInfoId() > 0) {
                StoreOrderInfo orderInfo = storeOrderInfoService.getById(productReply.getOrderInfoId());
                productReplyResponse.setProductName(orderInfo.getProductName());
                productReplyResponse.setProductImage(orderInfo.getImage());
                Merchant merchant = merchantService.getById(orderInfo.getMerId());
                productReplyResponse.setMerName(merchant.getName());
            } else {
                StoreProduct product = storeProductService.getById(productReply.getProductId());
                productReplyResponse.setProductName(product.getStoreName());
                productReplyResponse.setProductImage(product.getImage());
                Merchant merchant = merchantService.getById(product.getMerId());
                productReplyResponse.setMerName(merchant.getName());
            }
            productReplyResponse.setPics(CrmebUtil.stringToArrayStr(productReply.getPics()));
            dataResList.add(productReplyResponse);
        }
        return CommonPage.copyPageInfo(pageStoreReply, dataResList);
    }

    /**
     * 商户端商品评论分页列表
     * @param request 请求参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<StoreProductReplyResponse> getMerchantAdminPage(ProductReplySearchRequest request, PageParamRequest pageParamRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        //带 StoreProductReply 类的多条件查询
        LambdaQueryWrapper<StoreProductReply> lqw = new LambdaQueryWrapper<>();
        if (!adminPageGetWhere(lqw, request, systemAdmin.getMerId())) {
            Page<StoreProductReply> pageStoreReply = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
            return CommonPage.copyPageInfo(pageStoreReply, CollUtil.newArrayList());
        }

        Page<StoreProductReply> pageStoreReply = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<StoreProductReply> dataList = dao.selectList(lqw);
        if (CollUtil.isEmpty(dataList)) {
            return CommonPage.copyPageInfo(pageStoreReply, new ArrayList<>());
        }
        List<StoreProductReplyResponse> dataResList = new ArrayList<>();
        for (StoreProductReply productReply : dataList) {
            StoreProductReplyResponse productReplyResponse = new StoreProductReplyResponse();
            BeanUtils.copyProperties(productReply, productReplyResponse);
            if (productReply.getOrderInfoId() > 0) {
                StoreOrderInfo orderInfo = storeOrderInfoService.getById(productReply.getOrderInfoId());
                productReplyResponse.setProductName(orderInfo.getProductName());
                productReplyResponse.setProductImage(orderInfo.getImage());
            } else {
                StoreProduct product = storeProductService.getById(productReply.getProductId());
                productReplyResponse.setProductName(product.getStoreName());
                productReplyResponse.setProductImage(product.getImage());
            }
            productReplyResponse.setPics(CrmebUtil.stringToArrayStr(productReply.getPics()));
            dataResList.add(productReplyResponse);
        }
        return CommonPage.copyPageInfo(pageStoreReply, dataResList);
    }

    /**
     * 后台分页Where条件
     * @param request 查询参数
     * @param merId 商户ID，0-平台
     */
    private Boolean adminPageGetWhere(LambdaQueryWrapper<StoreProductReply> lqw, ProductReplySearchRequest request, Integer merId) {
        if (!merId.equals(0)) {
            lqw.eq(StoreProductReply::getMerId, merId);
        }
        if (ObjectUtil.isNotNull(request.getStar())) {
            lqw.eq(StoreProductReply::getStar, request.getStar());
        }
        if (ObjectUtil.isNotNull(request.getIsReply())) {
            lqw.eq(StoreProductReply::getIsReply, request.getIsReply());
        }
        if (StrUtil.isNotBlank(request.getProductSearch())) {
            List<StoreProduct> storeProducts = storeProductService.likeProductName(request.getProductSearch(), merId);
            if (CollUtil.isNotEmpty(storeProducts)) {
                List<Integer> productIds = storeProducts.stream().map(StoreProduct::getId).collect(Collectors.toList());
                lqw.in(StoreProductReply::getProductId, productIds);
            } else {
                return Boolean.FALSE;
            }
        }
        if (StrUtil.isNotBlank(request.getNickname())) {
            lqw.like(StoreProductReply::getNickname,request.getNickname());
        }
        if (StrUtil.isNotBlank(request.getDateLimit())) {
            dateLimitUtilVo dateLimit = DateUtil.getDateLimit(request.getDateLimit());
            lqw.between(StoreProductReply::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        lqw.eq(StoreProductReply::getIsDel, false);
        lqw.orderByDesc(StoreProductReply::getId);
        return Boolean.TRUE;
    }

    /**
     * 商品星数
     * @return Integer
     */
    private Integer getSumStar(Integer productId) {
        QueryWrapper<StoreProductReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("IFNULL(sum(star),0) as star");
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("product_id", productId);
        StoreProductReply storeProductReply = dao.selectOne(queryWrapper);
        if (ObjectUtil.isNull(storeProductReply)) {
            return 0;
        }
        if (storeProductReply.getStar() == 0) {
            return 0;
        }
        return storeProductReply.getStar();
    }

    /**
     * 添加虚拟评论
     * @param request 评论参数
     * @return 评论结果
     */
    @Override
    public boolean virtualCreate(ProductReplyVirtualRequest request) {
        StoreProduct product = storeProductService.getById(request.getProductId());
        if (ObjectUtil.isNull(product) || product.getIsDel()) {
            throw new CrmebException("商品不存在");
        }
        StoreProductAttrValue attrValue = attrValueService.getById(request.getAttrValueId());
        if (ObjectUtil.isNull(attrValue) || attrValue.getIsDel()) {
            throw new CrmebException("商品规格属性值不存在");
        }
        StoreProductReply storeProductReply = new StoreProductReply();
        BeanUtils.copyProperties(request, storeProductReply);
        storeProductReply.setAvatar(systemAttachmentService.clearPrefix(storeProductReply.getAvatar()));
        if (StrUtil.isNotBlank(request.getPics())) {
            String pics = request.getPics()
                    .replace("[","")
                    .replace("]","")
                    .replace("\"","");
            storeProductReply.setPics(systemAttachmentService.clearPrefix(ArrayUtils.toString(pics)));
        }
        storeProductReply.setUid(0);
        storeProductReply.setMerId(product.getMerId());
        storeProductReply.setSku(attrValue.getSku());
        return save(storeProductReply);
    }

    /**
     * H5商品评论统计
     * @param productId 商品编号
     * @return MyRecord
     */
    @Override
    public MyRecord getH5Count(Integer productId) {
        // 评论总数
        Integer sumCount = getCountByScore(productId, ProductConstants.PRODUCT_REPLY_TYPE_ALL);
        // 好评总数
        Integer goodCount = getCountByScore(productId, ProductConstants.PRODUCT_REPLY_TYPE_GOOD);
        // 中评总数
        Integer mediumCount = getCountByScore(productId, ProductConstants.PRODUCT_REPLY_TYPE_MEDIUM);
        // 差评总数
        Integer poorCount = getCountByScore(productId, ProductConstants.PRODUCT_REPLY_TYPE_POOR);
        // 好评率
        String replyChance = "0";
        if (sumCount > 0 && goodCount > 0) {
            replyChance = String.format("%.2f", ((goodCount.doubleValue() / sumCount.doubleValue())));
        }
        // 评分星数 = 总星数/评价数
        Integer replyStar = 0;
        if (sumCount > 0) {
            replyStar = getSumStar(productId);
            BigDecimal divide = new BigDecimal(replyStar).divide(BigDecimal.valueOf(sumCount), 0, BigDecimal.ROUND_DOWN);
            replyStar = divide.intValue();
        }
        MyRecord record = new MyRecord();
        record.set("sumCount", sumCount);
        record.set("goodCount", goodCount);
        record.set("mediumCount", mediumCount);
        record.set("poorCount", poorCount);
        record.set("replyChance", replyChance);
        record.set("replyStar", replyStar);
        return record;
    }

    /**
     * H5商品详情评论信息
     * @param proId 商品编号
     * @return ProductDetailReplyResponse
     */
    @Override
    public ProductDetailReplyResponse getH5ProductReply(Integer proId) {
        ProductDetailReplyResponse response = new ProductDetailReplyResponse();

        // 评论总数
        Integer sumCount = getCountByScore(proId, ProductConstants.PRODUCT_REPLY_TYPE_ALL);
        if (sumCount.equals(0)) {
            response.setSumCount(0);
            response.setReplyChance("0");
            return response;
        }
        // 好评总数
        Integer goodCount = getCountByScore(proId, ProductConstants.PRODUCT_REPLY_TYPE_GOOD);
        // 好评率
        String replyChance = "0";
        if (sumCount > 0 && goodCount > 0) {
            replyChance = String.format("%.2f", ((goodCount.doubleValue() / sumCount.doubleValue())));
        }

        // 查询最后一条评论
        LambdaQueryWrapper<StoreProductReply> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StoreProductReply::getProductId, proId);
        lqw.eq(StoreProductReply::getIsDel, false);
        lqw.orderByDesc(StoreProductReply::getId);
        lqw.last(" limit 1");
        StoreProductReply storeProductReply = dao.selectOne(lqw);
        ProductReplyResponse productReplyResponse = new ProductReplyResponse();
        BeanUtils.copyProperties(storeProductReply, productReplyResponse);
        // 评价图
        productReplyResponse.setPics(CrmebUtil.stringToArrayStr(storeProductReply.getPics()));
        // 昵称
        String nickname = storeProductReply.getNickname();
        if (StrUtil.isNotBlank(nickname)) {
            if (nickname.length() == 1) {
                nickname = nickname.concat("**");
            } else if (nickname.length() == 2) {
                nickname = nickname.substring(0, 1) + "**";
            } else {
                nickname = nickname.substring(0, 1) + "**" + nickname.substring(nickname.length() - 1);
            }
            productReplyResponse.setNickname(nickname);
        }

        response.setSumCount(sumCount);
        response.setReplyChance(replyChance);
        response.setProductReply(productReplyResponse);
        return response;
    }

    /**
     * 移动端商品评论列表
     * @param proId 商品编号
     * @param type 评价等级|0=全部,1=好评,2=中评,3=差评
     * @param pageParamRequest 分页参数
     * @return PageInfo<ProductReplyResponse>
     */
    @Override
    public PageInfo<ProductReplyResponse> getH5List(Integer proId, Integer type, PageParamRequest pageParamRequest) {
        Page<Object> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        //带 StoreProductReply 类的多条件查询
        LambdaQueryWrapper<StoreProductReply> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StoreProductReply::getIsDel, false);
        lqw.eq(StoreProductReply::getProductId, proId);
        //评价等级|0=全部,1=好评,2=中评,3=差评
        switch (type) {
            case 1:
                lqw.eq(StoreProductReply::getStar, 5);
                break;
            case 2:
                lqw.apply(" star < 5 and star > 1");
                break;
            case 3:
                lqw.eq(StoreProductReply::getStar, 1);
                break;
            default:
                break;

        }
        lqw.orderByDesc(StoreProductReply::getId);
        List<StoreProductReply> replyList = dao.selectList(lqw);
        List<ProductReplyResponse> responseList = new ArrayList<>();
        for (StoreProductReply productReply : replyList) {
            ProductReplyResponse productReplyResponse = new ProductReplyResponse();
            BeanUtils.copyProperties(productReply, productReplyResponse);
            // 评价图
            productReplyResponse.setPics(CrmebUtil.stringToArrayStr(productReply.getPics()));
            // 昵称
            String nickname = productReply.getNickname();
            if (StrUtil.isNotBlank(nickname)) {
                if (nickname.length() == 1) {
                    nickname = nickname.concat("**");
                } else if (nickname.length() == 2) {
                    nickname = nickname.substring(0, 1) + "**";
                } else {
                    nickname = nickname.substring(0, 1) + "**" + nickname.substring(nickname.length() - 1);
                }
                productReplyResponse.setNickname(nickname);
            }
            responseList.add(productReplyResponse);
        }
        return CommonPage.copyPageInfo(page, responseList);
    }

    /**
     * 删除评论
     * @param id 评论id
     * @return Boolean
     */
    @Override
    public Boolean delete(Integer id) {
        LambdaUpdateWrapper<StoreProductReply> lmdUp = new LambdaUpdateWrapper<>();
        lmdUp.set(StoreProductReply::getIsDel, 1);
        lmdUp.eq(StoreProductReply::getId, id);
        return update(lmdUp);
    }

    /**
     * 商品评论回复
     * @param request 回复参数
     */
    @Override
    public Boolean comment(StoreProductReplyCommentRequest request) {
        StoreProductReply reply = getById(request.getId());
        if (ObjectUtil.isNull(reply) || reply.getIsDel()) {
            throw new CrmebException("评论不存在");
        }
        LambdaUpdateWrapper<StoreProductReply> lup = new LambdaUpdateWrapper<>();
        lup.eq(StoreProductReply::getId, request.getId());
        lup.set(StoreProductReply::getMerchantReplyContent, request.getMerchantReplyContent());
        lup.set(StoreProductReply::getMerchantReplyTime, cn.hutool.core.date.DateUtil.date());
        lup.set(StoreProductReply::getIsReply, true);
        return update(lup);
    }

    // 获取统计数据（好评、中评、差评）
    private Integer getCountByScore(Integer productId, String type) {
        LambdaQueryWrapper<StoreProductReply> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StoreProductReply::getProductId, productId);
        lqw.eq(StoreProductReply::getIsDel, false);

        switch (type) {
            case ProductConstants.PRODUCT_REPLY_TYPE_ALL:
                break;
            case ProductConstants.PRODUCT_REPLY_TYPE_GOOD:
                lqw.eq(StoreProductReply::getStar, 5);
                break;
            case ProductConstants.PRODUCT_REPLY_TYPE_MEDIUM:
                lqw.and(i -> i.lt(StoreProductReply::getStar, 5).gt(StoreProductReply::getStar, 1));
                break;
            case ProductConstants.PRODUCT_REPLY_TYPE_POOR:
                lqw.eq(StoreProductReply::getStar, 1);
                break;
        }
        return dao.selectCount(lqw);
    }

}

