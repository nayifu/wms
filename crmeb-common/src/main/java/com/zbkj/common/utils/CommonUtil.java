package com.zbkj.common.utils;

import cn.hutool.core.date.DateTime;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 通用工具类
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
public class CommonUtil {

    /**
     * 随机生成密码
     *
     * @param phone 手机号
     * @return 密码
     * 使用des方式加密
     */
    public static String createPwd(String phone) {
        String password = "Abc" + CrmebUtil.randomCount(10000, 99999);
        return CrmebUtil.encryptPassword(password, phone);
    }

    /**
     * 随机生成用户昵称
     *
     * @param phone 手机号
     * @return 昵称
     */
    public static String createNickName(String phone) {
        return DigestUtils.md5Hex(phone + DateUtil.getNowTime()).
                subSequence(0, 12).
                toString();
    }

    /**
     * 创建用户标识
     * @return 用户标识
     */
    public static String createIdentity() {
        Integer randomCount = CrmebUtil.randomCount(10000, 99999);
        DateTime dateTime = cn.hutool.core.date.DateUtil.date();
        return "crmeb" + dateTime.toString("yyyyMMdd") + randomCount;
    }
}
