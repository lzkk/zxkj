package com.zxkj.goods.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.goods.condition.ProductCondition;
import com.zxkj.goods.service.SpuService;
import com.zxkj.goods.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/spu")
public class SpuController {

    @Autowired
    private SpuService spuService;

    /*****
     * 产品保存
     */
    @PostMapping(value = "/save")
    public RespResult save(@RequestBody ProductCondition product) {
        spuService.saveProduct(product);
        return RespResult.ok();
    }

    /***
     * 查询Product
     */
    @GetMapping(value = "/product/{id}")
    public RespResult<ProductVo> one(@PathVariable(value = "id") String id) {
        ProductVo product = spuService.findBySupId(id);
        return RespResult.ok(product);
    }
}
