package com.zbkj.service.delete;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.product.StoreProduct;
import com.zbkj.common.model.product.StoreProductAttr;
import com.zbkj.common.request.StoreProductRequest;
import com.zbkj.common.utils.UrlUtil;
import com.zbkj.service.service.SystemConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 商品工具类
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
public class ProductUtils {
    private String baseUrl;

    String rightUrl;

    @Autowired
    private SystemConfigService systemConfigService;

    /**
     * 解析淘宝产品数据
     *
     * @param url
     * @param tag
     * @throws IOException
     * @throws JSONException
     */
    public StoreProductRequest getTaobaoProductInfo(String url, int tag) throws JSONException, IOException {
        setConfig(url, tag);
        JSONObject tbJsonData = getRequestFromUrl(baseUrl + rightUrl);
        JSONObject data = tbJsonData.getJSONObject("data");
        if (null == data) throw new CrmebException("复制商品失败--返回数据格式错误--未找到data");
        JSONObject item = data.getJSONObject("item");
        if (null == item) throw new CrmebException("复制商品失败--返回数据格式错误--未找到item");

        StoreProductRequest productRequest = new StoreProductRequest();
        productRequest.setStoreName(item.getString("title"));
        productRequest.setStoreInfo(item.getString("title"));
        productRequest.setSliderImage(item.getString("images"));
        productRequest.setImage(item.getString("images").split(",")[0]
                .replace("[", "").replace("\"", ""));
        productRequest.setKeyword(item.getString("title"));
        productRequest.setContent(item.getString("desc"));

        JSONArray props = item.getJSONArray("props");
//        if (null == props) throw new CrmebException("复制商品失败--返回数据格式错误--未找到props");
        if (null == props || props.size() < 1) {
            productRequest.setSpecType(false);
            return productRequest;
        }
        productRequest.setSpecType(true);
        List<StoreProductAttr> spaAttes = new ArrayList<>();
        for (int i = 0; i < props.size(); i++) {
            JSONObject pItem = props.getJSONObject(i);
            StoreProductAttr spAttr = new StoreProductAttr();
            spAttr.setAttrName(pItem.getString("name"));
            JSONArray values = pItem.getJSONArray("values");
            List<String> attrValues = new ArrayList<>();
            for (int j = 0; j < values.size(); j++) {
                JSONObject value = values.getJSONObject(j);
                attrValues.add(value.getString("name"));
            }
            spAttr.setAttrValues(JSON.toJSONString(attrValues));
            spaAttes.add(spAttr);
        }
        productRequest.setAttr(spaAttes);
        return productRequest;
    }

    /**
     * 解析京东产品数据
     *
     * @param url
     * @param tag
     * @return
     * @throws JSONException
     */
    public StoreProductRequest getJDProductInfo(String url, int tag) throws JSONException, IOException {
        setConfig(url, tag);
        JSONObject tbJsonData = getRequestFromUrl(baseUrl + rightUrl);
        JSONObject data = tbJsonData.getJSONObject("data");
        if (null == data) throw new CrmebException("复制商品失败--返回数据格式错误--未找到data");
        JSONObject item = data.getJSONObject("item");
        if (null == item) throw new CrmebException("复制商品失败--返回数据格式错误--未找到item");

        StoreProductRequest productRequest = new StoreProductRequest();
        productRequest.setStoreName(item.getString("name"));
        productRequest.setStoreInfo(item.getString("name"));
        productRequest.setSliderImage(item.getString("images"));
        productRequest.setImage(item.getString("images").split(",")[0]
                .replace("[", "").replace("\"", ""));
        productRequest.setPrice(BigDecimal.valueOf(item.getDouble("price")));
        productRequest.setContent(item.getString("desc"));

        JSONObject props = item.getJSONObject("skuProps");
        if (null == props) throw new CrmebException("复制商品失败--返回数据格式错误--未找到props");
        List<StoreProductAttr> spaAttrs = new ArrayList<>();
        JSONObject saleJson = item.getJSONObject("saleProp");
        int attrValueIsNullCount = 0;
        Iterator<String> saleProps = saleJson.keySet().iterator();
        while (saleProps.hasNext()) {
            StoreProductAttr spattr = new StoreProductAttr();
            String stepkey = saleProps.next();
            String stepValue = props.getString(stepkey);
            String stepValueValidLength = stepValue.replace("[", "").replace("]", "").replace("\"", "");
            if (stepValueValidLength.length() > 0) {
                com.alibaba.fastjson.JSONArray stepValues = JSON.parseArray(stepValue);
                int c = stepValues.get(0).toString().length();
                attrValueIsNullCount += c == 0 ? 1 : 0;
                spattr.setAttrName(saleJson.getString(stepkey));
                spattr.setAttrValues(props.getString(stepkey));
                spaAttrs.add(spattr);
                productRequest.setAttr(spaAttrs);
            } else {
                attrValueIsNullCount += 1;
            }
        }
        // 判断是否单属性
        productRequest.setSpecType(spaAttrs.size() != attrValueIsNullCount);
        return productRequest;
    }

