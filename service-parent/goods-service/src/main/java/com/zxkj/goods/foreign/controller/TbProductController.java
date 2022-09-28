package com.zxkj.goods.foreign.controller;

import com.zxkj.goods.foreign.service.TbProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc  前端控制器
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-28 15:45:53
 */
@RestController
@RequestMapping("/tb-product")
public class TbProductController {

    @Autowired
    private TbProductService bProductService;

}
