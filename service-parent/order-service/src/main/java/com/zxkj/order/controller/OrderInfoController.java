package com.zxkj.order.controller;

import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.web.RespResult;
import com.zxkj.order.condition.OrderInfoCondition;
import com.zxkj.order.feign.OrderInfoFeign;
import com.zxkj.order.service.OrderInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author yuhui
 * @version 1.0
 * @desc 前端控制器
 * @date 2022-09-05 13:47:42
 */
@RestController
@RequestMapping("/order-info")
public class OrderInfoController implements OrderInfoFeign {

    @Resource
    private OrderInfoService orderInfoService;

    public RespResult<Boolean> ribbonTest() {
        orderInfoService.ribbonTest();
        return RespResult.ok(true);
    }

    public RespResult<Boolean> ribbonTest2(@RequestBody @Valid OrderInfoCondition condition) {
        if ("1".equals(condition.getId())) {
            throw new BusinessException(10001);
        } else if ("2".equals(condition.getId())) {
            throw new RuntimeException("哈哈哈哈");
        } else if ("3".equals(condition.getId())) {
            throw new ArrayIndexOutOfBoundsException("越界了");
        } else if ("4".equals(condition.getId())) {
            throw new BusinessException("急急急");
        }
        return RespResult.ok(true);
    }

}
