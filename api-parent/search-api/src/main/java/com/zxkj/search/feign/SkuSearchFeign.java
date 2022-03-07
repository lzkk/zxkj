package com.zxkj.search.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.hystrix.CustomFallbackFactory;
import com.zxkj.common.web.RespResult;
import com.zxkj.search.condition.SkuEsCondition;
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
@FeignClient(value = ServiceIdConstant.SEARCH_SERVICE, path = "/", fallbackFactory = SkuSearchFeignFallback.class)
public interface SkuSearchFeign {

    /*****
     * 增加索引
     */
    @PostMapping(value = "/search/add")
    RespResult<Boolean> add(@RequestBody SkuEsCondition skuEsCondition);

    /***
     * 删除索引
     */
    @DeleteMapping(value = "/search/del/{id}")
    RespResult<Boolean> del(@PathVariable(value = "id") String id);
}


@Component
class SkuSearchFeignFallback extends CustomFallbackFactory implements SkuSearchFeign {
    private static final Logger logger = LoggerFactory.getLogger(com.zxkj.search.feign.SkuSearchFeignFallback.class);

    @Override
    public RespResult<Boolean> add(SkuEsCondition skuEsCondition) {
        logger.error("SkuSearchFeignFallback -> add错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult<Boolean> del(String id) {
        logger.error("SeckillGoodsSearchFeignFallback -> del错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }
}
