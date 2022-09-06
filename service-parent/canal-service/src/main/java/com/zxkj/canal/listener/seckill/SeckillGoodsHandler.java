package com.zxkj.canal.listener.seckill;

import com.alibaba.fastjson.JSON;
import com.zxkj.canal.model.seckill.SeckillGoodsModel;
import com.zxkj.page.feign.SeckillPageFeign;
import com.zxkj.search.condition.SeckillGoodsEsCondition;
import com.zxkj.search.feign.SeckillGoodsSearchFeign;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import javax.annotation.Resource;

/*****
 * @Author:
 * @Description:
 ****/
@CanalTable(value = "seckill_goods")
@Component
public class SeckillGoodsHandler implements EntryHandler<SeckillGoodsModel> {

    @Resource
    private SeckillGoodsSearchFeign seckillGoodsSearchFeign;

    @Resource
    private SeckillPageFeign seckillPageFeign;

    /***
     * 增加商品
     * @param seckillGoods
     */
    @SneakyThrows
    @Override
    public void insert(SeckillGoodsModel seckillGoods) {
        //索引导入
        seckillGoodsSearchFeign.add(JSON.parseObject(JSON.toJSONString(seckillGoods), SeckillGoodsEsCondition.class));

        //生成/更新静态页
        seckillPageFeign.page(seckillGoods.getId());
    }


    /****
     * 修改商品
     * @param before
     * @param after
     */
    @SneakyThrows
    @Override
    public void update(SeckillGoodsModel before, SeckillGoodsModel after) {
        //索引导入
        seckillGoodsSearchFeign.add(JSON.parseObject(JSON.toJSONString(after), SeckillGoodsEsCondition.class));

        //生成/更新静态页
        seckillPageFeign.page(after.getId());
    }
}
