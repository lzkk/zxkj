package com.zxkj.cart.service;

import com.zxkj.cart.vo.CartVo;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
public interface CartService {

    /***
     * 根据集合ID删除指定的购物车列表
     */
    void delete(List<String> ids);

    /***
     * 加入购物车
     */
    void add(String id, String userName, Integer num);

    /***
     * 查询指定购物车ID集合的列表
     */
    List<CartVo> list(List<String> ids);

    /***
     * 购物车列表
     */
    List<CartVo> list(String userName);


}
