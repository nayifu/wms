package com.zbkj.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.SysConfigConstants;
import com.zbkj.common.model.system.SystemAttachment;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.request.SystemAttachmentMoveRequest;
import com.zbkj.common.utils.CrmebUtil;
import com.zbkj.service.dao.SystemAttachmentDao;
import com.zbkj.service.service.SystemAttachmentService;
import com.zbkj.service.service.SystemConfigService;
import com.zbkj.service.service.SystemRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * SystemAttachmentServiceImpl 接口实现
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
public class SystemAttachmentServiceImpl extends ServiceImpl<SystemAttachmentDao, SystemAttachment>
        implements SystemAttachmentService {

    @Resource
    private SystemAttachmentDao dao;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemRoleService systemRoleService;

    /**
     * 附件分页
     *
     * @param pid              Integer pid
     * @param attType          资源类型
     * @param pageParamRequest PageParamRequest 分页参数
     * @return List<SystemAttachment>
     */
    @Override
    public List<SystemAttachment> getList(Integer pid, String attType, PageParamRequest pageParamRequest) {
        PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<SystemAttachment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemAttachment::getOwner, systemRoleService.getOwnerByCurrentAdmin());
        if (StringUtils.isNotEmpty(attType)) {
            lambdaQueryWrapper.in(SystemAttachment::getAttType, CrmebUtil.stringToArrayStr(attType));
        }
        if (ObjectUtil.isNotNull(pid)) {
            lambdaQueryWrapper.eq(SystemAttachment::getPid, pid);
        }
        lambdaQueryWrapper.orderByDesc(SystemAttachment::getAttId);
        return dao.selectList(lambdaQueryWrapper);
    }

    /**
     * 给图片加前缀
     *
     * @param path String 路径
     * @return String
     */
    @Override
    public String prefixImage(String path) {
        // 如果那些域名不需要加，则跳过
        return path.replace(Constants.UPLOAD_TYPE_IMAGE + "/", getCdnUrl() + "/" + Constants.UPLOAD_TYPE_IMAGE + "/");
    }

    /**
     * 给文件加前缀
     *
     * @param path String 路径
     * @return String
     */
    @Override
    public String prefixFile(String path) {
        if (path.contains("file/excel")) {
            String cdnUrl = systemConfigService.getValueByKey(SysConfigConstants.CONFIG_LOCAL_UPLOAD_URL);
            return path.replace("crmebimage/file/", cdnUrl + "/crmebimage/file/");
        }
        return path.replace("crmebimage/file/", getCdnUrl() + "/crmebimage/file/");
    }

    /**
     * 获取cdn url
     *
     * @return String
     */
    @Override
    public String getCdnUrl() {
        String uploadType = systemConfigService.getValueByKeyException(SysConfigConstants.CONFIG_UPLOAD_TYPE);
        //获取配置信息
        int type = Integer.parseInt(uploadType);
        String uploadUrl = SysConfigConstants.CONFIG_LOCAL_UPLOAD_URL;
        switch (type) {
            case 2:
                uploadUrl = SysConfigConstants.CONFIG_QN_UPLOAD_URL;
                break;
            case 3:
                uploadUrl = SysConfigConstants.CONFIG_AL_UPLOAD_URL;
                break;
            case 4:
                uploadUrl = SysConfigConstants.CONFIG_TX_UPLOAD_URL;
                break;
            default:
                break;
        }
        return systemConfigService.getValueByKey(uploadUrl);
    }

    /**
     * 清除 cdn url， 在保存数据的时候使用
     *
     * @param path String 文件路径
     * @return String
     */
    @Override
    public String clearPrefix(String path) {
        if (StringUtils.isBlank(path)) {
            return path;
        }
        if (path.contains(getCdnUrl() + "/")) {
            return path.replace(getCdnUrl() + "/", "");
        }

        return path;
    }

    /**
     * 清除 cdn url， 在保存数据的时候使用
     *
     * @param path String 文件路径
     * @return String
     */
    @Override
    public String clearPrefix(String path, String cdnUrl) {
        if (StringUtils.isBlank(path)) {
            return path;
        }
        if (path.contains(cdnUrl + "/")) {
            return path.replace(cdnUrl + "/", "");
        }

        return path;
    }

    /**
     * 更改图片目录
     *
     * @param move 参数
     */
    @Override
    public Boolean updateAttrId(SystemAttachmentMoveRequest move) {
        LambdaUpdateWrapper<SystemAttachment> lup = new LambdaUpdateWrapper<>();
        lup.in(SystemAttachment::getAttId, CrmebUtil.stringToArray(move.getAttrId()));
        lup.set(SystemAttachment::getPid, move.getPid());
        lup.set(SystemAttachment::getOwner, systemRoleService.getOwnerByCurrentAdmin());
        return update(lup);
    }

    @Override
    public String prefixUploadf(String path) {
        // 如果那些域名不需要加，则跳过
        return path.replace(Constants.UPLOAD_TYPE_FILE + "/", getCdnUrl() + "/" + Constants.UPLOAD_TYPE_FILE + "/");
    }
}

