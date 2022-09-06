package com.zxkj.canal.model.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author yuhui
 * @version 1.0
 * @desc 商品表
 * @date 2022-09-02 16:35:52
 */
@Data
public class SkuModel {

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private String id;
    /**
     * SKU名称
     */
    @ApiModelProperty("SKU名称")
    private String name;
    /**
     * 价格（分）
     */
    @ApiModelProperty("价格（分）")
    private Integer price;
    /**
     * 库存数量
     */
    @ApiModelProperty("库存数量")
    private Integer num;
    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String image;
    /**
     * 商品图片列表
     */
    @ApiModelProperty("商品图片列表")
    private String images;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * SPUID
     */
    @Column(name = "spu_id")
    @ApiModelProperty("SPUID")
    private String spuId;
    /**
     * 类目ID
     */
    @Column(name = "category_id")
    @ApiModelProperty("类目ID")
    private Integer categoryId;
    /**
     * 类目名称
     */
    @Column(name = "category_name")
    @ApiModelProperty("类目名称")
    private String categoryName;
    /**
     * 品牌id
     */
    @Column(name = "brand_id")
    @ApiModelProperty("品牌id")
    private Integer brandId;
    /**
     * 品牌名称
     */
    @Column(name = "brand_name")
    @ApiModelProperty("品牌名称")
    private String brandName;
    /**
     * 规格
     */
    @Column(name = "sku_attribute")
    @ApiModelProperty("规格")
    private String skuAttribute;
    /**
     * 商品状态 1-正常，2-下架，3-删除
     */
    @ApiModelProperty("商品状态 1-正常，2-下架，3-删除")
    private Integer status;
}