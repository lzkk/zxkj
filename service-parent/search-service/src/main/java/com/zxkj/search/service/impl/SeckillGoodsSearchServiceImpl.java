package com.zxkj.search.service.impl;

import com.zxkj.search.mapper.SeckillGoodsSearchMapper;
import com.zxkj.search.model.SeckillGoodsEs;
import com.zxkj.search.service.SeckillGoodsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillGoodsSearchServiceImpl implements SeckillGoodsSearchService {

    @Autowired
    private SeckillGoodsSearchMapper seckillGoodsSearchMapper;

    /***
     * 秒杀商品导入索引库
     * @param seckillGoodsEs
     */
    @Override
    public void add(SeckillGoodsEs seckillGoodsEs) {
        seckillGoodsSearchMapper.save(seckillGoodsEs);
    }


    /***
     * 根据主键删除索引
     * @param id
     */
    @Override
    public void del(String id) {
        seckillGoodsSearchMapper.deleteById(id);
    }
}
