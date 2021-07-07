package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.goods.model.SkuAttribute;

import java.util.List;

public interface SkuAttributeService extends IService<SkuAttribute> {


    /***
     * 根据分类ID查询属性加集合
     */
    List<SkuAttribute> queryList(Integer id);
}
