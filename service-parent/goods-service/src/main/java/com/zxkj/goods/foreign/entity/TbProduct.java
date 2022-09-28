package com.zxkj.goods.foreign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @desc 
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-28 15:45:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_product")
public class TbProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("")
    private Long id;
    @TableField("create_time")
    @ApiModelProperty("")
    private Date createTime;
    @ApiModelProperty("")
    private BigDecimal price;
}