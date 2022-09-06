package com.zxkj.seckill.entity;

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
 * @date 2022-09-05 14:37:18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("seckill_order")
public class SeckillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键")
    private String id;
    /**
     * 秒杀商品ID
     */
    @TableField("seckill_goods_id")
    @ApiModelProperty("秒杀商品ID")
    private String seckillGoodsId;
    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private Integer money;
    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    private Integer num;
    /**
     * 用户
     */
    @ApiModelProperty("用户")
    private String username;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    @ApiModelProperty("支付时间")
    private Date payTime;
    /**
     * 状态，0未支付，1已支付
     */
    @ApiModelProperty("状态，0未支付，1已支付")
    private Integer status;
    /**
     * 交易流水
     */
    @TableField("weixin_transaction_id")
    @ApiModelProperty("交易流水")
    private String weixinTransactionId;
}