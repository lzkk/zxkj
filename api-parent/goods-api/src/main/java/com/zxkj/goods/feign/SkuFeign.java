package com.zxkj.goods.feign;

import com.zxkj.cart.condition.CartCondition;
import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.model.Sku;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    RespResult<Sku> one(@PathVariable(value = "id") String id);


    /****
     * 根据推广分类查询推广产品列表
     */
    @GetMapping(value = "/sku/aditems/type")
    RespResult<List<Sku>> typeItems(@RequestParam(value = "id") Integer id);

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
class SkuFeignFallback implements SkuFeign, FallbackFactory<SkuFeign> {
    private static final Logger logger = LoggerFactory.getLogger(com.zxkj.goods.feign.SkuFeignFallback.class);
    private Throwable throwable;

    @Override
    public SkuFeign create(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    @Override
    public RespResult dcount(List<CartCondition> carts) {
        logger.error("SkuFeignFallback -> dcount错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult<Sku> one(String id) {
        logger.error("SkuFeignFallback -> one错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult typeItems(Integer id) {
        logger.error("SkuFeignFallback -> typeItems错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult delTypeItems(Integer id) {
        logger.error("SkuFeignFallback -> delTypeItems错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult updateTypeItems(Integer id) {
        logger.error("SkuFeignFallback -> updateTypeItems错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult updateTest(String skuId, String spuId) {
        logger.error("SkuFeignFallback -> updateTest错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

}

