package com.zbkj.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.bill.MerchantBill;
import com.zbkj.common.model.system.SystemAdmin;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.FundsMonitorRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.utils.SecurityUtil;
import com.zbkj.common.vo.dateLimitUtilVo;
import com.zbkj.service.dao.MerchantBillDao;
import com.zbkj.service.service.MerchantBillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
*  MerchantBillServiceImpl 接口实现
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
@Service
public class MerchantBillServiceImpl extends ServiceImpl<MerchantBillDao, MerchantBill> implements MerchantBillService {

    @Resource
    private MerchantBillDao dao;

    /**
     * 通过pid获取商户帐单列表
     * @param pid 父级id
     * @return List
     */
    @Override
    public List<MerchantBill> getByParentId(Integer pid) {
        LambdaQueryWrapper<MerchantBill> lqw = Wrappers.lambdaQuery();
        lqw.eq(MerchantBill::getPid, pid);
        return dao.selectList(lqw);
    }

    /**
     * 资金监控
     * @param request          查询参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<MerchantBill> getFundMonitoring(FundsMonitorRequest request, PageParamRequest pageParamRequest) {
        SystemAdmin systemAdmin = SecurityUtil.getLoginUserVo().getUser();
        Page<MerchantBill> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<MerchantBill> lqw = Wrappers.lambdaQuery();
        lqw.eq(MerchantBill::getMerId, systemAdmin.getMerId());
        if (StrUtil.isNotBlank(request.getOrderNo())) {
            lqw.eq(MerchantBill::getOrderNo, request.getOrderNo());
        }
        if (StrUtil.isNotBlank(request.getDateLimit())) {
            dateLimitUtilVo dateLimit = com.zbkj.common.utils.DateUtil.getDateLimit(request.getDateLimit());
            lqw.between(MerchantBill::getCreateTime, dateLimit.getStartTime(), dateLimit.getEndTime());
        }
        lqw.orderByDesc(MerchantBill::getId);
        List<MerchantBill> merchantBillList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, merchantBillList);
    }
}

