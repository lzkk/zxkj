package com.zxkj.order.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.feign.fallback.CustomFallbackFactory;
import com.zxkj.order.condition.OrderInfoCondition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = ServiceIdConstant.ORDER_SERVICE, path = "/", fallbackFactory = OrderFeignFallback.class)    //服务名字
public interface OrderInfoFeign {

    @GetMapping(value = "/order/ribbonTest")
    RespResult<Boolean> ribbonTest();

    @PostMapping(value = "/order/ribbonTest2")
    RespResult<Boolean> ribbonTest2(@RequestBody @Valid OrderInfoCondition condition);

}

@Component
class OrderFeignFallback extends CustomFallbackFactory implements OrderInfoFeign {

    @Override
    public RespResult ribbonTest() {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult ribbonTest2(OrderInfoCondition condition) {
        return RespResult.error(throwable.getMessage());
    }

}

