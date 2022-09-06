package com.zxkj.search.service;

import com.zxkj.search.entity.SeckillGoodsEs;

public interface SeckillGoodsSearchService {

    //导入到索引库
    void add(SeckillGoodsEs seckillGoodsEs);

    //删除索引
    void del(String id);
}
