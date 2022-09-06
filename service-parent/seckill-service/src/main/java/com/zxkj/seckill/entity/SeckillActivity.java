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
 * @date 2022-09-05 14:43:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("seckill_activity")
public class SeckillActivity implements Serializable {

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
    @TableField("activity_name")
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
    @TableField("end_time")
    @ApiModelProperty("支付时间")
    private Date endTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;
}