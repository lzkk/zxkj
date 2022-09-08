package com.zxkj.order.feign;

import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.feign.fallback.CustomFallbackFactory;
import com.zxkj.goods.vo.SkuVo;
import com.zxkj.order.condition.OrderInfoCondition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/*****
 * @Author:
 * @Description:
 ****/
@FeignClient(value = ServiceIdConstant.ORDER_SERVICE, path = "/", fallbackFactory = OrderFeignFallback.class)    //服务名字
public interface OrderInfoFeign {

    @GetMapping(value = "/order/ribbonTest")
    RespResult<SkuVo> ribbonTest(@RequestParam(value = "id") String id);

    @PostMapping(value = "/order/ribbonTest2")
    RespResult<Boolean> ribbonTest2(@RequestBody @Valid OrderInfoCondition condition);

}

@Component
class OrderFeignFallback extends CustomFallbackFactory implements OrderInfoFeign {

    @Override
    public RespResult<SkuVo> ribbonTest(String id) {
        return RespResult.error(throwable.getMessage());
    }

    @Override
    public RespResult ribbonTest2(OrderInfoCondition condition) {
        return RespResult.error(throwable.getMessage());
    }

}

