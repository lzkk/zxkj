package com.zxkj.search.mapper;

import com.zxkj.search.model.SkuEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/*****
 * @Author:
 * @Description:
 ****/
public interface SkuSearchMapper extends ElasticsearchRepository<SkuEs,String> {
}