    /**
     * 解析天猫产品数据
     *
     * @param url
     * @param tag
     * @return
     * @throws JSONException
     */
    public StoreProductRequest getTmallProductInfo(String url, int tag) throws JSONException, IOException {
        setConfig(url, tag);
        JSONObject tbJsonData = getRequestFromUrl(baseUrl + rightUrl);
        JSONObject data = tbJsonData.getJSONObject("data");
        if (null == data) throw new CrmebException("复制商品失败--返回数据格式错误--未找到data");
        JSONObject item = data.getJSONObject("item");
        if (null == item) throw new CrmebException("复制商品失败--返回数据格式错误--未找到item");

        StoreProductRequest productRequest = new StoreProductRequest();
        productRequest.setStoreName(item.getString("title"));
        productRequest.setStoreInfo(item.getString("subTitle"));
        productRequest.setSliderImage(item.getString("images"));
        productRequest.setImage(item.getString("images").split(",")[0]
                .replace("[", "").replace("\"", ""));
        productRequest.setKeyword(item.getString("title"));
        productRequest.setContent(item.getString("descUrl"));

        JSONArray props = item.getJSONArray("props");
//        if (null == props) throw new CrmebException("复制商品失败--返回数据格式错误--未找到props");
        if (null == props || props.size() < 1) {
            // 无规格商品
            productRequest.setSpecType(false);
            return productRequest;
        }
        productRequest.setSpecType(true);
        List<StoreProductAttr> spaAttes = new ArrayList<>();
        for (int i = 0; i < props.size(); i++) {
            JSONObject pItem = props.getJSONObject(i);
            StoreProductAttr spattr = new StoreProductAttr();
            spattr.setAttrName(pItem.getString("name"));
            JSONArray values = pItem.getJSONArray("values");
            List<String> attrValues = new ArrayList<>();
            for (int j = 0; j < values.size(); j++) {
                JSONObject value = values.getJSONObject(j);
                attrValues.add(value.getString("name"));
            }
            spattr.setAttrValues(JSON.toJSONString(attrValues));
            spaAttes.add(spattr);
        }
        productRequest.setAttr(spaAttes);
        return productRequest;
    }

