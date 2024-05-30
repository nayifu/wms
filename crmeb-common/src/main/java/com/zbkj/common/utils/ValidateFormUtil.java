package com.zbkj.common.utils;

import cn.hutool.core.util.ReUtil;
import com.zbkj.common.constants.RegularConstants;
import com.zbkj.common.exception.CrmebException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 表单验证类
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
public class ValidateFormUtil {

    /**
     * 邮箱
     * @param value String 值
     * @param info String 字段名
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    public static void isEmail(String value, String info) {
        regularException(value, info, RegularConstants.EMAIL, "邮箱");
    }

    /**
     * 手机
     * @param value String 值
     * @param info String 字段名
     */
    public static void isPhone(String value, String info) {
        regularException(value, info, RegularConstants.PHONE_TWO, "手机");
    }

    /**
     * 验证必填
     * @param value String 值
     * @param info String 字段名
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    public static void isRequire(String value, String info){
        if(StringUtils.isBlank(value) ){
            throw new CrmebException("请填写/选择" + info);
        }
    }

    /**
     * 数字验证
     * @param value String 值
     * @param info String 字段名
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    public static void isNumber(String value, String info){
        regularException(value, info, RegularConstants.NUMBER, "数字");
    }

    /**
     * 数字范围
     * @param value String 值
     * @param info String 字段名
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    public static void range(String value, String info, Integer max, Integer min){
        isNumber(value, info);
        max(value, info, max);
        min(value, info, min);
    }

    /**
     * 最大数值
     * @param value String 值
     * @param info String 字段名
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    public static void max(String value, String info, Integer max){
        isNumber(value, info);
        int number = Integer.parseInt(value);
        if(number > max ){
            throw new CrmebException(info + "不在取值范围内，最大不能大于" + max);
        }
    }

    /**
     * 最小数值
     * @param value String 值
     * @param info String 字段名
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    public static void min(String value, String info, Integer min){
        isNumber(value, info);
        int number = Integer.parseInt(value);
        if(number > min ){
            throw new CrmebException(info + "不在取值范围内，最小不能小于" + min);
        }
    }

    /**
     * 正则表达式验证
     * @param value String 值
     * @param info String 字段名
     * @param regular String 正则表达式
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    public static void regularException(String value, String info, String regular, String title){
        if(!regular(value, info, regular)){
            //正则验证
            throw new CrmebException(info + " 格式必须为 " + title);
        }
    }

    /**
     * 正则表达式验证
     * @param value String 值
     * @param info String 字段名
     * @param regular String 正则表达式
     * @author Mr.Zhang
     * @since 2020-05-11
     */
    public static boolean regular(String value, String info, String regular){
        isRequire(value, info);
        Pattern pattern = Pattern.compile(regular);
        return pattern.matcher(value).matches();
    }

    /**
     * 校验是否是手机号，不是则抛出异常
     * @param phone 手机号
     * @param errMsg 异常内容
     */
    public static void isPhoneException(String phone, String errMsg) {
        boolean match = ReUtil.isMatch(RegularConstants.PHONE_TWO, phone);
        if (!match) {
            throw new CrmebException(errMsg);
        }
    }

    /**
     * 校验是否是手机号，不是则抛出异常
     * @param phone 手机号
     */
    public static void isPhoneException(String phone) {
        boolean match = ReUtil.isMatch(RegularConstants.PHONE_TWO, phone);
        if (!match) {
            throw new CrmebException("请输入正确的手机号");
        }
    }

    /**
     * 校验是否是邮箱，不是则抛出异常
     * @param email 邮箱
     */
    public static void isEmailException(String email) {
        boolean match = ReUtil.isMatch(RegularConstants.EMAIL, email);
        if (!match) {
            throw new CrmebException("please enter your vaild email");
        }
    }
}
