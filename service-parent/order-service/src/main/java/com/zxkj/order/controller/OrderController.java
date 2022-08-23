package com.zxkj.order.controller;

import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.web.RespResult;
import com.zxkj.order.condition.TestCondition;
import com.zxkj.order.feign.OrderFeign;
import com.zxkj.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 订单信息Controller
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/5 15:26
 */
@RestController
public class OrderController implements OrderFeign {

    @Autowired
    private OrderService orderService;

    public RespResult<Boolean> ribbonTest() {
        orderService.ribbonTest();
        return RespResult.ok(true);
    }

    public RespResult<Boolean> ribbonTest2(@RequestBody @Valid TestCondition condition) {
        if ("1".equals(condition.getOrderNo())) {
            throw new BusinessException(10001);
        } else if ("2".equals(condition.getOrderNo())) {
            throw new RuntimeException("哈哈哈哈");
        } else if ("3".equals(condition.getOrderNo())) {
            throw new ArrayIndexOutOfBoundsException("越界了");
        } else if ("4".equals(condition.getOrderNo())) {
            throw new BusinessException("急急急");
        }
        return RespResult.ok(true);
    }

}
