package com.zxkj.search.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.search.condition.SeckillGoodsEsCondition;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = ServiceIdConstant.SEARCH_SERVICE, path = "/", fallbackFactory = SeckillGoodsSearchFeignFallback.class)
public interface SeckillGoodsSearchFeign {


    /***
     * 将秒杀商品导入索引库
     */
    @PostMapping("/seckill/goods/import")
    RespResult<Boolean> add(@RequestBody SeckillGoodsEsCondition seckillGoodsEsCondition);

    /***
     * 删除索引
     */
    @DeleteMapping(value = "/seckill/del/{id}")
    RespResult<Boolean> del(@PathVariable(value = "id") String id);
}

@Component
class SeckillGoodsSearchFeignFallback implements SeckillGoodsSearchFeign, FallbackFactory<SeckillGoodsSearchFeign> {
    private static final Logger logger = LoggerFactory.getLogger(com.zxkj.search.feign.SeckillGoodsSearchFeignFallback.class);
    private Throwable throwable;

    @Override
    public SeckillGoodsSearchFeign create(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public RespResult<Boolean> add(SeckillGoodsEsCondition seckillGoodsEsCondition) {
        logger.error("SeckillGoodsSearchFeignFallback -> add错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult<Boolean> del(String id) {
        logger.error("SeckillGoodsSearchFeignFallback -> del错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }
}
