package com.zxkj.search.controller.api;

import com.zxkj.common.web.RespResult;
import com.zxkj.search.service.SkuSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@RestController
public class ApiSearchController {

    @Autowired
    private SkuSearchService skuSearchService;

    /***
     * 商品搜索
     */
    @GetMapping(value = "/web/search")
    public RespResult<Map<String, Object>> search(@RequestParam(required = false) Map<String, Object> searchMap) {
        Map<String, Object> resultMap = skuSearchService.search(searchMap);
        return RespResult.ok(resultMap);
    }
}
