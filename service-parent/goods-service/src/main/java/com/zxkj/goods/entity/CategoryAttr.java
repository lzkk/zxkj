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
@TableName("category_attr")
public class CategoryAttr implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "category_id")
    @ApiModelProperty("")
    private Integer categoryId;
    /**
     * 属性分类表
     */
    @TableField(value = "attr_id")
    @ApiModelProperty("属性分类表")
    private Integer attrId;
}