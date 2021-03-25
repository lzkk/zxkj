package com.zxkj.order.microservice.controller;

import com.alibaba.fastjson.JSON;
import com.zxkj.common.feign.product.ProductInnovateFeign;
import com.zxkj.common.web.DataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单信息Controller
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/5 15:26
 */
@RestController
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductInnovateFeign productInnovateFeign;

    @RequestMapping(value = "/getOrderInfo", method = RequestMethod.POST)
    public DataResponse<String> getOrderInfo(@RequestBody Integer id) {
        logger.info("getOrderInfo，参数：" + id);
        try {
            if (id == null) {
                return DataResponse.no("参数不能为空");
            }
            DataResponse dataResponse = productInnovateFeign.sayHello(id);

            return DataResponse.ok(JSON.toJSONString(dataResponse.getResult()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DataResponse.no(e.getMessage());
        }
    }

}
