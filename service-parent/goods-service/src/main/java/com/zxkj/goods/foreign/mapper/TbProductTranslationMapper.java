package com.zxkj.goods.foreign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxkj.goods.foreign.entity.TbProductTranslation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc  Mapper
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-28 15:45:53
 */
public interface TbProductTranslationMapper extends BaseMapper<TbProductTranslation> {

    /**
     *  批量插入数据集
     * @param param
     * @return Boolean
     */
    Boolean insertAll(@Param("paramList") List<TbProductTranslation> param);

}
