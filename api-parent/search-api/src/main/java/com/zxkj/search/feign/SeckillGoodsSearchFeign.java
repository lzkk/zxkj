package com.zxkj.search.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.search.model.SeckillGoodsEs;
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
@FeignClient(value = ServiceIdConstant.SEARCH_SERVICE, path = "/", fallback = SeckillGoodsSearchFeignFallback.class)
public interface SeckillGoodsSearchFeign {


    /***
     * 将秒杀商品导入索引库
     */
    @PostMapping("/seckill/goods/import")
    RespResult add(@RequestBody SeckillGoodsEs seckillGoodsEs);
}

@Component
class SeckillGoodsSearchFeignFallback implements SeckillGoodsSearchFeign {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.zxkj.search.feign.SeckillGoodsSearchFeignFallback.class);

    @Override
    public RespResult add(SeckillGoodsEs seckillGoodsEs) {
        return null;
    }
}
