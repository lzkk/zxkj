package com.zxkj.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.seckill.entity.SeckillGoods;
import com.zxkj.seckill.vo.SeckillGoodsVo;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务
 * @date 2022-09-05 14:37:18
 */
public interface SeckillGoodsService extends IService<SeckillGoods> {

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<SeckillGoods> param);

    //热门商品分离  uri:商品ID
    void isolation(String uri);

    //根据活动ID查询商品信息
    List<SeckillGoodsVo> actGoods(String acid);

}
