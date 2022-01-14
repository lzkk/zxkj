package com.zxkj.goods.controller;

import com.zxkj.cart.condition.CartCondition;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.feign.SkuFeign;
import com.zxkj.goods.model.Sku;
import com.zxkj.goods.service.SKuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class SkuController implements SkuFeign {

    @Autowired
    private SKuService sKuService;

    /***
     * 库存递减
     */
    public RespResult<Integer> dcount(@RequestBody List<CartCondition> carts) {
        return RespResult.ok(sKuService.dcount(carts));
    }

    /***
     * 根据ID查询商品详情
     * @return
     */
    public RespResult<Sku> one(@PathVariable(value = "id") String id) {
        Sku sku = sKuService.getById(id);
        return RespResult.ok(sku);
    }

    /****
     * 根据推广分类查询推广产品列表
     *
     */
    public RespResult<List<Sku>> typeItems(@RequestParam(value = "id") Integer id) {
        //查询
        List<Sku> skus = sKuService.typeSkuItems(id);
        return RespResult.ok(skus);
    }

    /****
     * 根据推广分类查询推广产品列表
     */
    public RespResult delTypeItems(@RequestParam(value = "id") Integer id) {
        sKuService.delTypeSkuItems(id);
        return RespResult.ok();
    }

    /****
     * 根据推广分类查询推广产品列表
     *
     */
    public RespResult updateTypeItems(@RequestParam(value = "id") Integer id) {
        //修改
        sKuService.updateTypeSkuItems(id);
        return RespResult.ok();
    }

    public RespResult updateTest(@RequestParam(value = "skuId") String skuId, @RequestParam(value = "spuId") String spuId) {
        //修改
        sKuService.updateTest(skuId, spuId);
        return RespResult.ok();
    }

}