    /**
     * 解析拼多多产品数据
     *
     * @param url
     * @param tag
     * @return
     * @throws JSONException
     */
    public StoreProductRequest getPddProductInfo(String url, int tag) throws JSONException, IOException {
        setConfig(url, tag);
        JSONObject tbJsonData = getRequestFromUrl(baseUrl + rightUrl);
        JSONObject data = tbJsonData.getJSONObject("data");
        if (null == data) throw new CrmebException("复制商品失败--返回数据格式错误--未找到data");
        JSONObject item = data.getJSONObject("item");
        if (null == item) throw new CrmebException("复制商品失败--返回数据格式错误--未找到item");

        StoreProductRequest productRequest = new StoreProductRequest();
        productRequest.setStoreName(item.getString("goodsName"));
        productRequest.setStoreInfo(item.getString("goodsDesc"));
        productRequest.setSliderImage(item.getString("thumbUrl"));
        productRequest.setImage(item.getString("banner"));
        productRequest.setPrice(BigDecimal.valueOf(item.getDouble("maxNormalPrice")));
        productRequest.setOtPrice(BigDecimal.valueOf(item.getDouble("marketPrice")));

        JSONArray props = item.getJSONArray("skus");
        if (null == props) throw new CrmebException("复制商品失败--返回数据格式错误--未找到props");
        if (props.size() > 0) {
            List<StoreProductAttr> spaAttes = new ArrayList<>();
            HashMap<String, List<String>> tempAttr = new HashMap<>();
            for (int i = 0; i < props.size(); i++) {
                JSONObject pItem = props.getJSONObject(i);
                JSONArray specArray = pItem.getJSONArray("specs");
                for (int j = 0; j < specArray.size(); j++) {
                    JSONObject specItem = specArray.getJSONObject(j);
                    String keyTemp = specItem.getString("spec_key");
                    String valueTemp = specItem.getString("spec_value");
                    if (tempAttr.containsKey(keyTemp)) {
                        if (!tempAttr.get(keyTemp).contains(valueTemp)) {
                            tempAttr.get(keyTemp).add(valueTemp);
                        }
                    } else {
                        List<String> tempList = new ArrayList<>();
                        tempList.add(valueTemp);
                        tempAttr.put(keyTemp, tempList);
                    }
                }

            }
            Iterator iterator = tempAttr.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                StoreProductAttr spattr = new StoreProductAttr();
                spattr.setAttrName(key);
                spattr.setAttrValues(tempAttr.get(key).toString());
                spaAttes.add(spattr);
            }
            productRequest.setAttr(spaAttes);
        }
        return productRequest;
    }

    /**
     * *** 苏宁返回的数据不一致，暂放
     * 解析苏宁产品数据
     *
     * @param url
     * @param tag
     * @return
     * @throws JSONException
     */
    public StoreProductRequest getSuningProductInfo(String url, int tag) throws JSONException, IOException {
        setConfig(url, tag);
        JSONObject tbJsonData = getRequestFromUrl(baseUrl + rightUrl);
        System.out.println("tbJsonData:" + tbJsonData);
        JSONObject data = tbJsonData.getJSONObject("data");
        if (null == data) throw new CrmebException("复制商品失败--返回数据格式错误--未找到data");

        StoreProductRequest productRequest = new StoreProductRequest();
        productRequest.setStoreName(data.getString("title"));
        productRequest.setStoreInfo(data.getString("title"));
        productRequest.setSliderImage(data.getString("images"));
        productRequest.setImage(data.getString("images").split(",")[0]
                .replace("[", "").replace("\"", ""));
        Long priceS = data.getLong("price");
        productRequest.setPrice(BigDecimal.valueOf(priceS));
        productRequest.setContent(data.getString("desc"));

        List<StoreProductAttr> spaAttes = new ArrayList<>();
        StoreProductAttr spattr = new StoreProductAttr();
        spattr.setAttrName("默认");
        List<String> attrValues = new ArrayList<>();
        attrValues.add("默认");
        spattr.setAttrValues(attrValues.toString());
        productRequest.setSpecType(false);
        productRequest.setAttr(spaAttes);
        return productRequest;
    }

    /**
     * 设置配置数据
     *
     * @param tag
     */
    public void setConfig(String url, int tag) {
        String rightEndUrl = "&itemid=";
        switch (tag) { // 导入平台1=淘宝，2=京东，3=苏宁，4=拼多多， 5=天猫
            case 1:
                baseUrl = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_IMPORT_PRODUCT_TB);
                rightEndUrl += UrlUtil.getParamsByKey(url, "id");
                break;
            case 2:
                baseUrl = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_IMPORT_PRODUCT_JD);
                String replace = url.substring(url.lastIndexOf("/") + 1);
                String substring = replace.substring(0, replace.indexOf("."));
                rightEndUrl += substring;
                break;
            case 3:
                baseUrl = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_IMPORT_PRODUCT_SN);
                int start = url.indexOf(".com/") + 5;
                int end = url.indexOf(".html");
                String sp = url.substring(start, end);
                String[] shopProduct = sp.split("/");
                rightEndUrl += shopProduct[1] + "&shopid=" + shopProduct[0];
                break;
            case 4:
                rightEndUrl += UrlUtil.getParamsByKey(url, "goods_id");
                baseUrl = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_IMPORT_PRODUCT_PDD);
                break;
            case 5:
                rightEndUrl += UrlUtil.getParamsByKey(url, "id");
                baseUrl = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_IMPORT_PRODUCT_TM);
                break;
        }
        String token = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_COPY_PRODUCT_APIKEY);
        if (StrUtil.isBlank(token)) {
            throw new CrmebException("请配置复制产品平台的Token -- www.99api.com");
        }
        if (StrUtil.isBlank(baseUrl)) {
            throw new CrmebException("请配置复制产品平台的Url-- www.99api.com");
        }
        rightUrl = "?apikey=" + token + rightEndUrl;

    }

    /**
     * 99api产品复制工具方法
     *
     * @param rd
     * @return
     * @throws IOException
     */
    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * 根据url访问99api后返回对应的平台的产品json数据
     *
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject getRequestFromUrl(String url) throws IOException, JSONException {
        URL realUrl = new URL(url);
        URLConnection conn = realUrl.openConnection();
        InputStream instream = conn.getInputStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(instream, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = JSONObject.parseObject(jsonText);
            return json;
        } finally {
            instream.close();
        }
    }

}
