package com.zxkj.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.order.condition.OrderRefundCondition;
import com.zxkj.order.entity.OrderInfo;
import com.zxkj.order.entity.OrderRefund;
import com.zxkj.order.mapper.OrderInfoMapper;
import com.zxkj.order.mapper.OrderRefundMapper;
import com.zxkj.order.service.OrderRefundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务实现
 * @date 2022-09-05 14:00:31
 */
@Service
@Slf4j
public class OrderRefundServiceImpl extends ServiceImpl<OrderRefundMapper, OrderRefund> implements OrderRefundService {

    @Resource
    private OrderInfoMapper orderInfoMapper;

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<OrderRefund> param) {
        return this.getBaseMapper().insertAll(param);
    }


    /****
     * 退款申请
     * @param orderRefundCondition
     * @return
     */
    @Override
    public int refund(OrderRefundCondition orderRefundCondition) {
        OrderRefund orderRefund = new OrderRefund();
        BeanUtils.copyProperties(orderRefundCondition, orderRefund);
        //1记录退款申请
        this.getBaseMapper().insert(orderRefund);

        //2修改订单状态
        OrderInfo order = new OrderInfo();
        order.setOrderStatus(4);    //申请退款

        //构建条件
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderRefund.getOrderNo()); //订单ID
        queryWrapper.eq("username", orderRefund.getUsername()); //用户名
        queryWrapper.eq("order_status", 1);
        queryWrapper.eq("pay_status", 1);
        return orderInfoMapper.update(order, queryWrapper);
    }
}
