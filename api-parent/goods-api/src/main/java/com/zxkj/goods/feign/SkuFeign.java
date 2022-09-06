package com.zxkj.goods.feign;

import com.zxkj.cart.condition.CartCondition;
import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.feign.fallback.CustomFallbackFactory;
import com.zxkj.goods.vo.SkuVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = ServiceIdConstant.GOODS_SERVICE, path = "/", fallbackFactory = SkuFeignFallback.class)    //服务名字
public interface SkuFeign {

    /***
     * 库存递减
     */
    @PostMapping(value = "/sku/dcount")
    RespResult<Integer> dcount(@RequestBody List<CartCondition> carts);

    /***
     * 根据ID查询商品详情
     * @return
     */
    @GetMapping(value = "/sku/{id}")
    RespResult<SkuVo> one(@PathVariable(value = "id") String id);

    /***
     * 根据ID查询商品详情
     * @return
     */
    @GetMapping(value = "/sku2/{id}")
    RespResult<SkuVo> one2(@PathVariable(value = "id") String id);


    /****
     * 根据推广分类查询推广产品列表
     */
    @GetMapping(value = "/sku/aditems/type")
    RespResult<List<SkuVo>> typeItems(@RequestParam(value = "id") Integer id);

    /****
     * 根据推广分类查询推广产品列表
     *
     */
    @DeleteMapping(value = "/sku/aditems/type")
    RespResult delTypeItems(@RequestParam(value = "id") Integer id);

    /****
     * 根据推广分类查询推广产品列表
     */
    @PutMapping(value = "/sku/aditems/type")
    RespResult updateTypeItems(@RequestParam(value = "id") Integer id);

    /****
     * 根据推广分类查询推广产品列表
     */
    @PutMapping(value = "/sku/skuUpdateTest")
    RespResult updateTest(@RequestParam(value = "skuId") String skuId, @RequestParam(value = "spuId") String spuId);

}

@Component
class SkuFeignFallback extends CustomFallbackFactory implements SkuFeign {

    @Override
    public RespResult dcount(List<CartCondition> carts) {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult<SkuVo> one(String id) {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult<SkuVo> one2(String id) {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult typeItems(Integer id) {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult delTypeItems(Integer id) {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult updateTypeItems(Integer id) {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult updateTest(String skuId, String spuId) {
        return RespResult.error(throwable.getMessage());
    }

}

