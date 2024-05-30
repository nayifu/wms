package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.DateConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.StoreOrderSearchRequest;
import com.zbkj.common.request.StoreProductSearchRequest;
import com.zbkj.common.response.AdminProductListResponse;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.common.utils.DateUtil;
import com.zbkj.common.utils.ExportUtil;
import com.zbkj.common.vo.OrderExcelVo;
import com.zbkj.common.vo.ProductExcelVo;
import com.zbkj.service.service.ExcelService;
import com.zbkj.service.service.StoreProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ExcelServiceImpl 接口实现
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
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private StoreProductService storeProductService;

    @Autowired
    private CrmebConfig crmebConfig;

    /**
     * 商品导出
     *
     * @param request 请求参数
     * @return 导出地址
     */
    @Override
    public String exportProduct(StoreProductSearchRequest request) {
        PageParamRequest pageParamRequest = new PageParamRequest();
        pageParamRequest.setPage(Constants.DEFAULT_PAGE);
        pageParamRequest.setLimit(Constants.EXPORT_MAX_LIMIT);
        PageInfo<AdminProductListResponse> storeProductResponsePageInfo = storeProductService.getAdminList(request, pageParamRequest);
        List<AdminProductListResponse> list = storeProductResponsePageInfo.getList();
        if (list.size() < 1) {
            throw new CrmebException("没有可导出的数据！");
        }

        //产品分类id
        List<String> cateIdListStr = list.stream().map(AdminProductListResponse::getCateId).distinct().collect(Collectors.toList());

        HashMap<Integer, String> categoryNameList = new HashMap<Integer, String>();
        if (cateIdListStr.size() > 0) {
            String join = StringUtils.join(cateIdListStr, ",");
            List<Integer> cateIdList = CrmebUtil.stringToArray(join);
//            categoryNameList = categoryService.getListInId(cateIdList);
        }
        List<ProductExcelVo> voList = CollUtil.newArrayList();
        for (AdminProductListResponse product : list) {
            ProductExcelVo vo = new ProductExcelVo();
            vo.setStoreName(product.getStoreName());
//            vo.setStoreInfo(product.getStoreInfo());
            vo.setCateName(CrmebUtil.getValueByIndex(categoryNameList, product.getCateId()));
            vo.setPrice("￥" + product.getPrice());
            vo.setStock(product.getStock().toString());
            vo.setSales(product.getSales().toString());
//            vo.setBrowse(product.getBrowse().toString());
            voList.add(vo);
        }

        /**
         * ===============================
         * 以下为存储部分
         * ===============================
         */
        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "商品导出_".concat(DateUtil.nowDateTime(DateConstants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");

        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();
        aliasMap.put("storeName", "商品名称");
//        aliasMap.put("storeInfo", "商品简介");
        aliasMap.put("cateName", "商品分类");
        aliasMap.put("price", "价格");
        aliasMap.put("stock", "库存");
        aliasMap.put("sales", "销量");
//        aliasMap.put("browse", "浏览量");

        return ExportUtil.exportExecl(fileName, "商品导出", voList, aliasMap);
    }

    /**
     * 订单导出
     *
     * @param request 查询条件
     * @return 文件名称
     */
    @Override
    public String exportOrder(StoreOrderSearchRequest request) {
        PageParamRequest pageParamRequest = new PageParamRequest();
        pageParamRequest.setPage(Constants.DEFAULT_PAGE);
        pageParamRequest.setLimit(Constants.EXPORT_MAX_LIMIT);
//        CommonPage<StoreOrderDetailResponse> adminList = storeOrderService.getAdminList(request, pageParamRequest);
//        List<StoreOrderDetailResponse> list = adminList.getList();
//        if(list.size() < 1){
//            throw new CrmebException("没有可导出的数据！");
//        }

        List<OrderExcelVo> voList = CollUtil.newArrayList();
//        for (StoreOrderDetailResponse order: list ) {
//            OrderExcelVo vo = new OrderExcelVo();
//            vo.setCreateTime(DateUtil.dateToStr(order.getCreateTime(), DateConstants.DATE_FORMAT));
//            vo.setOrderId(order.getOrderId());
//            vo.setOrderType(order.getOrderType());
//            vo.setPayPrice(order.getPayPrice().toString());
//            vo.setPayTypeStr(order.getPayTypeStr());
//            vo.setProductName(order.getProductList().stream().map(item-> item.getInfo().getProductName()).collect(Collectors.joining(",")));
//            vo.setRealName(order.getRealName());
//            vo.setStatusStr(order.getStatusStr().get("value"));
//            voList.add(vo);
//        }

        /*
          ===============================
          以下为存储部分
          ===============================
         */
        // 上传设置
        ExportUtil.setUpload(crmebConfig.getImagePath(), Constants.UPLOAD_MODEL_PATH_EXCEL, Constants.UPLOAD_TYPE_FILE);

        // 文件名
        String fileName = "订单导出_".concat(DateUtil.nowDateTime(DateConstants.DATE_TIME_FORMAT_NUM)).concat(CrmebUtil.randomCount(111111111, 999999999).toString()).concat(".xlsx");

        //自定义标题别名
        LinkedHashMap<String, String> aliasMap = new LinkedHashMap<>();
        aliasMap.put("orderId", "订单号");
        aliasMap.put("payPrice", "实际支付金额");
//        aliasMap.put("payType", "支付方式");
        aliasMap.put("createTime", "创建时间");
//        aliasMap.put("status", "订单状态");
        aliasMap.put("productName", "商品信息");
        aliasMap.put("statusStr", "订单状态");
        aliasMap.put("payTypeStr", "支付方式");
//        aliasMap.put("isDel", "是否删除");
//        aliasMap.put("refundReasonWapImg", "退款图片");
//        aliasMap.put("refundReasonWapExplain", "退款用户说明");
//        aliasMap.put("refundReasonTime", "退款时间");
//        aliasMap.put("refundReasonWap", "前台退款原因");
//        aliasMap.put("refundReason", "不退款的理由");
//        aliasMap.put("refundPrice", "退款金额");
//        aliasMap.put("refundStatus", "退款状态状态，0 未退款 1 申请中 2 已退款");
//        aliasMap.put("verifyCode", "核销码");
        aliasMap.put("orderType", "订单类型");
//        aliasMap.put("remark", "订单管理员备注");
        aliasMap.put("realName", "用户姓名");
//        aliasMap.put("paid", "支付状态");
//        aliasMap.put("type", "订单类型:0-普通订单，1-视频号订单");
//        aliasMap.put("isAlterPrice", "是否改价,0-否，1-是");

        return ExportUtil.exportExecl(fileName, "订单导出", voList, aliasMap);

    }
}

