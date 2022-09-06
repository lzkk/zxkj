package com.zxkj.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.goods.entity.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 品牌表 Mapper
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 16:35:52
 */
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 品牌表 批量插入数据集
     * @param param
     * @return Boolean
     */
    Boolean insertAll(@Param("paramList") List<Brand> param);



}
