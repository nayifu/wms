package com.zbkj.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import com.zbkj.admin.dao.ScheduleJobDao;
import com.zbkj.admin.model.ScheduleJob;
import com.zbkj.admin.quartz.ScheduleConstants;
import com.zbkj.admin.quartz.ScheduleManager;
import com.zbkj.admin.service.ScheduleJobService;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
*  ScheduleJobServiceImpl 接口实现
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
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJob> implements ScheduleJobService {

    @Resource
    private ScheduleJobDao dao;

    @Autowired
    private ScheduleManager scheduleManager;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        getAll().forEach(scheduleJob -> {
            CronTrigger trigger = scheduleManager.getCronTrigger(scheduleJob);
            // 如果定时任务不存在，则创建定时任务
            if (trigger == null && !scheduleJob.getIsDelte()) {
                scheduleManager.createScheduleJob(scheduleJob);
            } else if (scheduleJob.getIsDelte()) {
                scheduleManager.deleteScheduleJob(scheduleJob);
            } else if (ScheduleConstants.NORMAL.equals(scheduleJob.getStatus())) {
                scheduleManager.resumeJob(scheduleJob);
            } else if (ScheduleConstants.PAUSE.equals(scheduleJob.getStatus())) {
                scheduleManager.pauseJob(scheduleJob);
            }
        });
    }

    /**
     * 获取所有的job
     * @return List<ScheduleJob>
     */
    private List<ScheduleJob> getAll() {
        LambdaQueryWrapper<ScheduleJob> lqw = Wrappers.lambdaQuery();
//        lqw.eq(ScheduleJob::getIsDelte, false);
        return dao.selectList(lqw);
    }

}

