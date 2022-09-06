package com.zxkj.order.condition;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderSkuCondition implements Serializable {

    private String id;
    private String payType;
    private String username;
    private Integer orderStatus;
    private Integer payStatus;
    private String skuId;
    private String name;
    private Integer price;
    private Integer num;    //数量
    private Integer money;  //总金额

}
