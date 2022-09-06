package com.zxkj.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.seckill.entity.SeckillOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc  Mapper
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 14:37:18
 */
public interface SeckillOrderMapper extends BaseMapper<SeckillOrder> {

    /**
     *  批量插入数据集
     * @param param
     * @return Boolean
     */
    Boolean insertAll(@Param("paramList") List<SeckillOrder> param);

}
