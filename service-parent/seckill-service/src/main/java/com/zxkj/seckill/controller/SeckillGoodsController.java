package com.zxkj.seckill.controller;

import com.zxkj.common.web.JsonUtil;
import com.zxkj.common.web.RespResult;
import com.zxkj.seckill.feign.SeckillGoodsFeign;
import com.zxkj.seckill.model.SeckillGoods;
import com.zxkj.seckill.service.SeckillGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class SeckillGoodsController implements SeckillGoodsFeign {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    /***
     * 根据活动查询秒杀商品集合
     * @param acid
     * @return
     */
    public RespResult<List<SeckillGoods>> actGoods(@PathVariable("acid") String acid) {
        List<SeckillGoods> seckillGoods = seckillGoodsService.actGoods(acid);
        return RespResult.ok(seckillGoods);
    }

    /***
     * 根据ID查询秒杀商品详情
     * @param id
     * @return
     */
    public RespResult<SeckillGoods> one(@PathVariable("id") String id) {
        SeckillGoods seckillGoods = seckillGoodsService.getById(id);
        log.info("seckillGoods--" + JsonUtil.toJsonString(seckillGoods));
        return RespResult.ok(seckillGoods);
    }

}
