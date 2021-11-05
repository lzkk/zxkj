package com.zxkj.cart.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CartVo {

    private String _id;       //主键
    private String userName; //用户名字
    private String name;     //商品名字
    private Integer price;   //单价
    private String image;    //商品图片
    private String skuId;   //商品ID
    private Integer num;    //商品数量

}

