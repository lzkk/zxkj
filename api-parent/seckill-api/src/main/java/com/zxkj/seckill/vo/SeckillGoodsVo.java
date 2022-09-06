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
public class SeckillGoodsVo {

    @ApiModelProperty("")
    private String id;
    /**
     * spu ID
     */
    @ApiModelProperty("spu ID")
    private String supId;
    /**
     * sku ID
     */
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
    @ApiModelProperty("秒杀价格")
    private Double seckillPrice;
    /**
     * 添加日期
     */
    @ApiModelProperty("添加日期")
    private Date createTime;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
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
    @ApiModelProperty("活动ID")
    private String activityId;
    /**
     * 是否锁定
     */
    @ApiModelProperty("是否锁定")
    private Integer islock;
}