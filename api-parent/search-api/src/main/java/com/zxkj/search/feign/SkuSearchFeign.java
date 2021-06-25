package com.zxkj.search.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.search.model.SkuEs;
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
@FeignClient(value = ServiceIdConstant.SEARCH_SERVICE, path = "/", fallback = SkuSearchFeignFallback.class)
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
    RespResult add(@RequestBody SkuEs skuEs);

    /***
     * 删除索引
     */
    @DeleteMapping(value = "/search/del/{id}")
    RespResult del(@PathVariable(value = "id") String id);
}


@Component
class SkuSearchFeignFallback implements SkuSearchFeign {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.zxkj.search.feign.SkuSearchFeignFallback.class);

    @Override
    public RespResult<Map<String, Object>> search(Map<String, Object> searchMap) {
        return null;
    }

    @Override
    public RespResult add(SkuEs skuEs) {
        return null;
    }

    @Override
    public RespResult del(String id) {
        return null;
    }
}
