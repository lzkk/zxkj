package com.zxkj.goods.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.feign.fallback.CustomFallbackFactory;
import com.zxkj.goods.vo.ProductVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = ServiceIdConstant.GOODS_SERVICE, path = "/", fallbackFactory = SpuFeignFallback.class)    //服务名字
public interface SpuFeign {

    /***
     * 查询Product
     */
    @GetMapping(value = "/spu/product/{id}")
    RespResult<ProductVo> one(@PathVariable(value = "id") String id);
}


@Component
class SpuFeignFallback extends CustomFallbackFactory implements SpuFeign {
    protected static final Logger logger = LoggerFactory.getLogger(SpuFeignFallback.class);

    @Override
    public RespResult<ProductVo> one(String id) {
        return RespResult.error(throwable.getMessage());
    }


}