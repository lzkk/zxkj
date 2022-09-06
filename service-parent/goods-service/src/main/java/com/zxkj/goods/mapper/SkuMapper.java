package com.zxkj.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.goods.entity.Sku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 商品表 Mapper
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 16:35:52
 */
public interface SkuMapper extends BaseMapper<Sku> {

    /**
     * 商品表 批量插入数据集
     * @param param
     * @return Boolean
     */
    Boolean insertAll(@Param("paramList") List<Sku> param);

    int dcount(@Param("id") String id, @Param("num") Integer num);
}
