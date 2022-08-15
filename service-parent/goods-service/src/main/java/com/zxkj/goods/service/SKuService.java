package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.cart.condition.CartCondition;
import com.zxkj.goods.model.Sku;

import java.util.List;

public interface SKuService extends IService<Sku> {

    Sku selectOne(String id);

    Sku selectOne2(String id);

    int dcount(List<CartCondition> carts);

    List<Sku> typeSkuItems(Integer id);

    void delTypeSkuItems(Integer id);

    List<Sku> updateTypeSkuItems(Integer id);

    void updateTest(String skuId, String spuId);
}
