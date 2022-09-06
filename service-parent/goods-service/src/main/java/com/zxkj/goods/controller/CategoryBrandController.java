package com.zxkj.goods.controller;

import com.zxkj.goods.service.CategoryBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc  前端控制器
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 17:22:02
 */
@RestController
@RequestMapping("/category-brand")
public class CategoryBrandController {

    @Autowired
    private CategoryBrandService ategoryBrandService;

}
