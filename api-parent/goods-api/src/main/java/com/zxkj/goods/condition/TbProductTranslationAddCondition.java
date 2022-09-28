package com.zxkj.goods.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ：yuhui
 * @date ：Created in 2022/9/28 16:50
 */
@Data
public class TbProductTranslationAddCondition {

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("语言编码")
    private String languageCode;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;
}
