package com.zxkj.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc 商品表
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 16:35:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sku")
public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * SPUID
     */
    @TableField("spu_id")
    @ApiModelProperty("SPUID")
    private String spuId;
    /**
     * 类目ID
     */
    @TableField("category_id")
    @ApiModelProperty("类目ID")
    private Integer categoryId;
    /**
     * 类目名称
     */
    @TableField("category_name")
    @ApiModelProperty("类目名称")
    private String categoryName;
    /**
     * 品牌id
     */
    @TableField("brand_id")
    @ApiModelProperty("品牌id")
    private Integer brandId;
    /**
     * 品牌名称
     */
    @TableField("brand_name")
    @ApiModelProperty("品牌名称")
    private String brandName;
    /**
     * 规格
     */
    @TableField("sku_attribute")
    @ApiModelProperty("规格")
    private String skuAttribute;
    /**
     * 商品状态 1-正常，2-下架，3-删除
     */
    @ApiModelProperty("商品状态 1-正常，2-下架，3-删除")
    private Integer status;
}