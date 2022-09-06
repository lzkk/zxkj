package com.zxkj.canal.model.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author yuhui
 * @version 1.0
 * @desc
 * @date 2022-09-02 16:35:52
 */
@Data
public class AdItemsModel implements Serializable {
    private static final long serialVersionUID = 1L;

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
    @Column(name = "sku_id")
    @ApiModelProperty("展示的产品")
    private String skuId;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
}