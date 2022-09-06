package com.zxkj.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.common.cache.redis.Cache;
import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.seckill.entity.SeckillGoods;
import com.zxkj.seckill.mapper.SeckillGoodsMapper;
import com.zxkj.seckill.service.SeckillGoodsService;
import com.zxkj.seckill.vo.SeckillGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务实现
 * @date 2022-09-05 14:37:18
 */
@Service
@Slf4j
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements SeckillGoodsService {

    @Resource
    private Cache cache;

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<SeckillGoods> param) {
        return this.getBaseMapper().insertAll(param);
    }

    /****
     * 热门商品分离
     * @param uri：商品ID
     */
    @Override
    public void isolation(String uri) {
        //1.锁定商品
        QueryWrapper<SeckillGoods> seckillGoodsQueryWrapper = new QueryWrapper<>();
        seckillGoodsQueryWrapper.eq("id", uri);
        seckillGoodsQueryWrapper.eq("islock", 0);
        seckillGoodsQueryWrapper.gt("store_count", 0);
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setIslock(1);
        int count = this.getBaseMapper().update(seckillGoods, seckillGoodsQueryWrapper);

        //2.分离->查询出来存入到Redis缓存
        if (count > 0) {
            //查询商品个数
            seckillGoods = this.getBaseMapper().selectById(uri);
            // HotSeckillGoods
            //                1:5
            cache.hincre("HotSeckillGoods", uri, seckillGoods.getStoreCount());
        }
    }

    //根据活动ID查询商品信息
    @Override
    public List<SeckillGoodsVo> actGoods(String acid) {
        QueryWrapper<SeckillGoods> seckillGoodsQueryWrapper = new QueryWrapper<>();
        seckillGoodsQueryWrapper.eq("activity_id", acid);
        return BeanUtil.copyList(this.getBaseMapper().selectList(seckillGoodsQueryWrapper), SeckillGoodsVo.class);
    }

}
