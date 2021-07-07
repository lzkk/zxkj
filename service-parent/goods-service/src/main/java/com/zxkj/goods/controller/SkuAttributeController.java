package com.zxkj.goods.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.goods.model.SkuAttribute;
import com.zxkj.goods.service.SkuAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/skuAttribute")
@CrossOrigin
public class SkuAttributeController {

    @Autowired
    private SkuAttributeService skuAttributeService;

    /*****
     * 根据分类ID查询属性集合
     */
    @GetMapping(value = "/category/{id}")
    public RespResult<List<SkuAttribute>> categorySkuAttributeList(@PathVariable(value = "id") Integer id) {
        List<SkuAttribute> skuAttributes = skuAttributeService.queryList(id);
        return RespResult.ok(skuAttributes);
    }
}
