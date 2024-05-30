package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.ProductConstants;
import com.zbkj.common.constants.SystemRoleConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.merchant.Merchant;
import com.zbkj.common.model.merchant.MerchantInfo;
import com.zbkj.common.model.product.*;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.*;
import com.zbkj.common.response.*;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.LoginUserVo;
import com.zbkj.common.vo.SimpleProductVo;
import com.zbkj.service.dao.StoreProductDao;
import com.zbkj.service.delete.ProductUtils;
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
import java.util.stream.Stream;

/**
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
public class StoreProductServiceImpl extends ServiceImpl<StoreProductDao, StoreProduct>
        implements StoreProductService {

    private Logger LOGGER = LoggerFactory.getLogger(StoreProductServiceImpl.class);

    @Resource
    private StoreProductDao dao;

    @Autowired
    private StoreProductAttrService attrService;
    @Autowired
    private StoreProductAttrValueService storeProductAttrValueService;
    @Autowired
    private StoreProductDescriptionService storeProductDescriptionService;
    @Autowired
    private StoreProductRelationService storeProductRelationService;
    @Autowired
    private SystemAttachmentService systemAttachmentService;
    @Autowired
    private StoreProductCouponService storeProductCouponService;
    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private StoreCartService storeCartService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private StoreCouponService storeCouponService;

    /**
     * 获取产品列表Admin
     *
     * @param request          筛选参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<AdminProductListResponse> getAdminList(StoreProductSearchRequest request, PageParamRequest pageParamRequest) {
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        //带 StoreProduct 类的多条件查询
        LambdaQueryWrapper<StoreProduct> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StoreProduct::getMerId, admin.getMerId());
        //类型搜索
        switch (request.getType()) {
            case 1:
                //出售中（已上架）
                lqw.eq(StoreProduct::getIsForced, false);
                lqw.eq(StoreProduct::getIsShow, true);
                lqw.eq(StoreProduct::getIsRecycle, false);
                lqw.eq(StoreProduct::getIsDel, false);
                lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                break;
            case 2:
                //仓库中（未上架）
                lqw.eq(StoreProduct::getIsShow, false);
                lqw.eq(StoreProduct::getIsRecycle, false);
                lqw.eq(StoreProduct::getIsDel, false);
                lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                break;
            case 3:
                //已售罄
                lqw.le(StoreProduct::getStock, 0);
                lqw.eq(StoreProduct::getIsRecycle, false);
                lqw.eq(StoreProduct::getIsDel, false);
                lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                break;
            case 4:
                //警戒库存
                MerchantInfo merchantInfo = merchantInfoService.getByMerId(admin.getMerId());
                lqw.le(StoreProduct::getStock, merchantInfo.getAlertStock());
                lqw.eq(StoreProduct::getIsRecycle, false);
                lqw.eq(StoreProduct::getIsDel, false);
                lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                break;
            case 5:
                //回收站
                lqw.eq(StoreProduct::getIsRecycle, true);
                lqw.eq(StoreProduct::getIsDel, false);
                break;
            case 6:
                //待审核
                lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_WAIT);
                lqw.eq(StoreProduct::getIsRecycle, false);
                lqw.eq(StoreProduct::getIsDel, false);
                break;
            case 7:
                //审核失败
                lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_FAIL);
                lqw.eq(StoreProduct::getIsRecycle, false);
                lqw.eq(StoreProduct::getIsDel, false);
                break;
            default:
                break;
        }
        //关键字搜索
        if (StrUtil.isNotBlank(request.getKeywords())) {
            lqw.and(i -> i.like(StoreProduct::getStoreName, request.getKeywords())
                    .or().like(StoreProduct::getKeyword, request.getKeywords()));
        }
        lqw.apply(StrUtil.isNotBlank(request.getCateId()), "FIND_IN_SET ('" + request.getCateId() + "', cate_id)");
        if (ObjectUtil.isNotNull(request.getCategoryId())) {
            lqw.eq(StoreProduct::getCategoryId, request.getCategoryId());
        }
        lqw.orderByDesc(StoreProduct::getSort).orderByDesc(StoreProduct::getId);
        Page<StoreProduct> storeProductPage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<StoreProduct> storeProducts = dao.selectList(lqw);
        if (CollUtil.isEmpty(storeProducts)) {
            return CommonPage.copyPageInfo(storeProductPage, CollUtil.newArrayList());
        }
        List<AdminProductListResponse> storeProductResponses = new ArrayList<>();
        for (StoreProduct product : storeProducts) {
            AdminProductListResponse storeProductResponse = new AdminProductListResponse();
            BeanUtils.copyProperties(product, storeProductResponse);
            // 收藏数
            storeProductResponse.setCollectCount(storeProductRelationService.getCollectCountByProductId(product.getId()));
            storeProductResponses.add(storeProductResponse);
        }
        // 多条sql查询处理分页正确
        return CommonPage.copyPageInfo(storeProductPage, storeProductResponses);
    }

    /**
     * 根据id集合获取商品简单信息
     *
     * @param productIds id集合
     * @return 商品信息
     */
    @Override
    public List<SimpleProductVo> getSimpleListInIds(List<Integer> productIds) {
        LambdaQueryWrapper<StoreProduct> lqw = new LambdaQueryWrapper<>();
        lqw.select(StoreProduct::getId, StoreProduct::getStoreName, StoreProduct::getImage);
        lqw.in(StoreProduct::getId, productIds);
        lqw.eq(StoreProduct::getIsDel, false);
        List<StoreProduct> selectList = dao.selectList(lqw);
        List<SimpleProductVo> voList = selectList.stream().map(e -> {
            SimpleProductVo vo = new SimpleProductVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

    /**
     * 新增产品
     *
     * @param request 新增产品request对象
     * @return 新增结果
     */
    @Override
    public Boolean save(StoreProductAddRequest request) {
        // 多规格需要校验规格参数
        if (!request.getSpecType()) {
            if (request.getAttrValue().size() > 1) {
                throw new CrmebException("单规格商品属性值不能大于1");
            }
        }
        LoginUserVo loginUserVo = SecurityUtil.getLoginUserVo();
        if (loginUserVo.getUser().getType().equals(SystemRoleConstants.SYSTEM_ROLE_TYPE_PLATFORM)) {
            throw new CrmebException("只有商户管理员才能创建商品");
        }
        Merchant merchant = merchantService.getByIdException(loginUserVo.getUser().getMerId());

        StoreProduct storeProduct = new StoreProduct();
        BeanUtils.copyProperties(request, storeProduct);
        storeProduct.setId(null);
        storeProduct.setMerId(loginUserVo.getUser().getMerId());

        String cdnUrl = systemAttachmentService.getCdnUrl();
        //主图
        storeProduct.setImage(systemAttachmentService.clearPrefix(storeProduct.getImage(), cdnUrl));
        //轮播图
        storeProduct.setSliderImage(systemAttachmentService.clearPrefix(storeProduct.getSliderImage(), cdnUrl));
        // 展示图
        if (StrUtil.isNotEmpty(storeProduct.getFlatPattern())) {
            storeProduct.setFlatPattern(systemAttachmentService.clearPrefix(storeProduct.getFlatPattern(), cdnUrl));
        }

        List<StoreProductAttrValueAddRequest> attrValueAddRequestList = request.getAttrValue();
        //计算价格
        StoreProductAttrValueAddRequest minAttrValue = attrValueAddRequestList.stream().min(Comparator.comparing(StoreProductAttrValueAddRequest::getPrice)).get();
        storeProduct.setPrice(minAttrValue.getPrice());
        storeProduct.setOtPrice(minAttrValue.getOtPrice());
        storeProduct.setCost(minAttrValue.getCost());
        storeProduct.setStock(attrValueAddRequestList.stream().mapToInt(StoreProductAttrValueAddRequest::getStock).sum());

        List<StoreProductAttrAddRequest> addRequestList = request.getAttr();
        List<StoreProductAttr> attrList = addRequestList.stream().map(e -> {
            StoreProductAttr attr = new StoreProductAttr();
            BeanUtils.copyProperties(e, attr);
            attr.setType(ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
            return attr;
        }).collect(Collectors.toList());

        List<StoreProductAttrValue> attrValueList = attrValueAddRequestList.stream().map(e -> {
            StoreProductAttrValue attrValue = new StoreProductAttrValue();
            BeanUtils.copyProperties(e, attrValue);
            attrValue.setId(null);
            attrValue.setSku(getSku(e.getAttrValue()));
            attrValue.setQuota(0);
            attrValue.setQuotaShow(0);
            attrValue.setType(ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
            attrValue.setImage(systemAttachmentService.clearPrefix(e.getImage(), cdnUrl));
            return attrValue;
        }).collect(Collectors.toList());

        // 处理富文本
        StoreProductDescription spd = new StoreProductDescription();
        spd.setDescription(request.getContent().length() > 0 ? systemAttachmentService.clearPrefix(request.getContent(), cdnUrl) : "");
        spd.setType(ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);

        Boolean execute = transactionTemplate.execute(e -> {
            if (merchant.getProductSwitch()) {
                storeProduct.setAuditStatus(ProductConstants.AUDIT_STATUS_WAIT);
            }
            save(storeProduct);

            attrList.forEach(attr -> attr.setProductId(storeProduct.getId()));
            attrValueList.forEach(value -> value.setProductId(storeProduct.getId()));
            attrService.saveBatch(attrList);
            storeProductAttrValueService.saveBatch(attrValueList);

            spd.setProductId(storeProduct.getId());
            storeProductDescriptionService.deleteByProductId(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
            storeProductDescriptionService.save(spd);

            if (CollUtil.isNotEmpty(request.getCouponIds())) {
                List<StoreProductCoupon> couponList = new ArrayList<>();
                for (Integer couponId : request.getCouponIds()) {
                    StoreProductCoupon spc = new StoreProductCoupon(storeProduct.getId(), couponId, DateUtil.getNowTime());
                    couponList.add(spc);
                }
                storeProductCouponService.saveBatch(couponList);
            }
            return Boolean.TRUE;
        });

        return execute;
    }

    /**
     * 商品sku
     *
     * @param attrValue json字符串
     * @return sku
     */
    private String getSku(String attrValue) {
        LinkedHashMap<String, String> linkedHashMap = JSONObject.parseObject(attrValue, LinkedHashMap.class, Feature.OrderedField);
        Iterator<Map.Entry<String, String>> iterator = linkedHashMap.entrySet().iterator();
        List<String> strings = CollUtil.newArrayList();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            strings.add(next.getValue());
        }
//        List<String> strings = jsonObject.values().stream().map(o -> (String) o).collect(Collectors.toList());
        return String.join(",", strings);
    }

    /**
     * 更新商品信息
     *
     * @param storeProductRequest 商品参数
     * @return 更新结果
     */
    @Override
    public Boolean update(StoreProductAddRequest storeProductRequest) {
        if (ObjectUtil.isNull(storeProductRequest.getId())) {
            throw new CrmebException("商品ID不能为空");
        }

        if (!storeProductRequest.getSpecType()) {
            if (storeProductRequest.getAttrValue().size() > 1) {
                throw new CrmebException("单规格商品属性值不能大于1");
            }
        }

        StoreProduct tempProduct = getById(storeProductRequest.getId());
        if (ObjectUtil.isNull(tempProduct)) {
            throw new CrmebException("商品不存在");
        }
        if (tempProduct.getIsRecycle() || tempProduct.getIsDel()) {
            throw new CrmebException("商品已删除");
        }
        if (tempProduct.getIsShow()) {
            throw new CrmebException("请先下架商品，再进行修改");
        }

        StoreProduct storeProduct = new StoreProduct();
        BeanUtils.copyProperties(storeProductRequest, storeProduct);
        storeProduct.setIsForced(tempProduct.getIsForced());
        storeProduct.setAuditStatus(tempProduct.getAuditStatus());
        Merchant merchant = merchantService.getByIdException(tempProduct.getMerId());

        String cdnUrl = systemAttachmentService.getCdnUrl();
        //主图
        storeProduct.setImage(systemAttachmentService.clearPrefix(storeProduct.getImage(), cdnUrl));
        //轮播图
        storeProduct.setSliderImage(systemAttachmentService.clearPrefix(storeProduct.getSliderImage(), cdnUrl));

        List<StoreProductAttrValueAddRequest> attrValueAddRequestList = storeProductRequest.getAttrValue();
        //计算价格
        StoreProductAttrValueAddRequest minAttrValue = attrValueAddRequestList.stream().min(Comparator.comparing(StoreProductAttrValueAddRequest::getPrice)).get();
        storeProduct.setPrice(minAttrValue.getPrice());
        storeProduct.setOtPrice(minAttrValue.getOtPrice());
        storeProduct.setCost(minAttrValue.getCost());
        storeProduct.setStock(attrValueAddRequestList.stream().mapToInt(StoreProductAttrValueAddRequest::getStock).sum());

        // attr部分
        List<StoreProductAttrAddRequest> addRequestList = storeProductRequest.getAttr();
        List<StoreProductAttr> attrAddList = CollUtil.newArrayList();
        List<StoreProductAttr> attrUpdateList = CollUtil.newArrayList();
        addRequestList.forEach(e -> {
            StoreProductAttr attr = new StoreProductAttr();
            BeanUtils.copyProperties(e, attr);
            if (ObjectUtil.isNull(attr.getId())) {
                attr.setProductId(storeProduct.getId());
                attr.setType(ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
                attrAddList.add(attr);
            } else {
                attr.setIsDel(false);
                attrUpdateList.add(attr);
            }
        });

        // attrValue部分
        List<StoreProductAttrValue> attrValueAddList = CollUtil.newArrayList();
        List<StoreProductAttrValue> attrValueUpdateList = CollUtil.newArrayList();
        attrValueAddRequestList.forEach(e -> {
            StoreProductAttrValue attrValue = new StoreProductAttrValue();
            BeanUtils.copyProperties(e, attrValue);
            attrValue.setSku(getSku(e.getAttrValue()));
            attrValue.setImage(systemAttachmentService.clearPrefix(e.getImage(), cdnUrl));
            if (ObjectUtil.isNull(attrValue.getId()) || attrValue.getId().equals(0)) {
                attrValue.setId(null);
                attrValue.setProductId(storeProduct.getId());
                attrValue.setQuota(0);
                attrValue.setQuotaShow(0);
                attrValue.setType(ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
                attrValueAddList.add(attrValue);
            } else {
                attrValue.setProductId(storeProduct.getId());
                attrValue.setIsDel(false);
                attrValueUpdateList.add(attrValue);
            }
        });

        // 处理富文本
        StoreProductDescription spd = new StoreProductDescription();
        spd.setDescription(storeProductRequest.getContent().length() > 0 ? systemAttachmentService.clearPrefix(storeProductRequest.getContent(), cdnUrl) : "");
        spd.setType(ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        spd.setProductId(storeProduct.getId());

        Boolean execute = transactionTemplate.execute(e -> {
            if (merchant.getProductSwitch() || storeProduct.getIsForced() || storeProduct.getAuditStatus().equals(ProductConstants.AUDIT_STATUS_FAIL)) {
                storeProduct.setAuditStatus(ProductConstants.AUDIT_STATUS_WAIT);
            }
            if (storeProduct.getIsForced()) {
                storeProduct.setIsForced(false);
            }
            dao.updateById(storeProduct);

            // 先删除原用attr+value
            attrService.deleteByProductIdAndType(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
            storeProductAttrValueService.deleteByProductIdAndType(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);

            if (CollUtil.isNotEmpty(attrAddList)) {
                attrService.saveBatch(attrAddList);
            }
            if (CollUtil.isNotEmpty(attrUpdateList)) {
                attrService.saveOrUpdateBatch(attrUpdateList);
            }

            if (CollUtil.isNotEmpty(attrValueAddList)) {
                storeProductAttrValueService.saveBatch(attrValueAddList);
            }
            if (CollUtil.isNotEmpty(attrValueUpdateList)) {
                storeProductAttrValueService.saveOrUpdateBatch(attrValueUpdateList);
            }

            storeProductDescriptionService.deleteByProductId(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
            storeProductDescriptionService.save(spd);

            if (CollUtil.isNotEmpty(storeProductRequest.getCouponIds())) {
                storeProductCouponService.deleteByProductId(storeProduct.getId());
                List<StoreProductCoupon> couponList = new ArrayList<>();
                for (Integer couponId : storeProductRequest.getCouponIds()) {
                    StoreProductCoupon spc = new StoreProductCoupon(storeProduct.getId(), couponId, DateUtil.getNowTime());
                    couponList.add(spc);
                }
                storeProductCouponService.saveBatch(couponList);
            } else {
                storeProductCouponService.deleteByProductId(storeProduct.getId());
            }

            return Boolean.TRUE;
        });

        return execute;
    }

    /**
     * 商品详情（管理端）
     *
     * @param id 商品id
     * @return StoreProductInfoResponse
     */
    @Override
    public StoreProductInfoResponse getInfo(Integer id) {
        StoreProduct storeProduct = dao.selectById(id);
        if (ObjectUtil.isNull(storeProduct)) {
            throw new CrmebException("未找到对应商品信息");
        }

        StoreProductInfoResponse storeProductResponse = new StoreProductInfoResponse();
        BeanUtils.copyProperties(storeProduct, storeProductResponse);

        List<StoreProductAttr> attrList = attrService.getListByProductIdAndType(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        storeProductResponse.setAttr(attrList);

        List<StoreProductAttrValue> attrValueList = storeProductAttrValueService.getListByProductIdAndType(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        List<AttrValueResponse> valueResponseList = attrValueList.stream().map(e -> {
            AttrValueResponse valueResponse = new AttrValueResponse();
            BeanUtils.copyProperties(e, valueResponse);
            return valueResponse;
        }).collect(Collectors.toList());
        storeProductResponse.setAttrValue(valueResponseList);

        StoreProductDescription sd = storeProductDescriptionService.getByProductIdAndType(storeProduct.getId(), ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        if (ObjectUtil.isNotNull(sd)) {
            storeProductResponse.setContent(ObjectUtil.isNull(sd.getDescription()) ? "" : sd.getDescription());
        }

        // 获取已关联的优惠券
        List<StoreProductCoupon> storeProductCoupons = storeProductCouponService.getListByProductId(storeProduct.getId());
        if (CollUtil.isNotEmpty(storeProductCoupons)) {
            List<Integer> ids = storeProductCoupons.stream().map(StoreProductCoupon::getIssueCouponId).collect(Collectors.toList());
            SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
            if (systemAdmin.getMerId() > 0) {
                storeProductResponse.setCouponIds(ids);
            } else {
                storeProductResponse.setCouponList(storeCouponService.findSimpleListByIdList(ids));
            }
        }
        return storeProductResponse;
    }

    /**
     * 根据商品tabs获取对应类型的产品数量
     *
     * @return List
     */
    @Override
    public List<StoreProductTabsHeader> getTabsHeader() {
        List<StoreProductTabsHeader> headers = new ArrayList<>();
        StoreProductTabsHeader header1 = new StoreProductTabsHeader(0, 1);
        StoreProductTabsHeader header2 = new StoreProductTabsHeader(0, 2);
        StoreProductTabsHeader header3 = new StoreProductTabsHeader(0, 3);
        StoreProductTabsHeader header4 = new StoreProductTabsHeader(0, 4);
        StoreProductTabsHeader header5 = new StoreProductTabsHeader(0, 5);
        StoreProductTabsHeader header6 = new StoreProductTabsHeader(0, 6);
        StoreProductTabsHeader header7 = new StoreProductTabsHeader(0, 7);
        headers.add(header1);
        headers.add(header2);
        headers.add(header3);
        headers.add(header4);
        headers.add(header5);
        headers.add(header6);
        headers.add(header7);
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();

        LambdaQueryWrapper<StoreProduct> lqw = new LambdaQueryWrapper<>();
        for (StoreProductTabsHeader h : headers) {
            lqw.clear();
            lqw.eq(StoreProduct::getMerId, systemAdmin.getMerId());
            switch (h.getType()) {
                case 1:
                    //出售中（已上架）
                    lqw.eq(StoreProduct::getIsForced, false);
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                    lqw.eq(StoreProduct::getIsShow, true);
                    lqw.eq(StoreProduct::getIsRecycle, false);
                    lqw.eq(StoreProduct::getIsDel, false);
                    break;
                case 2:
                    //仓库中（未上架）
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                    lqw.eq(StoreProduct::getIsShow, false);
                    lqw.eq(StoreProduct::getIsRecycle, false);
                    lqw.eq(StoreProduct::getIsDel, false);
                    break;
                case 3:
                    //已售罄
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                    lqw.le(StoreProduct::getStock, 0);
                    lqw.eq(StoreProduct::getIsRecycle, false);
                    lqw.eq(StoreProduct::getIsDel, false);
                    break;
                case 4:
                    //警戒库存
                    MerchantInfo merchantInfo = merchantInfoService.getByMerId(systemAdmin.getMerId());
                    lqw.le(StoreProduct::getStock, merchantInfo.getAlertStock());
                    lqw.eq(StoreProduct::getIsRecycle, false);
                    lqw.eq(StoreProduct::getIsDel, false);
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                    break;
                case 5:
                    //回收站
                    lqw.eq(StoreProduct::getIsRecycle, true);
                    lqw.eq(StoreProduct::getIsDel, false);
                    break;
                case 6:
                    //待审核
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_WAIT);
                    lqw.eq(StoreProduct::getIsRecycle, false);
                    lqw.eq(StoreProduct::getIsDel, false);
                    break;
                case 7:
                    //审核失败
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_FAIL);
                    lqw.eq(StoreProduct::getIsRecycle, false);
                    lqw.eq(StoreProduct::getIsDel, false);
                    break;
                default:
                    break;
            }
            List<StoreProduct> storeProducts = dao.selectList(lqw);
            h.setCount(storeProducts.size());
        }

        return headers;
    }

    /**
     * 根据其他平台url导入产品信息
     *
     * @param url 待导入平台url
     * @param tag 1=淘宝，2=京东，3=苏宁，4=拼多多， 5=天猫
     * @return StoreProductRequest
     */
    @Override
    public StoreProductRequest importProductFrom99Api(String url, int tag) {
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        Merchant merchant = merchantService.getByIdException(admin.getMerId());
        if (merchant.getCopyProductNum() <= 0) {
            throw new CrmebException("商户复制商品数量不足");
        }

        StoreProductRequest productRequest = null;
        try {
            switch (tag) {
                case 1:
                    productRequest = productUtils.getTaobaoProductInfo(url, tag);
                    break;
                case 2:
                    productRequest = productUtils.getJDProductInfo(url, tag);
                    break;
                case 3:
                    productRequest = productUtils.getSuningProductInfo(url, tag);
                    break;
                case 4:
                    productRequest = productUtils.getPddProductInfo(url, tag);
                    break;
                case 5:
                    productRequest = productUtils.getTmallProductInfo(url, tag);
                    break;
            }
        } catch (Exception e) {
            throw new CrmebException("确认URL和平台是否正确，以及平台费用是否足额" + e.getMessage());
        }
        Boolean sub = merchantService.subCopyProductNum(merchant.getId());
        if (!sub) {
            LOGGER.error("扣除商户复制条数异常：商户ID = {}", merchant.getId());
        }
        return productRequest;
    }

    /**
     * @param productId 商品id
     * @param type      类型：recycle——回收站 delete——彻底删除
     * @return Boolean
     */
    @Override
    public Boolean deleteProduct(Integer productId, String type) {
        StoreProduct product = getById(productId);
        if (ObjectUtil.isNull(product)) {
            throw new CrmebException("商品不存在");
        }
        if (ProductConstants.PRODUCT_DELETE_TYPE_RECYCLE.equals(type) && product.getIsRecycle()) {
            throw new CrmebException("商品已存在回收站");
        }

        LambdaUpdateWrapper<StoreProduct> wrapper = new LambdaUpdateWrapper<>();
        if (ProductConstants.PRODUCT_DELETE_TYPE_DELETE.equals(type)) {
            wrapper.eq(StoreProduct::getId, productId);
            wrapper.set(StoreProduct::getIsDel, true);
            return update(wrapper);
        }
        wrapper.eq(StoreProduct::getId, productId);
        wrapper.set(StoreProduct::getIsRecycle, true);
        return update(wrapper);
    }

    /**
     * 恢复已删除的商品
     *
     * @param productId 商品id
     * @return 恢复结果
     */
    @Override
    public Boolean reStoreProduct(Integer productId) {
        LambdaUpdateWrapper<StoreProduct> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(StoreProduct::getId, productId);
        wrapper.set(StoreProduct::getIsRecycle, false);
        wrapper.set(StoreProduct::getIsShow, false);
        return update(wrapper);
    }

    /**
     * 添加/扣减库存
     *
     * @param id   商品id
     * @param num  数量
     * @param type 类型：add—添加，sub—扣减
     */
    @Override
    public Boolean operationStock(Integer id, Integer num, String type) {
        UpdateWrapper<StoreProduct> updateWrapper = new UpdateWrapper<>();
        if (type.equals("add")) {
            updateWrapper.setSql(StrUtil.format("stock = stock + {}", num));
            updateWrapper.setSql(StrUtil.format("sales = sales - {}", num));
        }
        if (type.equals("sub")) {
            updateWrapper.setSql(StrUtil.format("stock = stock - {}", num));
            updateWrapper.setSql(StrUtil.format("sales = sales + {}", num));
            // 扣减时加乐观锁保证库存不为负
            updateWrapper.last(StrUtil.format(" and (stock - {} >= 0)", num));
        }
        updateWrapper.eq("id", id);
        boolean update = update(updateWrapper);
        if (!update) {
            throw new CrmebException("更新普通商品库存失败,商品id = " + id);
        }
        return update;
    }

    /**
     * 下架
     *
     * @param id 商品id
     */
    @Override
    public Boolean offShelf(Integer id) {
        StoreProduct storeProduct = getById(id);
        if (ObjectUtil.isNull(storeProduct)) {
            throw new CrmebException("商品不存在");
        }
        if (!storeProduct.getIsShow()) {
            return true;
        }

        storeProduct.setIsShow(false);
        Boolean execute = transactionTemplate.execute(e -> {
            dao.updateById(storeProduct);
            storeCartService.productStatusNotEnable(id);
            // 商品下架时，清除用户收藏
            storeProductRelationService.deleteByProId(storeProduct.getId());
            return Boolean.TRUE;
        });

        return execute;
    }

    /**
     * 上架
     *
     * @param id 商品id
     * @return Boolean
     */
    @Override
    public Boolean putOnShelf(Integer id) {
        StoreProduct storeProduct = getById(id);
        if (ObjectUtil.isNull(storeProduct)) {
            throw new CrmebException("商品不存在");
        }
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        Merchant merchant = merchantService.getByIdException(admin.getMerId());
        if (storeProduct.getIsForced() || merchant.getProductSwitch()) {
            storeProduct.setIsShow(false);
            storeProduct.setIsForced(false);
            storeProduct.setAuditStatus(ProductConstants.AUDIT_STATUS_WAIT);
            return updateById(storeProduct);
        }

        if (storeProduct.getIsShow()) {
            return true;
        }
        // 获取商品skuid
        List<StoreProductAttrValue> skuList = storeProductAttrValueService.getListByProductIdAndType(id, ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL);
        List<Integer> skuIdList = skuList.stream().map(StoreProductAttrValue::getId).collect(Collectors.toList());
        storeProduct.setIsShow(true);
        Boolean execute = transactionTemplate.execute(e -> {
            dao.updateById(storeProduct);
            storeCartService.productStatusNoEnable(skuIdList);
            return Boolean.TRUE;
        });
        return execute;
    }

    /**
     * 首页商品列表
     *
     * @param pageParamRequest 分页参数
     * @return CommonPage
     */
    @Override
    public PageInfo<StoreProduct> getIndexProduct(PageParamRequest pageParamRequest) {
        Page<StoreProduct> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId, StoreProduct::getImage, StoreProduct::getStoreName,
                StoreProduct::getPrice, StoreProduct::getOtPrice, StoreProduct::getSales, StoreProduct::getFicti);
        getForSaleWhere(lqw);
        lqw.gt(StoreProduct::getStock, 0);
        lqw.orderByDesc(StoreProduct::getRank);
        lqw.orderByDesc(StoreProduct::getId);
        List<StoreProduct> productList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, productList);
    }

    /**
     * 获取出售中商品的Where条件
     */
    private void getForSaleWhere(LambdaQueryWrapper<StoreProduct> lqw) {
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.eq(StoreProduct::getIsRecycle, false);
        lqw.eq(StoreProduct::getIsShow, true);
        lqw.eq(StoreProduct::getIsForced, false);
        lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
    }

    /**
     * 获取商品移动端列表
     *
     * @param request     筛选参数
     * @param pageRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<StoreProduct> findH5List(ProductRequest request, PageParamRequest pageRequest) {

        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        // id、名称、图片、价格、销量
        lqw.select(StoreProduct::getId, StoreProduct::getStoreName, StoreProduct::getImage, StoreProduct::getPrice,
                StoreProduct::getSales, StoreProduct::getFicti, StoreProduct::getUnitName, StoreProduct::getStock);

        getForSaleWhere(lqw);


        if (ObjectUtil.isNotNull(request.getCid()) && request.getCid() > 0) {
            lqw.eq(StoreProduct::getCategoryId, request.getCid());
        }

        if (StrUtil.isNotBlank(request.getKeyword())) {
            lqw.and(i -> i.like(StoreProduct::getStoreName, request.getKeyword())
                    .or().like(StoreProduct::getKeyword, request.getKeyword()));
        }

        if (ObjectUtil.isNotNull(request.getMaxPrice())) {
            lqw.le(StoreProduct::getPrice, request.getMaxPrice());
        }

        if (ObjectUtil.isNotNull(request.getMinPrice())) {
            lqw.ge(StoreProduct::getPrice, request.getMinPrice());
        }

        // 排序部分
        if (StrUtil.isNotBlank(request.getSalesOrder())) {
            if (request.getSalesOrder().equals(Constants.SORT_DESC)) {
                lqw.last(" order by (sales + ficti) desc, rank desc, sort desc, id desc");
            } else {
                lqw.last(" order by (sales + ficti) asc, rank desc, sort asc, id asc");
            }
        } else {
            if (StrUtil.isNotBlank(request.getPriceOrder())) {
                if (request.getPriceOrder().equals(Constants.SORT_DESC)) {
                    lqw.orderByDesc(StoreProduct::getPrice);
                } else {
                    lqw.orderByAsc(StoreProduct::getPrice);
                }
            }

            lqw.orderByDesc(StoreProduct::getRank);
//            lqw.orderByDesc(StoreProduct::getSort);
            lqw.orderByDesc(StoreProduct::getId);
        }
        Page<StoreProduct> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit());
        List<StoreProduct> productList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, productList);
    }

    /**
     * 获取移动端商品详情
     *
     * @param id 商品id
     * @return StoreProduct
     */
    @Override
    public StoreProduct getH5Detail(Integer id) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId, StoreProduct::getMerId, StoreProduct::getImage, StoreProduct::getStoreName, StoreProduct::getSliderImage,
                StoreProduct::getOtPrice, StoreProduct::getStock, StoreProduct::getSales, StoreProduct::getPrice,
                StoreProduct::getFicti, StoreProduct::getStoreInfo, StoreProduct::getBrowse, StoreProduct::getUnitName, StoreProduct::getGuaranteeIds);
        lqw.eq(StoreProduct::getId, id);
        lqw.eq(StoreProduct::getIsRecycle, false);
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.eq(StoreProduct::getIsShow, true);
        lqw.eq(StoreProduct::getIsForced, false);
        lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
        StoreProduct storeProduct = dao.selectOne(lqw);
        if (ObjectUtil.isNull(storeProduct)) {
            throw new CrmebException(StrUtil.format("Item with id {} not found", id));
        }

        StoreProductDescription sd = storeProductDescriptionService.getOne(
                new LambdaQueryWrapper<StoreProductDescription>()
                        .eq(StoreProductDescription::getProductId, storeProduct.getId())
                        .eq(StoreProductDescription::getType, ProductConstants.PRODUCT_ACTIVITY_TYPE_NORMAL));
        if (ObjectUtil.isNotNull(sd)) {
            storeProduct.setContent(StrUtil.isBlank(sd.getDescription()) ? "" : sd.getDescription());
        }
        return storeProduct;
    }

    /**
     * 获取购物车商品信息
     *
     * @param productId 商品编号
     * @return StoreProduct
     */
    @Override
    public StoreProduct getCartByProId(Integer productId) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId, StoreProduct::getImage, StoreProduct::getStoreName);
        lqw.eq(StoreProduct::getId, productId);
        return dao.selectOne(lqw);
    }

    /**
     * 根据日期获取新增商品数量
     *
     * @param date 日期，yyyy-MM-dd格式
     * @return Integer
     */
    @Override
    public Integer getNewProductByDate(String date) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId);
        lqw.eq(StoreProduct::getIsDel, 0);
        lqw.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        return dao.selectCount(lqw);
    }

    /**
     * 获取所有未删除的商品
     *
     * @return List<StoreProduct>
     */
    @Override
    public List<StoreProduct> findAllProductByNotDelte() {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId);
        lqw.eq(StoreProduct::getIsDel, 0);
        return dao.selectList(lqw);
    }

    /**
     * 模糊搜索商品名称
     *
     * @param productName 商品名称
     * @param merId       商户Id
     * @return List
     */
    @Override
    public List<StoreProduct> likeProductName(String productName, Integer merId) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId);
        lqw.like(StoreProduct::getStoreName, productName);
        lqw.eq(StoreProduct::getIsDel, 0);
        if (!merId.equals(0)) {
            lqw.eq(StoreProduct::getMerId, merId);
        }
        return dao.selectList(lqw);
    }

    /**
     * 警戒库存数量
     *
     * @return Integer
     */
    @Override
    public Integer getVigilanceInventoryNum(Integer merId) {
        SystemAdmin admin = SecurityUtil.getLoginUserVo().getUser();
        MerchantInfo merchantInfo = merchantInfoService.getByMerId(admin.getMerId());
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.le(StoreProduct::getStock, merchantInfo.getAlertStock());
        if (merId > 0) {
            lqw.eq(StoreProduct::getMerId, merId);
        }
        lqw.eq(StoreProduct::getIsRecycle, false);
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
        return dao.selectCount(lqw);
    }

    /**
     * 销售中（上架）商品数量
     *
     * @return Integer
     */
    @Override
    public Integer getOnSaleNum(Integer merId) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        if (merId > 0) {
            lqw.eq(StoreProduct::getMerId, merId);
        }
        getForSaleWhere(lqw);
        return dao.selectCount(lqw);
    }

    /**
     * 未销售（仓库）商品数量
     *
     * @return Integer
     */
    @Override
    public Integer getNotSaleNum(Integer merId) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreProduct::getIsShow, false);
        if (merId > 0) {
            lqw.eq(StoreProduct::getMerId, merId);
        }
        lqw.eq(StoreProduct::getIsRecycle, false);
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
        return dao.selectCount(lqw);
    }

    /**
     * 获取商品排行榜
     * 2.   TOP20
     *
     * @return List
     */
    @Override
    public List<StoreProduct> getLeaderboard() {
        QueryWrapper<StoreProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "store_name", "image", "price", "ot_price", "(sales + ficti) as sales");
        queryWrapper.eq("is_show", true);
        queryWrapper.eq("is_recycle", false);
        queryWrapper.eq("is_del", false);
        queryWrapper.eq("is_forced", false);
        queryWrapper.eq("audit_status", ProductConstants.AUDIT_STATUS_SUCCESS);
        queryWrapper.orderByDesc("sales");
        queryWrapper.last("limit 20");
        return dao.selectList(queryWrapper);
    }

    /**
     * 强制下架商户所有商品
     *
     * @param merchantId 商户ID
     * @return Boolean
     */
    @Override
    public Boolean forcedRemovalAll(Integer merchantId) {
        UpdateWrapper<StoreProduct> wrapper = Wrappers.update();
        wrapper.set("is_show", 0);
        wrapper.set("is_forced", 1);
//        wrapper.set("audit_status", ProductConstants.AUDIT_STATUS_WAIT);
        wrapper.eq("mer_id", merchantId);
        wrapper.ne("audit_status", ProductConstants.AUDIT_STATUS_FAIL);
        boolean update = update(wrapper);
        if (!update) {
            return update;
        }
        LambdaQueryWrapper<StoreProduct> query = Wrappers.lambdaQuery();
        query.select(StoreProduct::getId);
        query.eq(StoreProduct::getMerId, merchantId);
        query.eq(StoreProduct::getIsDel, false);
        List<StoreProduct> productList = dao.selectList(query);
        productList.forEach(product -> {
            storeCartService.productStatusNotEnable(product.getId());
            // 商品下架时，清除用户收藏
            storeProductRelationService.deleteByProId(product.getId());
        });
        return true;
    }

    /**
     * 平台端商品分页列表
     *
     * @param request          查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<PlatformProductListResponse> getPlatformPageList(ProductSearchRequest request, PageParamRequest pageParamRequest) {
        HashMap<String, Object> map = CollUtil.newHashMap();
        map.put("type", request.getType());
        if (ObjectUtil.isNotNull(request.getCategoryId())) {
            ProductCategory category = productCategoryService.getById(request.getCategoryId());
            if (category.getLevel().equals(3)) {
                map.put("categoryIds", request.getCategoryId());
            } else {
                List<ProductCategory> categoryList = productCategoryService.findAllChildListByPid(category.getId(), category.getLevel());
                List<String> cateIdList = categoryList.stream().filter(e -> e.getLevel().equals(3)).map(e -> e.getId().toString()).collect(Collectors.toList());
                String categoryIds = String.join(",", cateIdList);
                map.put("categoryIds", categoryIds);
            }
        }
        if (ObjectUtil.isNotNull(request.getMerId())) {
            map.put("merId", request.getMerId());
        }
        if (ObjectUtil.isNotNull(request.getIsSelf())) {
            map.put("self", request.getIsSelf());
        }
        if (StrUtil.isNotEmpty(request.getKeywords())) {
            map.put("keywords", request.getKeywords());
        }
        Page<StoreProduct> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<PlatformProductListResponse> proList = dao.getPlatformPageList(map);
        return CommonPage.copyPageInfo(page, proList);
    }

    /**
     * 商品审核
     *
     * @param request 审核参数
     * @return Boolean
     */
    @Override
    public Boolean audit(StoreProductAuditRequest request) {
        if (request.getAuditStatus().equals("fail") && StrUtil.isEmpty(request.getReason())) {
            throw new CrmebException("审核拒绝请填写拒绝原因");
        }
        StoreProduct product = getByIdException(request.getId());
        if (!product.getAuditStatus().equals(ProductConstants.AUDIT_STATUS_WAIT)) {
            throw new CrmebException("商品并非等待审核状态");
        }
        if (request.getAuditStatus().equals("fail")) {
            product.setAuditStatus(ProductConstants.AUDIT_STATUS_FAIL);
            product.setReason(request.getReason());
            return updateById(product);
        }
        // 审核成功
        product.setAuditStatus(ProductConstants.AUDIT_STATUS_SUCCESS);
        product.setIsForced(false);
        product.setIsShow(true);
        return updateById(product);
    }

    /**
     * 强制下加商品
     *
     * @param request 商品id参数
     * @return Boolean
     */
    @Override
    public Boolean forceDown(ProductForceDownRequest request) {
        String ids = request.getIds();
        List<Integer> idList = Stream.of(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
        LambdaUpdateWrapper<StoreProduct> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(StoreProduct::getIsForced, true);
        wrapper.set(StoreProduct::getIsShow, false);
        wrapper.in(StoreProduct::getId, idList);
        boolean update = update(wrapper);
        if (update) {
            idList.forEach(id -> {
                storeCartService.productStatusNotEnable(id);
                // 商品下架时，清除用户收藏
                storeProductRelationService.deleteByProId(id);
            });
        }
        return update;
    }

    /**
     * 修改虚拟销量
     *
     * @param request 修改参数
     * @return Boolean
     */
    @Override
    public Boolean updateVirtualSales(ProductVirtualSalesRequest request) {
        StoreProduct product = getByIdException(request.getId());
        if (request.getType().equals("sub") && product.getFicti() - request.getNum() < 0) {
            throw new CrmebException("修改后虚拟销量不能小于0");
        }
        UpdateWrapper<StoreProduct> wrapper = Wrappers.update();
        if (request.getType().equals("add")) {
            wrapper.setSql(StrUtil.format(" ficti = ficti + {}", request.getNum()));
        } else {
            wrapper.setSql(StrUtil.format(" ficti = ficti - {}", request.getNum()));
            wrapper.last(StrUtil.format(" and ficti - {} >= 0", request.getNum()));
        }
        wrapper.eq("id", request.getId());
        return update(wrapper);
    }

    /**
     * 是否有商品使用对应的商户商品分类
     *
     * @param id 商户商品分类id
     * @return Boolean
     */
    @Override
    public Boolean isExistStoreCategory(Integer id) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId);
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.apply(" find_in_set({0}, cate_id)", id);
        lqw.last(" limit 1");
        StoreProduct storeProduct = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(storeProduct);
    }

    /**
     * 商品增加浏览量
     *
     * @param proId 商品id
     * @return Boolean
     */
    @Override
    public Boolean addBrowse(Integer proId) {
        LambdaUpdateWrapper<StoreProduct> wrapper = Wrappers.lambdaUpdate();
        wrapper.setSql("browse = browse + 1");
        wrapper.eq(StoreProduct::getId, proId);
        return update(wrapper);
    }

    /**
     * 获取商户推荐商品
     *
     * @param merId 商户id
     * @param num   查询商品数量
     * @return List
     */
    @Override
    public List<ProMerchantProductResponse> getRecommendedProductsByMerId(Integer merId, Integer num) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId, StoreProduct::getMerId, StoreProduct::getImage, StoreProduct::getStoreName,
                StoreProduct::getPrice, StoreProduct::getSales, StoreProduct::getFicti);
        lqw.eq(StoreProduct::getMerId, merId);
        lqw.eq(StoreProduct::getIsRecycle, false);
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.eq(StoreProduct::getIsShow, true);
        lqw.eq(StoreProduct::getIsForced, false);
        lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
        lqw.orderByDesc(StoreProduct::getSort);
        lqw.last("limit " + num);
        List<StoreProduct> productList = dao.selectList(lqw);
        if (CollUtil.isEmpty(productList)) {
            return CollUtil.newArrayList();
        }
        return productList.stream().map(product -> {
            ProMerchantProductResponse response = new ProMerchantProductResponse();
            BeanUtils.copyProperties(product, response);
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 商户商品列表
     *
     * @param request          搜索参数
     * @param pageParamRequest 分页参数
     * @return List
     */
    @Override
    public PageInfo<StoreProduct> findMerchantProH5List(MerchantProductSearchRequest request, PageParamRequest pageParamRequest) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        // id、名称、图片、价格、销量
        lqw.select(StoreProduct::getId, StoreProduct::getStoreName, StoreProduct::getImage, StoreProduct::getPrice, StoreProduct::getOtPrice,
                StoreProduct::getSales, StoreProduct::getFicti, StoreProduct::getUnitName, StoreProduct::getStock);

        getForSaleWhere(lqw);
        lqw.eq(StoreProduct::getMerId, request.getMerId());
        if (StrUtil.isNotBlank(request.getKeyword())) {
            lqw.and(i -> i.like(StoreProduct::getStoreName, request.getKeyword())
                    .or().like(StoreProduct::getKeyword, request.getKeyword()));
        }
        if (ObjectUtil.isNotNull(request.getCid()) && request.getCid() > 0) {
            lqw.apply(StrUtil.format(" find_in_set({}, cate_id)", request.getCid()));
        }
        if (ObjectUtil.isNotNull(request.getMaxPrice())) {
            lqw.le(StoreProduct::getPrice, request.getMaxPrice());
        }
        if (ObjectUtil.isNotNull(request.getMinPrice())) {
            lqw.ge(StoreProduct::getPrice, request.getMinPrice());
        }
        // 排序部分
        if (StrUtil.isNotBlank(request.getSalesOrder())) {
            if (request.getSalesOrder().equals(Constants.SORT_DESC)) {
                lqw.last(" order by (sales + ficti) desc, sort desc, id desc");
            } else {
                lqw.last(" order by (sales + ficti) asc, sort desc, id desc");
            }
        } else {
            if (StrUtil.isNotBlank(request.getPriceOrder())) {
                if (request.getPriceOrder().equals(Constants.SORT_DESC)) {
                    lqw.orderByDesc(StoreProduct::getPrice);
                } else {
                    lqw.orderByAsc(StoreProduct::getPrice);
                }
            }

            lqw.orderByDesc(StoreProduct::getSort);
            lqw.orderByDesc(StoreProduct::getId);
        }
        Page<StoreProduct> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<StoreProduct> productList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, productList);
    }

    /**
     * 修改商品后台排序
     *
     * @param id   商品id
     * @param rank 商品后台排序
     * @return Boolean
     */
    @Override
    public Boolean updateRank(Integer id, Integer rank) {
        if (rank < 0 || rank > 9999) {
            throw new CrmebException("商品后台排序范围为0~9999");
        }
        StoreProduct product = getByIdException(id);
        if (product.getRank().equals(rank)) {
            return Boolean.TRUE;
        }
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setId(id);
        storeProduct.setRank(rank);
        return updateById(storeProduct);
    }

    /**
     * 判断商品是否使用品牌
     *
     * @param brandId 品牌id
     * @return Boolean
     */
    @Override
    public Boolean isUseBrand(Integer brandId) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId);
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.eq(StoreProduct::getBrandId, brandId);
        lqw.last("limit 1");
        StoreProduct storeProduct = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(storeProduct);
    }

    /**
     * 判断商品是否使用平台分类
     *
     * @param categoryId 平台分类id
     * @return Boolean
     */
    @Override
    public Boolean isUsePlatformCategory(Integer categoryId) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId);
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.eq(StoreProduct::getCategoryId, categoryId);
        lqw.last("limit 1");
        StoreProduct storeProduct = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(storeProduct);
    }

    /**
     * 查询使用服务保障的商品列表
     *
     * @param gid 服务保障id
     * @return List
     */
    @Override
    public List<StoreProduct> findUseGuarantee(Integer gid) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId, StoreProduct::getMerId);
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.apply(" find_in_set({0}, guarantee_ids)", gid);
        return dao.selectList(lqw);
    }

    /**
     * 判断商品是否使用服务保障
     *
     * @param gid 服务保障id
     * @return Boolean
     */
    @Override
    public Boolean isUseGuarantee(Integer gid) {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.select(StoreProduct::getId);
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.apply(" find_in_set({0}, guarantee_ids)", gid);
        lqw.last("limit 1");
        StoreProduct storeProduct = dao.selectOne(lqw);
        return ObjectUtil.isNotNull(storeProduct);
    }

    /**
     * 获取待审核商品数量
     */
    @Override
    public Integer getAwaitAuditNum() {
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(StoreProduct::getIsDel, false);
        lqw.eq(StoreProduct::getIsRecycle, false);
        lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_WAIT);
        return dao.selectCount(lqw);
    }

    /**
     * 下架商品商品
     *
     * @param merId 商户id
     */
    @Override
    public Boolean downByMerId(Integer merId) {
        LambdaUpdateWrapper<StoreProduct> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(StoreProduct::getIsShow, true);
        wrapper.eq(StoreProduct::getMerId, merId);
        wrapper.eq(StoreProduct::getIsShow, false);
        return update(wrapper);
    }

    /**
     * 平台端获取商品表头数量
     *
     * @return List
     */
    @Override
    public List<StoreProductTabsHeader> getPlatformTabsHeader() {
        List<StoreProductTabsHeader> headers = new ArrayList<>();
        StoreProductTabsHeader header1 = new StoreProductTabsHeader(0, 1);
        StoreProductTabsHeader header2 = new StoreProductTabsHeader(0, 2);
        StoreProductTabsHeader header6 = new StoreProductTabsHeader(0, 6);
        StoreProductTabsHeader header7 = new StoreProductTabsHeader(0, 7);
        headers.add(header1);
        headers.add(header2);
        headers.add(header6);
        headers.add(header7);

        LambdaQueryWrapper<StoreProduct> lqw = new LambdaQueryWrapper<>();
        for (StoreProductTabsHeader h : headers) {
            lqw.clear();
            lqw.eq(StoreProduct::getIsRecycle, false);
            lqw.eq(StoreProduct::getIsDel, false);
            switch (h.getType()) {
                case 1:
                    //出售中（已上架）
                    lqw.eq(StoreProduct::getIsForced, false);
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                    lqw.eq(StoreProduct::getIsShow, true);
                    break;
                case 2:
                    //仓库中（未上架）
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_SUCCESS);
                    lqw.eq(StoreProduct::getIsShow, false);
                    break;
                case 6:
                    //待审核
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_WAIT);
                    break;
                case 7:
                    //审核失败
                    lqw.eq(StoreProduct::getAuditStatus, ProductConstants.AUDIT_STATUS_FAIL);
                    break;
                default:
                    break;
            }
            List<StoreProduct> storeProducts = dao.selectList(lqw);
            h.setCount(storeProducts.size());
        }

        return headers;
    }

    private StoreProduct getByIdException(Integer id) {
        StoreProduct storeProduct = getById(id);
        if (ObjectUtil.isNull(storeProduct) || storeProduct.getIsDel()) {
            throw new CrmebException("商品不存在");
        }
        return storeProduct;
    }

}

