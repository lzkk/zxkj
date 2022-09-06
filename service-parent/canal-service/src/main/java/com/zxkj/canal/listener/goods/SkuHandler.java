package com.zxkj.canal.listener.goods;

import com.alibaba.fastjson.JSON;
import com.zxkj.canal.model.goods.SkuModel;
import com.zxkj.page.feign.PageFeign;
import com.zxkj.search.condition.SkuEsCondition;
import com.zxkj.search.feign.SkuSearchFeign;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import javax.annotation.Resource;

/*****
 * @Author:
 * @Description:
 ****/
@CanalTable(value = "sku")
@Component
public class SkuHandler implements EntryHandler<SkuModel> {

    @Resource
    private SkuSearchFeign skuSearchFeign;

    @Resource
    private PageFeign pageFeign;

    /***
     * 增加数据监听
     * @param sku
     */
    @SneakyThrows
    @Override
    public void insert(SkuModel sku) {
        if (sku.getStatus().intValue() == 1) {
            //将Sku转成JSON，再将JSON转成SkuEs
            skuSearchFeign.add(JSON.parseObject(JSON.toJSONString(sku), SkuEsCondition.class));
        }
        //生成静态页
        pageFeign.html(sku.getSpuId());
    }

    /****
     * 修改数据监听
     * @param before
     * @param after
     */
    @SneakyThrows
    @Override
    public void update(SkuModel before, SkuModel after) {
        if (after.getStatus().intValue() == 2) {
            //删除索引
            skuSearchFeign.del(after.getId());
        } else {
            //更新
            skuSearchFeign.add(JSON.parseObject(JSON.toJSONString(after), SkuEsCondition.class));
        }
        //生成静态页
        pageFeign.html(after.getSpuId());
    }

    /***
     * 删除数据监听
     * @param sku
     */
    @Override
    public void delete(SkuModel sku) {
        skuSearchFeign.del(sku.getId());
    }
}
