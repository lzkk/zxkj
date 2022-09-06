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
 * @desc 商品类目
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 16:35:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("分类ID")
    private Integer id;
    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
    /**
     * 上级ID
     */
    @TableField("parent_id")
    @ApiModelProperty("上级ID")
    private Integer parentId;
}