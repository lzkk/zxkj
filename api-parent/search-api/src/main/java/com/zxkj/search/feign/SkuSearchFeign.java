package com.zxkj.search.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.search.condition.SkuEsCondition;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = ServiceIdConstant.SEARCH_SERVICE, path = "/", fallbackFactory = SkuSearchFeignFallback.class)
public interface SkuSearchFeign {

    /***
     * 商品搜索
     */
    @GetMapping("/search")
    RespResult<Map<String, Object>> search(@RequestParam(required = false) Map<String, Object> searchMap);

    /*****
     * 增加索引
     */
    @PostMapping(value = "/search/add")
    RespResult add(@RequestBody SkuEsCondition skuEsCondition);

    /***
     * 删除索引
     */
    @DeleteMapping(value = "/search/del/{id}")
    RespResult del(@PathVariable(value = "id") String id);
}


@Component
class SkuSearchFeignFallback implements SkuSearchFeign, FallbackFactory<SkuSearchFeign> {
    private static final Logger logger = LoggerFactory.getLogger(com.zxkj.search.feign.SkuSearchFeignFallback.class);

    private Throwable throwable;

    @Override
    public SkuSearchFeign create(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public RespResult<Map<String, Object>> search(Map<String, Object> searchMap) {
        logger.error("SkuSearchFeignFallback -> search错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult add(SkuEsCondition skuEsCondition) {
        logger.error("SkuSearchFeignFallback -> add错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult del(String id) {
        logger.error("SeckillGoodsSearchFeignFallback -> del错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }
}
