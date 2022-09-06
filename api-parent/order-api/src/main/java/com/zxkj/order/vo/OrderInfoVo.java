package com.zxkj.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yuhui
 * @version 1.0
 * @desc
 * @date 2022-09-05 13:47:42
 */
@Data
public class OrderInfoVo {

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private String id;
    /**
     * 数量合计
     */
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
    @ApiModelProperty("支付类型，1、在线支付、0 货到付款")
    private String payType;
    /**
     * 订单创建时间
     */
    @ApiModelProperty("订单创建时间")
    private Date createTime;
    /**
     * 订单更新时间
     */
    @ApiModelProperty("订单更新时间")
    private Date updateTime;
    /**
     * 付款时间
     */
    @ApiModelProperty("付款时间")
    private Date payTime;
    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    private Date consignTime;
    /**
     * 交易完成时间
     */
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
    @ApiModelProperty("收货人手机")
    private String recipientsMobile;
    /**
     * 收货人地址
     */
    @ApiModelProperty("收货人地址")
    private String recipientsAddress;
    /**
     * 交易流水号
     */
    @ApiModelProperty("交易流水号")
    private String weixinTransactionId;
    /**
     * 订单状态,0:未完成,1:已完成，2：已退货
     */
    @ApiModelProperty("订单状态,0:未完成,1:已完成，2：已退货")
    private Integer orderStatus;
    /**
     * 支付状态,0:未支付，1：已支付，2：支付失败
     */
    @ApiModelProperty("支付状态,0:未支付，1：已支付，2：支付失败")
    private Integer payStatus;
    /**
     * 是否删除
     */
    @ApiModelProperty("是否删除")
    private Integer isDelete;
}