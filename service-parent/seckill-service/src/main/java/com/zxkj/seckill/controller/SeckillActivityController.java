package com.zxkj.seckill.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.seckill.service.SeckillActivityService;
import com.zxkj.seckill.vo.SeckillActivityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/seckill/activity")
public class SeckillActivityController {

    @Autowired
    private SeckillActivityService seckillActivityService;

    /****
     * 有效活动列表
     * http://localhost:8092/test/lock
     */
    @GetMapping
    public RespResult list(){
        //查询活动列表
        List<SeckillActivityVo> seckillActivities = seckillActivityService.validActivity();
        return RespResult.ok(seckillActivities);
    }
}
