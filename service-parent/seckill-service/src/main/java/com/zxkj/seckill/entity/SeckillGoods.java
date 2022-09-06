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
@TableName("seckill_goods")
public class SeckillGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("")
    private String id;
    /**
     * spu ID
     */
    @TableField("sup_id")
    @ApiModelProperty("spu ID")
    private String supId;
    /**
     * sku ID
     */
    @TableField("sku_id")
    @ApiModelProperty("sku ID")
    private String skuId;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String name;
    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String images;
    /**
     * 原价格
     */
    @ApiModelProperty("原价格")
    private Integer price;
    /**
     * 秒杀价格
     */
    @TableField("seckill_price")
    @ApiModelProperty("秒杀价格")
    private Integer seckillPrice;
    /**
     * 添加日期
     */
    @TableField("create_time")
    @ApiModelProperty("添加日期")
    private Date createTime;
    /**
     * 开始时间
     */
    @TableField("start_time")
    @ApiModelProperty("开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @TableField("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;
    /**
     * 秒杀商品数
     */
    @ApiModelProperty("秒杀商品数")
    private Integer num;
    /**
     * 剩余库存数
     */
    @TableField("store_count")
    @ApiModelProperty("剩余库存数")
    private Integer storeCount;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String content;
    /**
     * 活动ID
     */
    @TableField("activity_id")
    @ApiModelProperty("活动ID")
    private String activityId;
    /**
     * 是否锁定
     */
    @ApiModelProperty("是否锁定")
    private Integer islock;
}