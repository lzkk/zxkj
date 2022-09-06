package com.zxkj.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.seckill.entity.SeckillActivity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @desc  Mapper
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 14:43:27
 */
public interface SeckillActivityMapper extends BaseMapper<SeckillActivity> {

    /**
     *  批量插入数据集
     * @param param
     * @return Boolean
     */
    Boolean insertAll(@Param("paramList") List<SeckillActivity> param);

    /****
     * 活动列表（有效的）
     */
    List<SeckillActivity> validActivity();

}
