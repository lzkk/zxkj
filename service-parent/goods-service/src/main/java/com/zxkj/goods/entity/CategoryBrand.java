package com.zxkj.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("category_brand")
public class CategoryBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableField(value = "category_id")
    @ApiModelProperty("分类ID")
    private Integer categoryId;
    /**
     * 品牌ID
     */
    @TableField(value = "brand_id")
    @ApiModelProperty("品牌ID")
    private Integer brandId;
}