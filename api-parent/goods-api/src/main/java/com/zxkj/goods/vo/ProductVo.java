package com.zxkj.goods.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVo {
    // Spu
    private SpuVo spu;
    // Sku
    private List<SkuVo> skus;
}
