package com.zxkj.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @desc 
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 13:47:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("order_sku")
public class OrderSku implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("ID")
    private String id;
    /**
     * 1级分类
     */
    @TableField("category_one_id")
    @ApiModelProperty("1级分类")
    private Integer categoryOneId;
    /**
     * 2级分类
     */
    @TableField("category_two_id")
    @ApiModelProperty("2级分类")
    private Integer categoryTwoId;
    /**
     * 3级分类
     */
    @TableField("category_three_id")
    @ApiModelProperty("3级分类")
    private Integer categoryThreeId;
    /**
     * SPU_ID
     */
    @TableField("spu_id")
    @ApiModelProperty("SPU_ID")
    private String spuId;
    /**
     * SKU_ID
     */
    @TableField("sku_id")
    @ApiModelProperty("SKU_ID")
    private String skuId;
    /**
     * 订单ID
     */
    @TableField("order_id")
    @ApiModelProperty("订单ID")
    private String orderId;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;
    /**
     * 单价
     */
    @ApiModelProperty("单价")
    private Integer price;
    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer num;
    /**
     * 总金额
     */
    @ApiModelProperty("总金额")
    private Integer money;
    /**
     * 图片地址
     */
    @ApiModelProperty("图片地址")
    private String image;
}