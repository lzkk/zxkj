package com.zxkj.goods.controller;

import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.feign.ProductInnovateFeign;
import com.zxkj.goods.model.ProductInnovateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品信息Controller
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/5 15:26
 */
@RestController
public class ProductInnovateController implements ProductInnovateFeign {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public RespResult<String> sayHello(@RequestBody ProductInnovateModel productInnovateModel) {
        logger.info("sayHello，参数：" + productInnovateModel);

        if (1 == productInnovateModel.getId()) {
            throw new BusinessException("参数异常");
        }

        return RespResult.ok("sayHello," + productInnovateModel.getId());
    }
}
