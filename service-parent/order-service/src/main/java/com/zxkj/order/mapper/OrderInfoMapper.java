package com.zxkj.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.order.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc  Mapper
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 13:47:42
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     *  批量插入数据集
     * @param param
     * @return Boolean
     */
    Boolean insertAll(@Param("paramList") List<OrderInfo> param);

}