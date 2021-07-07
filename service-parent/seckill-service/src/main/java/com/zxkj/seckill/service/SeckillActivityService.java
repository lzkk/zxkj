package com.zxkj.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.seckill.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityService extends IService<SeckillActivity>{

    /***
     * 有效活动列表
     * @return
     */
    List<SeckillActivity> validActivity();
 
}
