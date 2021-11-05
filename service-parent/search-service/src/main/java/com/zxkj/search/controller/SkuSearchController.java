package com.zxkj.search.controller;

import com.zxkj.common.util.url.BeanUtil;
import com.zxkj.common.web.RespResult;
import com.zxkj.search.condition.SkuEsCondition;
import com.zxkj.search.feign.SkuSearchFeign;
import com.zxkj.search.model.SkuEs;
import com.zxkj.search.service.SkuSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
public class SkuSearchController implements SkuSearchFeign {

    @Autowired
    private SkuSearchService skuSearchService;


    /***
     * 商品搜索
     */
    public RespResult<Map<String, Object>> search(@RequestParam(required = false) Map<String, Object> searchMap) {
        Map<String, Object> resultMap = skuSearchService.search(searchMap);
        return RespResult.ok(resultMap);
    }

    /*****
     * 增加索引
     */
    public RespResult add(@RequestBody SkuEsCondition skuEsCondition) {
        if (skuEsCondition == null) {
            return RespResult.error();
        }
        SkuEs skuEs = BeanUtil.copyObject(skuEsCondition, SkuEs.class);
        skuSearchService.add(skuEs);
        return RespResult.ok();
    }

    /***
     * 删除索引
     */
    public RespResult del(@PathVariable(value = "id") String id) {
        skuSearchService.del(id);
        return RespResult.ok();
    }
}
