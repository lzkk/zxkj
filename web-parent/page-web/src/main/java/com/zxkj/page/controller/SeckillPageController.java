package com.zxkj.page.controller;

import com.zxkj.common.web.RespResult;
import com.zxkj.page.feign.SeckillPageFeign;
import com.zxkj.page.service.SeckillPageService;
import com.zxkj.seckill.feign.SeckillGoodsFeign;
import com.zxkj.seckill.model.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeckillPageController implements SeckillPageFeign {

    @Autowired
    private SeckillPageService seckillPageService;

    @Autowired
    private SeckillGoodsFeign seckillGoodsFeign;

    /***
     * 生成秒杀商品详情页
     */
    public RespResult page(@PathVariable("id") String id) throws Exception {
        //生成秒杀商品详情页
        seckillPageService.html(id);
        return RespResult.ok();
    }

    /***
     * 删除指定活动的页面
     */
    public RespResult deleByAct(@PathVariable("acid") String acid) {
        //1.查询当前活动ID对应的商品列表数据\
        RespResult<List<SeckillGoods>> listRespResult = seckillGoodsFeign.actGoods(acid);
        List<SeckillGoods> goodsList = listRespResult.getData();
        //2.根据列表数据删除页面
        if (goodsList != null) {
            //循环删除页面
            for (SeckillGoods seckillGoods : goodsList) {
                seckillPageService.delete(seckillGoods.getId());
            }
        }
        return RespResult.ok();
    }
}
