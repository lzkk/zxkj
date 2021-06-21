package com.zxkj.order.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.goods.feign.ProductInnovateFeign;
import com.zxkj.goods.model.ProductInnovateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单信息Controller
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/5 15:26
 */
@RestController
@RefreshScope
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${didispace.title:}")
    private String hello;

    @Autowired
    private ProductInnovateFeign productInnovateFeign;

    @PostMapping(value = "/getOrderInfo")
    public RespResult<String> getOrderInfo(@RequestBody Integer id) {
        logger.info("getOrderInfo，参数：" + id + "," + hello);
        if (id == null) {
            return RespResult.error("参数不能为空");
        }
        ProductInnovateModel productInnovateModel = new ProductInnovateModel();
        productInnovateModel.setId(id);
        String result = productInnovateFeign.sayHello(productInnovateModel).getDataWithException();
        logger.info("result:" + result);
        return RespResult.ok(result);
    }

}
