package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.goods.model.Product;
import com.zxkj.goods.model.Spu;

public interface SpuService extends IService<Spu> {

    /****
     * 产品保存
     */
    void saveProduct(Product product);

    Product findBySupId(String id);
}
