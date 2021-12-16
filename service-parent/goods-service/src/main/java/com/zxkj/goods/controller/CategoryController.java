package com.zxkj.goods.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.goods.model.Category;
import com.zxkj.goods.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /****
     * 根据分类父ID查询子分类
     */
    @GetMapping(value = "/parent/{id}")
    public RespResult<List<Category>> findByParentId(@PathVariable("id") Integer id) {
        return RespResult.ok(categoryService.findByParentId(id));
    }

    /****
     * 根据分类查询分类信息
     */
    @GetMapping(value = "/{id}")
    public RespResult<Category> one(@PathVariable(value = "id") Integer id) {
        Category category = categoryService.getById(id);
        return RespResult.ok(category);
    }
}
