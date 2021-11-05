package com.zxkj.task.controller;

import com.zxkj.task.bean.Job;
import com.zxkj.task.config.ElasticJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private ElasticJobService elasticJobService;

    @RequestMapping("/add")
    public Object add(@RequestBody Job job) {
        try {
            elasticJobService.processSimpleJob(Job.getInstance(job.getJobName(), job.getJobClass(), job.getCron(), job.getJobParameter(), job.getShardingTotalCount()));
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }

    @RequestMapping("/update")
    public Object update(@RequestBody Job job) {
        try {
            elasticJobService.processSimpleJob(Job.getInstance(job.getJobName(), job.getJobClass(), job.getCron(), job.getJobParameter(), job.getShardingTotalCount()));
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }

    @RequestMapping("/delete")
    public Object delete(@RequestBody Job job) {
        try {
            elasticJobService.removeJob(job.getJobName());
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }
}