package com.zxkj.page.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.hystrix.CustomFallbackFactory;
import com.zxkj.common.web.RespResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = ServiceIdConstant.PAGE_WEB_SERVICE, path = "/", fallbackFactory = SeckillPageFeignFallback.class)
public interface SeckillPageFeign {

    /***
     * 生成秒杀商品详情页
     */
    @GetMapping(value = "/page/seckill/goods/{id}")
    RespResult page(@PathVariable("id") String id) throws Exception;

    /***
     * 删除指定活动的页面
     */
    @DeleteMapping(value = "/page/seckill/goods/{acid}")
    RespResult deleByAct(@PathVariable("acid") String acid);
}

@Component
class SeckillPageFeignFallback extends CustomFallbackFactory implements SeckillPageFeign {
    private static final Logger logger = LoggerFactory.getLogger(SeckillPageFeignFallback.class);

    @Override
    public RespResult page(String id) throws Exception {
        logger.error("SeckillPageFeignFallback -> page错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult deleByAct(String acid) {
        logger.error("SeckillPageFeignFallback -> deleByAct错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }
}
