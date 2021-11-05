package com.zxkj.search.vo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class SkuEsVo {

    private String id;
    private String name;
    private Integer price;
    private Integer num;
    private String image;
    private String images;
    private Date createTime;
    private Date updateTime;
    private String spuId;
    private Integer categoryId;
    private String categoryName;
    private Integer brandId;
    private String brandName;
    private String skuAttribute;
    private Integer status;

    //属性映射(动态创建域信息)
    //key=就业薪资
    //value=1万
    //attrMap.就业薪资.keyword=1万
    private Map<String, String> attrMap;
}