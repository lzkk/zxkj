package com.zxkj.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yuhui
 * @version 1.0
 * @desc 品牌表
 * @date 2022-09-02 16:35:52
 */
@Data
public class BrandVo {

    /**
     * 品牌id
     */
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