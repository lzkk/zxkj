package com.zxkj.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.order.model.Order;
import com.zxkj.order.model.OrderRefund;
import com.zxkj.order.vo.OrderSkuVo;

import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
public interface OrderService extends IService<Order> {

    /***
     * 退款
     */
    int refund(OrderRefund orderRefund);

    /***
     * 添加订单
     */
    Boolean add(Order order);

    /***
     * 支付成功修改状态
     */
    int updateAfterPayStatus(String id);

    Order getById(String id);

    Order getByUserName(String username);

    List<OrderSkuVo> getOrderSkuByUserName(String username);

    List<OrderSkuVo> getCart(List<String> ids);

    void ribbonTest();
}
