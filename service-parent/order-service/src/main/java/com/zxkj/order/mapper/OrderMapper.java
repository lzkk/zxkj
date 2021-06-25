package com.zxkj.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.order.model.Order;

/*****
 * @Author:
 * @Description:
 ****/
public interface OrderMapper extends BaseMapper<Order> {

    Order getByUserName(String username);

}
