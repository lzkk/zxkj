package com.zxkj.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc 
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 14:00:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("order_refund")
public class OrderRefund implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("订单id")
    private String id;
    /**
     * 订单号
     */
    @TableField("order_no")
    @ApiModelProperty("订单号")
    private String orderNo;
    /**
     * 订单类型
     */
    @TableField("refund_type")
    @ApiModelProperty("订单类型")
    private Integer refundType;
    /**
     * 订单sku的Id
     */
    @TableField("order_sku_id")
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
    @TableField("create_time")
    @ApiModelProperty("退款订单创建时间")
    private Date createTime;
    /**
     * 金额合计
     */
    @ApiModelProperty("金额合计")
    private Integer money;
}