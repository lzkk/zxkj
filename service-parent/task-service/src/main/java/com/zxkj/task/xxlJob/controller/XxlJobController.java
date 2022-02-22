package com.zxkj.task.xxlJob.controller;

import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.zxkj.common.web.RespResult;
import com.zxkj.config.XxlJobConfig;
import com.zxkj.util.XxlHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/xxlJob")
public class XxlJobController {

    @Autowired
    private XxlJobConfig jobConfig;

    @RequestMapping("/addGroup")
    public RespResult<Boolean> addGroup(@RequestBody XxlJobGroup xxlJobGroup) {
        XxlJobGroup jobGroup = XxlHttpClientUtil.getJobGroup(jobConfig.getAdminAddresses(), xxlJobGroup.getAppname());
        if (jobGroup == null) {
            String res = XxlHttpClientUtil.createJobGroup(jobConfig.getAdminAddresses(), xxlJobGroup.getAppname());
            log.info("----addGroup:{},result:{}", xxlJobGroup.getAppname(), res);
        } else {
            log.info("----JobGroupExist:{}", xxlJobGroup.getAppname());
        }
        return RespResult.ok(true);
    }

    @RequestMapping("/addJob")
    public RespResult<Boolean> addJob(@RequestBody XxlJobInfo jobInfo) {
        int resId = XxlHttpClientUtil.createJobInfo(jobConfig.getAdminAddresses(), jobInfo.getJobGroup(), jobInfo.getExecutorHandler(), jobInfo.getScheduleConf(), jobInfo.getExecutorParam(), jobInfo.getJobDesc());
        log.info("----addJobRes:{}", resId);
        return RespResult.ok(true);
    }

}