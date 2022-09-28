package com.zxkj.goods.foreign.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.goods.foreign.entity.TbLanguage;
import com.zxkj.goods.foreign.mapper.TbLanguageMapper;
import com.zxkj.goods.foreign.service.TbLanguageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc  服务实现
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-28 15:45:53
 */
@Service
@Slf4j
public class TbLanguageServiceImpl extends ServiceImpl<TbLanguageMapper, TbLanguage> implements TbLanguageService {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<TbLanguage> param) {
        return this.getBaseMapper().insertAll(param);
    }

}
