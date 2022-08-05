package com.zxkj.seckill.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.feign.fallback.CustomFallbackFactory;
import com.zxkj.common.web.RespResult;
import com.zxkj.seckill.model.SeckillGoods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = ServiceIdConstant.SECKILL_SERVICE, path = "/", fallbackFactory = SeckillGoodsFeignFallback.class)
public interface SeckillGoodsFeign {


    /***
     * 根据活动查询秒杀商品集合
     * @param acid
     * @return
     */
    @GetMapping(value = "/seckill/goods/act/{acid}")
    RespResult<List<SeckillGoods>> actGoods(@PathVariable("acid") String acid);

    /***
     * 根据ID查询秒杀商品详情
     * @param id
     * @return
     */
    @GetMapping(value = "/seckill/goods/{id}")
    RespResult<SeckillGoods> one(@PathVariable("id") String id);
}

@Component
class SeckillGoodsFeignFallback extends CustomFallbackFactory implements SeckillGoodsFeign {
    private static final Logger logger = LoggerFactory.getLogger(com.zxkj.seckill.feign.SeckillGoodsFeignFallback.class);

    @Override
    public RespResult<List<SeckillGoods>> actGoods(String acid) {
        logger.error("SeckillGoodsFeignFallback -> actGoods错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult<SeckillGoods> one(String id) {
        logger.error("SeckillGoodsFeignFallback -> one错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }
}