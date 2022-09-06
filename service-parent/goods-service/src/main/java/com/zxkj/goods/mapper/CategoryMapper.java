package com.zxkj.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.goods.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 商品类目 Mapper
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 16:35:52
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 商品类目 批量插入数据集
     * @param param
     * @return Boolean
     */
    Boolean insertAll(@Param("paramList") List<Category> param);

}
