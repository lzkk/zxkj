package com.zxkj.cart.controller;

import com.zxkj.cart.feign.CartFeign;
import com.zxkj.cart.service.CartService;
import com.zxkj.cart.vo.CartVo;
import com.zxkj.common.web.JsonUtil;
import com.zxkj.common.web.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
@Slf4j
public class CartController implements CartFeign {

    @Autowired
    private CartService cartService;

    /***
     * 删除购物车数据
     */
    public RespResult delete(@RequestBody List<String> ids) {
        cartService.delete(ids);
        return RespResult.ok();
    }


    /****
     * 增加购物车方法
     */
    @PostMapping(value = "/cart/{id}/{num}")
    public RespResult add(@PathVariable(value = "id") String id,
                          @PathVariable(value = "num") Integer num) {
        String userName = "gp";
        cartService.add(id, userName, num);
        return RespResult.ok();
    }

    /***
     * 指定ID集合的购物车数据
     * http://localhost:8087/cart/list
     */
    public RespResult<List<CartVo>> list(@RequestBody List<String> ids) {
        List<CartVo> cartVoList = cartService.list(ids);
        log.info("carts--" + JsonUtil.jsonFromObject(cartVoList));
        return RespResult.ok(cartVoList);
    }


    /****
     * 购物车列表
     */
    @GetMapping(value = "/cart/list")
    public RespResult<List<CartVo>> list() {
        String userName = "gp";
        List<CartVo> list = cartService.list(userName);
        return RespResult.ok(list);
    }
}
