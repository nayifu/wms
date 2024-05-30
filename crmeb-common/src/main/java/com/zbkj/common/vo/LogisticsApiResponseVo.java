package com.zbkj.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  全球物流查询结果对象
 *  +----------------------------------------------------------------------
 *  | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 *  +----------------------------------------------------------------------
 *  | Copyright (c) 2016~2023 https://www.crmeb.com All rights reserved.
 *  +----------------------------------------------------------------------
 *  | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 *  +----------------------------------------------------------------------
 *  | Author: CRMEB Team <admin@crmeb.com>
 *  +----------------------------------------------------------------------
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogisticsApiResponseVo {

    /** 物流快递运单号 */
    private String LogisticCode;

    /** 快递公司编码 */
    private String ShipperCode;

    /** 快递物流轨迹,包含：AcceptStation-快递中转站，终点站， AcceptTime-事件时间 */
    private String Traces;

    /** 物流状态：-1：单号或快递公司代码错误, 0：暂无轨迹， 1：快递收件(揽件)，2：在途中,3：签收,4：问题件 5.疑难件 6.退件签收 */
    private String State;

    /** 成功与否 */
    private Boolean Success;

    /** 快递员 或 快递站(没有则为空) */
    private String Courier;

    /** 快递员电话 (没有则为空) */
    private String CourierPhone;

    /** 快递轨迹信息最新时间 */
    private String updateTime;

    /** 发货到收货消耗时长 (截止最新轨迹)  */
    private String takeTime;

    /** 快递公司名称  */
    private String Name;

    /** 快递公司官网 */
    private String Site;

    /** 快递公司电话 */
    private String Phone;

    /** 快递Logo */
    private String Logo;

    /** 提示信息 */
    private String Reason;

}
