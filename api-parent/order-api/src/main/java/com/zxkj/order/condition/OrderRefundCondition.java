package com.zxkj.order.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yuhui
 * @version 1.0
 * @desc
 * @date 2022-09-05 14:00:31
 */
@Data
public class OrderRefundCondition {

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private String id;
    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;
    /**
     * 订单类型
     */
    @ApiModelProperty("订单类型")
    private Integer refundType;
    /**
     * 订单sku的Id
     */
    @ApiModelProperty("订单sku的Id")
    private String orderSkuId;
    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String username;
    /**
     * 退款状态
     */
    @ApiModelProperty("退款状态")
    private Integer status;
    /**
     * 退款订单创建时间
     */
    @ApiModelProperty("退款订单创建时间")
    private Date createTime;
    /**
     * 金额合计
     */
    @ApiModelProperty("金额合计")
    private Integer money;
}