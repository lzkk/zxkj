package com.zxkj.order.controller;

import com.zxkj.order.service.OrderSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc  前端控制器
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 13:47:42
 */
@RestController
@RequestMapping("/order-sku")
public class OrderSkuController {

    @Autowired
    private OrderSkuService rderSkuService;

}
