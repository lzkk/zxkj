package com.zxkj.search.controller;

import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.common.web.RespResult;
import com.zxkj.search.condition.SkuEsCondition;
import com.zxkj.search.feign.SkuSearchFeign;
import com.zxkj.search.entity.SkuEs;
import com.zxkj.search.service.SkuSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
public class SkuSearchController implements SkuSearchFeign {

    @Autowired
    private SkuSearchService skuSearchService;

    /*****
     * 增加索引
     */
    public RespResult<Boolean> add(@RequestBody SkuEsCondition skuEsCondition) {
        if (skuEsCondition == null) {
            return RespResult.error();
        }
        SkuEs skuEs = BeanUtil.copyObject(skuEsCondition, SkuEs.class);
        skuSearchService.add(skuEs);
        return RespResult.ok(true);
    }

    /***
     * 删除索引
     */
    public RespResult<Boolean> del(@PathVariable(value = "id") String id) {
        skuSearchService.del(id);
        return RespResult.ok(true);
    }
}
