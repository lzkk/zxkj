package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.cart.model.Cart;
import com.zxkj.goods.model.Sku;

import java.util.List;

public interface SKuService extends IService<Sku> {

    int dcount(List<Cart> carts);

    List<Sku> typeSkuItems(Integer id);

    void delTypeSkuItems(Integer id);

    List<Sku> updateTypeSkuItems(Integer id);
}
