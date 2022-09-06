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
 * @date 2022-09-05 13:47:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("order_info")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("订单id")
    private String id;
    /**
     * 数量合计
     */
    @TableField("total_num")
    @ApiModelProperty("数量合计")
    private Integer totalNum;
    /**
     * 金额合计
     */
    @ApiModelProperty("金额合计")
    private Integer moneys;
    /**
     * 支付类型，1、在线支付、0 货到付款
     */
    @TableField("pay_type")
    @ApiModelProperty("支付类型，1、在线支付、0 货到付款")
    private String payType;
    /**
     * 订单创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("订单创建时间")
    private Date createTime;
    /**
     * 订单更新时间
     */
    @TableField("update_time")
    @ApiModelProperty("订单更新时间")
    private Date updateTime;
    /**
     * 付款时间
     */
    @TableField("pay_time")
    @ApiModelProperty("付款时间")
    private Date payTime;
    /**
     * 发货时间
     */
    @TableField("consign_time")
    @ApiModelProperty("发货时间")
    private Date consignTime;
    /**
     * 交易完成时间
     */
    @TableField("end_time")
    @ApiModelProperty("交易完成时间")
    private Date endTime;
    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String username;
    /**
     * 收货人
     */
    @ApiModelProperty("收货人")
    private String recipients;
    /**
     * 收货人手机
     */
    @TableField("recipients_mobile")
    @ApiModelProperty("收货人手机")
    private String recipientsMobile;
    /**
     * 收货人地址
     */
    @TableField("recipients_address")
    @ApiModelProperty("收货人地址")
    private String recipientsAddress;
    /**
     * 交易流水号
     */
    @TableField("weixin_transaction_id")
    @ApiModelProperty("交易流水号")
    private String weixinTransactionId;
    /**
     * 订单状态,0:未完成,1:已完成，2：已退货
     */
    @TableField("order_status")
    @ApiModelProperty("订单状态,0:未完成,1:已完成，2：已退货")
    private Integer orderStatus;
    /**
     * 支付状态,0:未支付，1：已支付，2：支付失败
     */
    @TableField("pay_status")
    @ApiModelProperty("支付状态,0:未支付，1：已支付，2：支付失败")
    private Integer payStatus;
    /**
     * 是否删除
     */
    @TableField("is_delete")
    @ApiModelProperty("是否删除")
    private Integer isDelete;
}