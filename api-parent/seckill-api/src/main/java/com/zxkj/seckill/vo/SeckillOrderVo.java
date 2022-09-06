package com.zxkj.seckill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yuhui
 * @version 1.0
 * @desc
 * @date 2022-09-05 14:37:18
 */
@Data
public class SeckillOrderVo {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 秒杀商品ID
     */
    @ApiModelProperty("秒杀商品ID")
    private String seckillGoodsId;
    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private Integer money;
    /**
     * 用户
     */
    @ApiModelProperty("用户")
    private String username;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 支付时间
     */
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
    @ApiModelProperty("交易流水")
    private String weixinTransactionId;
}