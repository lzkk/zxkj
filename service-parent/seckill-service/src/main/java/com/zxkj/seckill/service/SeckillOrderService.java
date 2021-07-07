package com.zxkj.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.seckill.model.SeckillOrder;

import java.util.Map;

public interface SeckillOrderService extends IService<SeckillOrder> {


    /***
     * 抢单操作
     */
    int add(Map<String, Object> dataMap);
}
