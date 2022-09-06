package com.zxkj.goods.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yuhui
 * @version 1.0
 * @desc
 * @date 2022-09-02 16:35:52
 */
@Data
public class SpuCondition {

    /**
     * 主键
     */
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
    @ApiModelProperty("品牌ID")
    private Integer brandId;
    /**
     * 一级分类
     */
    @ApiModelProperty("一级分类")
    private Integer categoryOneId;
    /**
     * 二级分类
     */
    @ApiModelProperty("二级分类")
    private Integer categoryTwoId;
    /**
     * 三级分类
     */
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
    @ApiModelProperty("规格列表")
    private String attributeList;
    /**
     * 是否上架,0已下架，1已上架
     */
    @ApiModelProperty("是否上架,0已下架，1已上架")
    private Integer isMarketable;
    /**
     * 是否删除,0:未删除，1：已删除
     */
    @ApiModelProperty("是否删除,0:未删除，1：已删除")
    private Integer isDelete;
    /**
     * 审核状态，0：未审核，1：已审核，2：审核不通过
     */
    @ApiModelProperty("审核状态，0：未审核，1：已审核，2：审核不通过")
    private Integer status;
}