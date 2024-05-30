package com.zbkj.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.RedisConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.express.Express;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ExpressSearchRequest;
import com.zbkj.common.request.ExpressUpdateShowRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.utils.RedisUtil;
import com.zbkj.service.dao.ExpressDao;
import com.zbkj.service.service.ExpressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ExpressServiceImpl 接口实现
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
public class ExpressServiceImpl extends ServiceImpl<ExpressDao, Express> implements ExpressService {

    @Resource
    private ExpressDao dao;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页显示快递公司表
     * @param request 搜索条件
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<Express> getPlatformPage(ExpressSearchRequest request, PageParamRequest pageParamRequest) {
        Page<Express> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<Express> lqw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getKeywords())) {
            lqw.like(Express::getCode, request.getKeywords()).or().like(Express::getName, request.getKeywords());
        }
        // 排序：sort字段倒序，正常id正序，方便展示常用物流公司
        lqw.orderByDesc(Express::getSort);
        lqw.orderByAsc(Express::getId);
        List<Express> expressList = dao.selectList(lqw);
        return CommonPage.copyPageInfo(page, expressList);
    }

    /**
     * 查询全部物流公司
     */
    @Override
    public List<Express> findAll() {
        if (redisUtil.exists(RedisConstants.EXPRESS_CACHE_LIST_KEY)) {
            return redisUtil.get(RedisConstants.EXPRESS_CACHE_LIST_KEY);
        }
        LambdaQueryWrapper<Express> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Express::getIsShow, true);
        lqw.eq(Express::getStatus, true);
        lqw.orderByDesc(Express::getSort);
        lqw.orderByAsc(Express::getId);
        List<Express> expressList = dao.selectList(lqw);
        redisUtil.set(RedisConstants.EXPRESS_CACHE_LIST_KEY, expressList);
        return expressList;
    }

    /**
     * 查询快递公司
     * @param code 快递公司编号
     */
    @Override
    public Express getByCode(String code) {
        LambdaQueryWrapper<Express> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Express::getCode, code);
        lqw.last("limit 1");
        return dao.selectOne(lqw);
    }

    /**
     * 修改显示状态
     */
    @Override
    public Boolean updateExpressShow(ExpressUpdateShowRequest expressRequest) {
        Express temp = getById(expressRequest.getId());
        if (ObjectUtil.isNull(temp)) throw new CrmebException("编辑的记录不存在!");
        if (temp.getIsShow().equals(expressRequest.getIsShow())) {
            return Boolean.TRUE;
        }
        Express express = new Express();
        BeanUtils.copyProperties(expressRequest, express);
        boolean update = updateById(express);
        if (update) {
            // 删除物流公司缓存
            redisUtil.delete(RedisConstants.EXPRESS_CACHE_LIST_KEY);
        }
        return update;
    }
}

