package com.zxkj.goods.foreign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @desc 
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-28 15:45:53
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_language")
public class TbLanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "code", type = IdType.ASSIGN_ID)
    @ApiModelProperty("")
    private String code;
    @ApiModelProperty("")
    private String name;
}