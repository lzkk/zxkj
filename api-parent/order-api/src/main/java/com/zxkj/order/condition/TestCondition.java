package com.zxkj.order.condition;

import com.zxkj.common.condition.MobileCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * @Description: 「购物车」操作购物车 Condition
 */
@Data
public class TestCondition extends MobileCondition implements Serializable {

    @ApiModelProperty(value = "商品数量")
    @NotNull(message = "商品数量不能为空")
    private Integer prodNum;

    @ApiModelProperty(value = "订单编号")
    @NotNull(message = "订单编号不能为空")
    private String orderNo;

}
