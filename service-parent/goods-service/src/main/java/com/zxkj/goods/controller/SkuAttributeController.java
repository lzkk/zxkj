package com.zxkj.goods.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.goods.service.SkuAttributeService;
import com.zxkj.goods.vo.SkuAttributeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/skuAttribute")
public class SkuAttributeController {

    @Autowired
    private SkuAttributeService skuAttributeService;

    /*****
     * 根据分类ID查询属性集合
     */
    @GetMapping(value = "/category/{id}")
    public RespResult<List<SkuAttributeVo>> categorySkuAttributeList(@PathVariable(value = "id") Integer id) {
        List<SkuAttributeVo> skuAttributes = skuAttributeService.queryList(id);
        return RespResult.ok(skuAttributes);
    }
}
