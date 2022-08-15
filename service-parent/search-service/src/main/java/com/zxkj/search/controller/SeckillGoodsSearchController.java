package com.zxkj.search.controller;

import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.common.web.RespResult;
import com.zxkj.search.condition.SeckillGoodsEsCondition;
import com.zxkj.search.feign.SeckillGoodsSearchFeign;
import com.zxkj.search.model.SeckillGoodsEs;
import com.zxkj.search.service.SeckillGoodsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeckillGoodsSearchController implements SeckillGoodsSearchFeign {

    @Autowired
    private SeckillGoodsSearchService seckillGoodsSearchService;

    /***
     * 将秒杀商品导入索引库
     * http://localhost:8084/seckill/goods/import
     */
    public RespResult<Boolean> add(@RequestBody SeckillGoodsEsCondition seckillGoodsEsCondition) {
        if (seckillGoodsEsCondition == null) {
            return RespResult.error();
        }
        SeckillGoodsEs seckillGoodsEs = BeanUtil.copyObject(seckillGoodsEsCondition, SeckillGoodsEs.class);
        seckillGoodsSearchService.add(seckillGoodsEs);
        return RespResult.ok(true);
    }

    /***
     * 删除索引
     */
    public RespResult<Boolean> del(@PathVariable(value = "id") String id) {
        seckillGoodsSearchService.del(id);
        return RespResult.ok(true);
    }
}
