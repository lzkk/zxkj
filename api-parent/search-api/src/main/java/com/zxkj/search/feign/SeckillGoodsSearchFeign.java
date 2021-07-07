package com.zxkj.search.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.search.model.SeckillGoodsEs;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
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
    RespResult add(@RequestBody SeckillGoodsEs seckillGoodsEs);
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
    public RespResult add(SeckillGoodsEs seckillGoodsEs) {
        logger.error("SeckillGoodsSearchFeignFallback -> add错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }
}
