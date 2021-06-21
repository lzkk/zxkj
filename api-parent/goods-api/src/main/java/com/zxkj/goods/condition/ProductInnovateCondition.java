package com.zxkj.goods.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInnovateCondition {

    private Integer id;

    private String code;

    private String name;

    private String grp;

}