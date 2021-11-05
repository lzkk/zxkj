package com.zxkj.search.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SeckillGoodsEsVo implements Serializable {

    private String id;
    private String supId;
    private String skuId;
    private String name;
    private String images;
    private Integer price;
    private Integer seckillPrice;
    private Integer num;
    private Integer storeCount;
    private String activityId;
}

