package com.zxkj.goods.entity;

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
 * @date 2022-09-02 16:35:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("spu")
public class Spu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private String id;
    /**
     * SPU名
     */
    @ApiModelProperty("SPU名")
    private String name;
    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String intro;
    /**
     * 品牌ID
     */
    @TableField("brand_id")
    @ApiModelProperty("品牌ID")
    private Integer brandId;
    /**
     * 一级分类
     */
    @TableField("category_one_id")
    @ApiModelProperty("一级分类")
    private Integer categoryOneId;
    /**
     * 二级分类
     */
    @TableField("category_two_id")
    @ApiModelProperty("二级分类")
    private Integer categoryTwoId;
    /**
     * 三级分类
     */
    @TableField("category_three_id")
    @ApiModelProperty("三级分类")
    private Integer categoryThreeId;
    /**
     * 图片列表
     */
    @ApiModelProperty("图片列表")
    private String images;
    /**
     * 售后服务
     */
    @TableField("after_sales_service")
    @ApiModelProperty("售后服务")
    private String afterSalesService;
    /**
     * 介绍
     */
    @ApiModelProperty("介绍")
    private String content;
    /**
     * 规格列表
     */
    @TableField("attribute_list")
    @ApiModelProperty("规格列表")
    private String attributeList;
    /**
     * 是否上架,0已下架，1已上架
     */
    @TableField("is_marketable")
    @ApiModelProperty("是否上架,0已下架，1已上架")
    private Integer isMarketable;
    /**
     * 是否删除,0:未删除，1：已删除
     */
    @TableField("is_delete")
    @ApiModelProperty("是否删除,0:未删除，1：已删除")
    private Integer isDelete;
    /**
     * 审核状态，0：未审核，1：已审核，2：审核不通过
     */
    @ApiModelProperty("审核状态，0：未审核，1：已审核，2：审核不通过")
    private Integer status;
}