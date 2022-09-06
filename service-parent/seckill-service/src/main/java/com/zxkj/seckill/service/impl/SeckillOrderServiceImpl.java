package com.zxkj.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.common.cache.redis.Cache;
import com.zxkj.seckill.entity.SeckillGoods;
import com.zxkj.seckill.entity.SeckillOrder;
import com.zxkj.seckill.mapper.SeckillGoodsMapper;
import com.zxkj.seckill.mapper.SeckillOrderMapper;
import com.zxkj.seckill.service.SeckillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务实现
 * @date 2022-09-05 14:37:18
 */
@Service
@Slf4j
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements SeckillOrderService {

    //库存不足
    public static final int STORE_NOT_FULL = 0;
    //库存足够下单成功
    public static final int STORE_FULL_ORDER_SUCCESS = 1;

    @Autowired
    private Cache cache;

    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<SeckillOrder> param) {
        return this.getBaseMapper().insertAll(param);
    }

    /***
     * 热门商品抢单实现
     * @return
     */
    @Override
    public int add(Map<String, Object> dataMap) {
        //username
        String username = dataMap.get("username").toString();
        //id
        String id = dataMap.get("id").toString();
        //num
        Integer num = Integer.valueOf(dataMap.get("num").toString());

        //获取锁
        RLock lock = redissonClient.getLock("No00001");
        lock.lock();

        try {
            /**
             * 库存足够
             */
            Object storecount = cache.hget("HotSeckillGoods", id);
            if (storecount == null || Integer.valueOf(storecount.toString()) < num) {
                //移除排队标识
                cache.del("OrderQueue" + username);
                return STORE_NOT_FULL;
            }

            //查询商品信息
            SeckillGoods seckillGoods = seckillGoodsMapper.selectById(id);

            /***
             * 添加订单
             */
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setUsername(username);
            seckillOrder.setSeckillGoodsId(id);
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setMoney(seckillGoods.getSeckillPrice() * num);
            seckillOrder.setNum(num);
            seckillOrder.setStatus(0);  //下单了
            this.getBaseMapper().insert(seckillOrder);

            /*****
             * 库存递减
             */
            Long lastStoreCount = cache.hincre("HotSeckillGoods", id, -num);

            if (lastStoreCount == 0) {
                //将数据同步到数据库
                seckillGoods = new SeckillGoods();
                seckillGoods.setId(id);
                seckillGoods.setStoreCount(0);
                //将当前商品添加到Redis布隆过滤器->作业->用户下次抢购该商品，去布隆过滤器中判断该商品是否在布隆过滤器中，如果在，则表明售罄
                seckillGoodsMapper.updateById(seckillGoods);
                //删除Redis缓存
                cache.hdel("HotSeckillGoods", id);
            }
            //移除排队标识
            cache.del("OrderQueue" + username);
            lock.unlock();
        } catch (NumberFormatException e) {
            lock.unlock();
        }
        return STORE_FULL_ORDER_SUCCESS;
    }

}
