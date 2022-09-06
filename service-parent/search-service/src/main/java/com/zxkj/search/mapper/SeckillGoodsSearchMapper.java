package com.zxkj.search.mapper;

import com.zxkj.search.entity.SeckillGoodsEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SeckillGoodsSearchMapper extends ElasticsearchRepository<SeckillGoodsEs,String> {
}
