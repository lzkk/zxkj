package com.zxkj.order.controller;

import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.web.RespResult;
import com.zxkj.order.feign.OrderFeign;
import com.zxkj.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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

    public RespResult<Boolean> ribbonTest2() {
        boolean flag = true;
        if(flag){
            throw new BusinessException(10001);
        }
        return RespResult.ok(true);
    }

}
