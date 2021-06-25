package com.zxkj.goods.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.model.Category;
import feign.hystrix.FallbackFactory;
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
    RespResult<Category> one(@PathVariable(value = "id") Integer id);

}

@Component
class CategoryFeignFallback implements CategoryFeign, FallbackFactory<CategoryFeign> {
    private static final Logger logger = LoggerFactory.getLogger(com.zxkj.goods.feign.CategoryFeignFallback.class);
    private Throwable throwable;

    @Override
    public CategoryFeign create(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public RespResult<Category> one(Integer id) {
        logger.error("CategoryFeignFallback -> one错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

}
