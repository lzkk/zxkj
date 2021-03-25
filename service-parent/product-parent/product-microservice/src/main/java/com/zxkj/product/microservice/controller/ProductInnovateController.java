package com.zxkj.product.microservice.controller;

import com.zxkj.common.exception.business.BusinessException;
import com.zxkj.common.feign.product.ProductInnovateFeign;
import com.zxkj.common.web.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 产品信息Controller
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/5 15:26
 */
@RestController
public class ProductInnovateController implements ProductInnovateFeign {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public DataResponse<?> sayHello(@RequestBody Integer id) {
        logger.info("sayHello，参数：" + id);

        if(1 == id){
            throw new BusinessException("22222");
        }

        return DataResponse.ok("sayHello," + id);
    }
}
