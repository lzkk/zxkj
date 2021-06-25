package com.zxkj.cart.feign;

import com.zxkj.cart.model.Cart;
import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = ServiceIdConstant.CART_SERVICE, path = "/", fallbackFactory = CartFeignFallback.class)
public interface CartFeign {

    /***
     * 删除购物车数据
     */
    @DeleteMapping(value = "/cart")
    RespResult delete(@RequestBody List<String> ids);


    /***
     * 指定ID集合的购物车数据
     * http://localhost:8087/cart/list
     */
    @PostMapping(value = "/cart/list/ids")
    RespResult<List<Cart>> list(@RequestBody List<String> ids);
}

@Component
class CartFeignFallback implements CartFeign, FallbackFactory<CartFeign> {
    private static final Logger logger = LoggerFactory.getLogger(com.zxkj.cart.feign.CartFeignFallback.class);
    private Throwable throwable;

    @Override
    public CartFeign create(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public RespResult delete(List<String> ids) {
        logger.error("CartFeignFallback -> delete错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult<List<Cart>> list(List<String> ids) {
        logger.error("CartFeignFallback -> list错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }
}

