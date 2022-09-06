package com.zxkj.seckill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yuhui
 * @version 1.0
 * @desc
 * @date 2022-09-05 14:43:27
 */
@Data
public class SeckillActivityVo {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 秒杀商品ID
     */
    @ApiModelProperty("秒杀商品ID")
    private String activityName;
    /**
     * 状态，0未支付，1已支付
     */
    @ApiModelProperty("状态，0未支付，1已支付")
    private Integer type;
    /**
     * 支付时间
     */
    @ApiModelProperty("支付时间")
    private Date endTime;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
}