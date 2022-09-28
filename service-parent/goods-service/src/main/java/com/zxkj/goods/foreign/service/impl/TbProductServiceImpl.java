package com.zxkj.goods.foreign.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.goods.foreign.entity.TbProduct;
import com.zxkj.goods.foreign.mapper.TbProductMapper;
import com.zxkj.goods.foreign.service.TbProductService;
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
public class TbProductServiceImpl extends ServiceImpl<TbProductMapper, TbProduct> implements TbProductService {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<TbProduct> param) {
        return this.getBaseMapper().insertAll(param);
    }

}
