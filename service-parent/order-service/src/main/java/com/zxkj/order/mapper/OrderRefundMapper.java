package com.zxkj.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.order.entity.OrderRefund;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc  Mapper
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 14:00:31
 */
public interface OrderRefundMapper extends BaseMapper<OrderRefund> {

    /**
     *  批量插入数据集
     * @param param
     * @return Boolean
     */
    Boolean insertAll(@Param("paramList") List<OrderRefund> param);

}
