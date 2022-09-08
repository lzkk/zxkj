package com.zxkj.goods.controller;

import com.zxkj.cart.condition.CartCondition;
import com.zxkj.common.datasource.support.Readonly;
import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.web.JsonUtil;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.common.exception.GoodsExceptionCodes;
import com.zxkj.goods.feign.SkuFeign;
import com.zxkj.goods.service.SkuService;
import com.zxkj.goods.vo.SkuVo;
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
    private SkuService skuService;

    /***
     * 库存递减
     */
    public RespResult<Integer> dcount(@RequestBody List<CartCondition> carts) {
        return RespResult.ok(skuService.dcount(carts));
    }

    /***
     * 根据ID查询商品详情
     * @return
     */
    public RespResult<SkuVo> one(@PathVariable(value = "id") String id) {
        SkuVo sku = skuService.selectOne(id);
        if (sku == null) {
            throw new BusinessException("无效的skuId:" + id);
        } else {
            if (sku.getId().endsWith("88")) {
                throw new BusinessException(GoodsExceptionCodes.CODE_10001);
            }
        }
        log.info("sku:" + JsonUtil.toJsonString(sku));
        return RespResult.ok(sku);
    }


    /***
     * 根据ID查询商品详情
     * @return
     */
    @Readonly
    public RespResult<SkuVo> one2(@PathVariable(value = "id") String id) {
        SkuVo sku = skuService.selectOne2(id);
        log.info("sku:" + JsonUtil.toJsonString(sku));
        return RespResult.ok(sku);
    }

    /****
     * 根据推广分类查询推广产品列表
     *
     */
    public RespResult<List<SkuVo>> typeItems(@RequestParam(value = "id") Integer id) {
        //查询
        List<SkuVo> skus = skuService.typeSkuItems(id);
        return RespResult.ok(skus);
    }

    /****
     * 根据推广分类查询推广产品列表
     */
    public RespResult delTypeItems(@RequestParam(value = "id") Integer id) {
        skuService.delTypeSkuItems(id);
        return RespResult.ok();
    }

    /****
     * 根据推广分类查询推广产品列表
     *
     */
    public RespResult updateTypeItems(@RequestParam(value = "id") Integer id) {
        //修改
        skuService.updateTypeSkuItems(id);
        return RespResult.ok();
    }

    public RespResult updateTest(@RequestParam(value = "skuId") String skuId, @RequestParam(value = "spuId") String spuId) {
        //修改
        skuService.updateTest(skuId, spuId);
        return RespResult.ok();
    }

}
