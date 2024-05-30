package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.system.SystemConfig;
import com.zbkj.common.request.SystemConfigAdminRequest;
import com.zbkj.common.request.SystemConfigSaveRequest;
import com.zbkj.common.request.SystemFormCheckRequest;

import java.util.HashMap;
import java.util.List;

/**
 * SystemConfigService 接口
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
public interface SystemConfigService extends IService<SystemConfig> {

    /**
     * 根据menu name 获取 value
     * @param key menu name
     * @return String
     */
    String getValueByKey(String key);

    /**
     * 保存或更新配置数据
     * @param name 菜单名称
     * @param value 菜单值
     * @return Boolean
     */
    Boolean updateOrSaveValueByName(String name, String value);

    /**
     * 根据 name 获取 value 找不到抛异常
     * @param key menu name
     * @return String
     */
    String getValueByKeyException(String key);

    /**
     * 整体保存表单数据
     * @param systemFormCheckRequest SystemFormCheckRequest 数据保存
     * @return Boolean
     */
    Boolean saveForm(SystemFormCheckRequest systemFormCheckRequest);

    /**
     * 根据formId查询数据
     * @param formId Integer id
     * @return HashMap<String, String>
     */
    HashMap<String, String> info(Integer formId);

    /**
     * 根据name查询数据
     * @param name name
     * @return Boolean
     */
    Boolean checkName(String name);

    /**
     * 根据key获取配置
     * @param key key
     * @return List
     */
    List<SystemConfig> getListByKey(String key);

    /**
     * 更新配置信息
     * @param requestList 请求数组
     * @return Boolean
     */
    Boolean updateByList(List<SystemConfigAdminRequest> requestList);

    /**
     * 获取颜色配置
     * @return SystemConfig
     */
    SystemConfig getColorConfig();

    /**
     * 获取商户入驻协议
     * @return String
     */
    String getMerchantSettlementAgreement();

    /**
     * 获取用户协议
     * @return String
     */
    String getUserRegisterAgreement();
}
