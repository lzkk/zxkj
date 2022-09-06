package com.zxkj.seckill.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.feign.fallback.CustomFallbackFactory;
import com.zxkj.seckill.vo.SeckillGoodsVo;
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
    RespResult<List<SeckillGoodsVo>> actGoods(@PathVariable("acid") String acid);

    /***
     * 根据ID查询秒杀商品详情
     * @param id
     * @return
     */
    @GetMapping(value = "/seckill/goods/{id}")
    RespResult<SeckillGoodsVo> one(@PathVariable("id") String id);
}

@Component
class SeckillGoodsFeignFallback extends CustomFallbackFactory implements SeckillGoodsFeign {

    @Override
    public RespResult<List<SeckillGoodsVo>> actGoods(String acid) {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult<SeckillGoodsVo> one(String id) {
        return RespResult.error(throwable.getMessage());
    }
}