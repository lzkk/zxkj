package com.zxkj.goods.feign;


import com.zxkj.common.constant.ServiceIdConstant;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.model.ProductInnovateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = ServiceIdConstant.GOODS_SERVICE, path = "/", fallback = ProductInnovateFeignFallback.class)
public interface ProductInnovateFeign {

    /**
     * 根据id获取产品信息
     *
     * @param productInnovateModel
     * @return
     */
    @RequestMapping(value = "/sayHello", method = RequestMethod.POST)
    RespResult<String> sayHello(@RequestBody ProductInnovateModel productInnovateModel);

}

@Component
class ProductInnovateFeignFallback implements ProductInnovateFeign {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductInnovateFeignFallback.class);


    @Override
    public RespResult<String> sayHello(ProductInnovateModel productInnovateModel) {
        LOGGER.error("=产品模块=sayHello失败");
        return RespResult.error("sayHello失败");
    }

}