package com.zxkj.seckill.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/seckill/order")
public class SeckillOrderController {

    @Resource
    private SeckillOrderService seckillOrderService;

    /***
     * 抢单（非热门抢单）
     * @param username
     * @param id
     * @param num
     * @return
     */
    @PostMapping
    public RespResult add(String username, String id, Integer num){
        return RespResult.ok("抢单成功");
    }

}
