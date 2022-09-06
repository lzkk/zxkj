package com.zxkj.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yuhui
 * @version 1.0
 * @desc
 * @date 2022-09-02 16:35:52
 */
@Data
public class SkuAttributeVo {

    /**
     * ID
     */
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