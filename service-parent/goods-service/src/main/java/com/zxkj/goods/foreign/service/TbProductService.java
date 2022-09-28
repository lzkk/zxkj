package com.zxkj.goods.foreign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.goods.foreign.entity.TbProduct;

import java.util.List;

/**
 * @desc  服务
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-28 15:45:53
 */
public interface TbProductService extends IService<TbProduct> {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<TbProduct> param);

}
