package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.cart.condition.CartCondition;
import com.zxkj.goods.entity.Sku;
import com.zxkj.goods.vo.SkuVo;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 商品表 服务
 * @date 2022-09-02 17:22:02
 */
public interface SkuService extends IService<Sku> {

    /**
     * 商品表 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<Sku> param);

    SkuVo selectOne(String id);

    SkuVo selectOne2(String id);

    int dcount(List<CartCondition> carts);

    List<SkuVo> typeSkuItems(Integer id);

    void delTypeSkuItems(Integer id);

    List<SkuVo> updateTypeSkuItems(Integer id);

    void updateTest(String skuId, String spuId);

}
