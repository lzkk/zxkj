package com.zxkj.task.controller;

import com.zxkj.task.config.DynamicTaskCreate;
import com.zxkj.task.job.MyJob2;
import com.zxkj.task.job.MyJob3;
import com.zxkj.task.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {

    @Autowired
    private DynamicTaskCreate dynamicTaskCreate;

    @RequestMapping("/add")
    public Object add() {
        try {
            dynamicTaskCreate.create("myTestJob3", "0/10 * * * * ? *", 3, new MyJob3(), "11-20");
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return "success";
    }
}