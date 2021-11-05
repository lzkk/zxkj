package com.zxkj.cart.service.impl;

import com.zxkj.cart.model.Cart;
import com.zxkj.cart.service.CartService;
import com.zxkj.cart.vo.CartVo;
import com.zxkj.common.mongo.base.BaseMongoImpl;
import com.zxkj.common.util.url.BeanUtil;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.feign.SkuFeign;
import com.zxkj.goods.model.Sku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CartServiceImpl extends BaseMongoImpl<Cart> implements CartService {

    @Autowired
    private SkuFeign skuFeign;

    /***
     * 根据集合ID删除指定的购物车列表
     */
    @Override
    public void delete(List<String> ids) {
        deleteByCondition(Query.query(Criteria.where("_id").in(ids)));
    }

    /***
     * 加入购物车
     * @param id
     * @param userName
     * @param num:当前商品加入购物车总数量
     * @return
     */
    @Override
    public void add(String id, String userName, Integer num) {
        //ID 不能冲突
        //1)删除当前ID对应的商品之前的购物车记录
        deleteById(userName + id);

        if (num > 0) {
            //2）根据ID查询Sku详情
            RespResult<Sku> skuResp = skuFeign.one(id);

            //3)将当前ID商品对应的数据加入购物车（存入到MongoDB）
            Sku sku = skuResp.getResult();
            Cart cart = new Cart(userName + id, userName, sku.getName(), sku.getPrice(), sku.getImage(), id, num);
            save(cart);
        }
    }

    /***
     * 查询指定购物车ID集合的列表
     */
    @Override
    public List<CartVo> list(List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return null;
        }
        //根据ID集合查询
        List<Cart> cartList = findByCondition(Query.query(Criteria.where("_id").in(ids)));
        return BeanUtil.copyList(cartList, CartVo.class);
    }

    /***
     * 购物车列表
     */
    @Override
    public List<CartVo> list(String userName) {
        //条件构建
        Query query = Query.query(Criteria.where("userName").is(userName));
        query.with(Sort.by("_id"));
        log.info("query：" + query.toString());
        //根据ID集合查询
        List<Cart> cartList = findByCondition(query);
        return BeanUtil.copyList(cartList, CartVo.class);
    }

}
