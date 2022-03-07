package com.zxkj.order.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.hystrix.CustomFallbackFactory;
import com.zxkj.common.web.RespResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = ServiceIdConstant.ORDER_SERVICE, path = "/", fallbackFactory = OrderFeignFallback.class)    //服务名字
public interface OrderFeign {

    @GetMapping(value = "/order/ribbonTest")
    RespResult<Boolean> ribbonTest();

}

@Component
class OrderFeignFallback extends CustomFallbackFactory implements OrderFeign {
    private static final Logger logger = LoggerFactory.getLogger(OrderFeignFallback.class);

    @Override
    public RespResult ribbonTest() {
        logger.error("OrderFeignFallback -> ribbonTest错误信息：{}", throwable.getMessage());
        return RespResult.error(throwable.getMessage());
    }

}

