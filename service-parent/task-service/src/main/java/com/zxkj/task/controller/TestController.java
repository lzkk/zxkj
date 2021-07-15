package com.zxkj.task.controller;

import com.zxkj.task.bean.Job;
import com.zxkj.task.config.ElasticJobService;
import com.zxkj.task.job.MyJob3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private ElasticJobService elasticJobService;

    @RequestMapping("/add")
    public Object add() {
        try {
            elasticJobService.processSimpleJob(new Job("myJob3", "0/5 * * * * ? *", "1-3", 2, MyJob3.class.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }

    @RequestMapping("/update")
    public Object update() {
        try {
            elasticJobService.processSimpleJob(new Job("myJob3", "0/3 * * * * ? *", "1-3", 2, MyJob3.class.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }

    @RequestMapping("/delete")
    public Object delete() {
        try {
            elasticJobService.deleteJob("myJob3");
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }
}