package com.zxkj.cart.feign;

import com.zxkj.cart.vo.CartVo;
import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.feign.fallback.CustomFallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    RespResult<List<CartVo>> list(@RequestBody List<String> ids);
}

@Component
class CartFeignFallback extends CustomFallbackFactory implements CartFeign {

    @Override
    public RespResult delete(List<String> ids) {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult<List<CartVo>> list(List<String> ids) {
        return RespResult.error(throwable.getMessage());
    }
}

