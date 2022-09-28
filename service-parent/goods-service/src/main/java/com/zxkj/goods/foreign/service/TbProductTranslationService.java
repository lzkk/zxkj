package com.zxkj.goods.foreign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.goods.condition.TbProductTranslationAddCondition;
import com.zxkj.goods.condition.TbProductTranslationQueryCondition;
import com.zxkj.goods.foreign.entity.TbProductTranslation;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务
 * @date 2022-09-28 15:45:53
 */
public interface TbProductTranslationService extends IService<TbProductTranslation> {

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<TbProductTranslation> param);

    List<TbProductTranslation> selectList(TbProductTranslationQueryCondition condition);

    void add(TbProductTranslationAddCondition condition);

}
