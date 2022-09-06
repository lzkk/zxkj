package com.zxkj.goods.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.feign.fallback.CustomFallbackFactory;
import com.zxkj.goods.vo.CategoryVo;
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
@FeignClient(value = ServiceIdConstant.GOODS_SERVICE, path = "/", fallbackFactory = CategoryFeignFallback.class)
public interface CategoryFeign {

    /****
     * 根据分类查询分类信息
     */
    @GetMapping(value = "/category/{id}")
    RespResult<CategoryVo> one(@PathVariable(value = "id") Integer id);

}

@Component
class CategoryFeignFallback extends CustomFallbackFactory implements CategoryFeign {
    protected static final Logger logger = LoggerFactory.getLogger(CategoryFeignFallback.class);

    @Override
    public RespResult<CategoryVo> one(Integer id) {
        return RespResult.error(throwable.getMessage());
    }

}
