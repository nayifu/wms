package com.zbkj.front.filter;

import com.zbkj.common.constants.Constants;
import com.zbkj.common.utils.SpringUtil;
import com.zbkj.service.service.SystemAttachmentService;

/**
 * response路径处理
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
public class ResponseRouter {

    public String filter(String data, String path) {
        boolean result = un().contains(path);
        if (result) {
            return data;
        }

        if (!path.contains("api/pc/") && !path.contains("api/front/")) {
            return data;
        }

        //根据需要处理返回值
        if ((data.contains(Constants.UPLOAD_TYPE_IMAGE+"/") && !data.contains("data:image/png;base64"))
                || data.contains(Constants.DOWNLOAD_TYPE_FILE) || data.contains(Constants.UPLOAD_TYPE_FILE)) {
            if (data.contains(Constants.DOWNLOAD_TYPE_FILE+"/"+Constants.UPLOAD_MODEL_PATH_EXCEL)) {
                data = SpringUtil.getBean(SystemAttachmentService.class).prefixFile(data);
            } else if(data.contains(Constants.UPLOAD_TYPE_FILE+"/"))  {
                data = SpringUtil.getBean(SystemAttachmentService.class).prefixUploadf(data);
            } else {
                data = SpringUtil.getBean(SystemAttachmentService.class).prefixImage(data);
            }
        }

        return data;
    }

    public static String un() {
        return "";
    }
}
