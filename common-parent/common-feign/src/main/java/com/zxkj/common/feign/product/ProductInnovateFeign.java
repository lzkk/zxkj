package com.zxkj.common.feign.product;


import com.zxkj.common.feign.constants.MicroServiceIdConstant;
import com.zxkj.common.web.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = MicroServiceIdConstant.PRODUCT_SERVICE, path = "/", fallback = ProductInnovateFeignFallback.class)
public interface ProductInnovateFeign {

    /**
     * 根据id获取产品信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sayHello", method = RequestMethod.POST)
    DataResponse<?> sayHello(@RequestBody Integer id);

}

@Component
class ProductInnovateFeignFallback implements ProductInnovateFeign {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductInnovateFeignFallback.class);


    @Override
    public DataResponse<?> sayHello(Integer id) {
        LOGGER.error("=产品模块=sayHello失败");
        return DataResponse.no("sayHello失败");
    }

}