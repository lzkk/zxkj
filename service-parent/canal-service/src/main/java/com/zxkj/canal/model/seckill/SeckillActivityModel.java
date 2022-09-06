package com.zxkj.canal.model.seckill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author yuhui
 * @version 1.0
 * @desc
 * @date 2022-09-05 14:43:27
 */
@Data
public class SeckillActivityModel {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 秒杀商品ID
     */
    @Column(name = "activity_name")
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
    @Column(name = "end_time")
    @ApiModelProperty("支付时间")
    private Date endTime;
    /**
     * 创建时间
     */
    @Column(name = "create_cime")
    @ApiModelProperty("创建时间")
    private Date createTime;
}