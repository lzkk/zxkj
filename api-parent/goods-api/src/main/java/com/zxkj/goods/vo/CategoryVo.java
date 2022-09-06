package com.zxkj.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yuhui
 * @version 1.0
 * @desc 商品类目
 * @date 2022-09-02 16:35:52
 */
@Data
public class CategoryVo {

    /**
     * 分类ID
     */
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
    @ApiModelProperty("上级ID")
    private Integer parentId;
}