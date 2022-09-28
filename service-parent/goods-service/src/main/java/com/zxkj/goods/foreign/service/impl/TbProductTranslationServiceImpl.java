package com.zxkj.goods.foreign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.goods.condition.TbProductTranslationAddCondition;
import com.zxkj.goods.condition.TbProductTranslationQueryCondition;
import com.zxkj.goods.foreign.entity.TbProductTranslation;
import com.zxkj.goods.foreign.mapper.TbProductTranslationMapper;
import com.zxkj.goods.foreign.service.TbProductTranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务实现
 * @date 2022-09-28 15:45:53
 */
@Service
@Slf4j
public class TbProductTranslationServiceImpl extends ServiceImpl<TbProductTranslationMapper, TbProductTranslation> implements TbProductTranslationService {

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<TbProductTranslation> param) {
        return this.getBaseMapper().insertAll(param);
    }

    public List<TbProductTranslation> selectList(TbProductTranslationQueryCondition condition) {
        QueryWrapper<TbProductTranslation> queryWrapper = new QueryWrapper<>();
        if (condition.getLanguageCode() != null) {
            queryWrapper.eq("language_code", condition.getLanguageCode());
        }
        if (condition.getTitle() != null) {
            queryWrapper.like("title", condition.getTitle());
        }
        return this.getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public void add(TbProductTranslationAddCondition condition) {
        TbProductTranslation tbProductTranslation = BeanUtil.copyObject(condition, TbProductTranslation.class);
        this.getBaseMapper().insert(tbProductTranslation);
    }

}
