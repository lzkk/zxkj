package com.zxkj.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.order.condition.OrderRefundCondition;
import com.zxkj.order.entity.OrderRefund;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务
 * @date 2022-09-05 14:00:31
 */
public interface OrderRefundService extends IService<OrderRefund> {

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<OrderRefund> param);

    /***
     * 退款
     */
    int refund(OrderRefundCondition orderRefund);

}
