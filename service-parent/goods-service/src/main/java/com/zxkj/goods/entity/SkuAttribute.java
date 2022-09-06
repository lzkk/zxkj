package com.zxkj.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("sku_attribute")
public class SkuAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;
    /**
     * 属性名称
     */
    @ApiModelProperty("属性名称")
    private String name;
    /**
     * 属性选项
     */
    @ApiModelProperty("属性选项")
    private String options;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
}