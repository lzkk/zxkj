package com.zxkj.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @desc 品牌表
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 16:35:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("brand")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("品牌id")
    private Integer id;
    /**
     * 品牌名称
     */
    @ApiModelProperty("品牌名称")
    private String name;
    /**
     * 品牌图片地址
     */
    @ApiModelProperty("品牌图片地址")
    private String image;
    /**
     * 品牌的首字母
     */
    @ApiModelProperty("品牌的首字母")
    private String initial;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
}