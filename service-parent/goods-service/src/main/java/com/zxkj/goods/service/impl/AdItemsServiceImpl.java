package com.zxkj.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.goods.entity.AdItems;
import com.zxkj.goods.mapper.AdItemsMapper;
import com.zxkj.goods.service.AdItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc  服务实现
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 17:22:02
 */
@Service
@Slf4j
public class AdItemsServiceImpl extends ServiceImpl<AdItemsMapper, AdItems> implements AdItemsService {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<AdItems> param) {
        return this.getBaseMapper().insertAll(param);
    }

}
