package com.zxkj.goods.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCondition {
    // Spu
    private SpuCondition spu;
    // Sku
    private List<SkuCondition> skus;
}
