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
@TableName("ad_items")
public class AdItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("")
    private String name;
    /**
     * 分类，1首页推广,2列表页推广
     */
    @ApiModelProperty("分类，1首页推广,2列表页推广")
    private Integer type;
    /**
     * 展示的产品
     */
    @TableField("sku_id")
    @ApiModelProperty("展示的产品")
    private String skuId;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
}