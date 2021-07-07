package com.zxkj.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.seckill.model.SeckillGoods;

import java.util.List;

public interface SeckillGoodsService extends IService<SeckillGoods> {


    //热门商品分离  uri:商品ID
    void isolation(String uri);

    //根据活动ID查询商品信息
    List<SeckillGoods> actGoods(String acid);
}
