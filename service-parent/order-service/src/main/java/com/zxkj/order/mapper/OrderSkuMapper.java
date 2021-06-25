package com.zxkj.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.order.model.OrderSku;
import com.zxkj.order.vo.OrderSkuVo;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
public interface OrderSkuMapper extends BaseMapper<OrderSku> {

    List<OrderSkuVo> getOrderSkuByUserName(String username);
}
