package com.zxkj.canal.model.seckill;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author yuhui
 * @version 1.0
 * @desc
 * @date 2022-09-05 14:37:18
 */
@Data
public class SeckillGoodsModel {

    @ApiModelProperty("")
    private String id;
    /**
     * spu ID
     */
    @Column(name = "sup_id")
    @ApiModelProperty("spu ID")
    private String supId;
    /**
     * sku ID
     */
    @Column(name = "sku_id")
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
    @Column(name = "seckill_price")
    @ApiModelProperty("秒杀价格")
    private Integer seckillPrice;
    /**
     * 添加日期
     */
    @Column(name = "create_time")
    @ApiModelProperty("添加日期")
    private Date createTime;
    /**
     * 开始时间
     */
    @Column(name = "start_time")
    @ApiModelProperty("开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @Column(name = "end_time")
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
    @Column(name = "store_count")
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
    @Column(name = "activity_id")
    @ApiModelProperty("活动ID")
    private String activityId;
    /**
     * 是否锁定
     */
    @ApiModelProperty("是否锁定")
    private Integer islock;
}