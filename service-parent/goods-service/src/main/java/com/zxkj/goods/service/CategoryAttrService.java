package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.goods.entity.CategoryAttr;

import java.util.List;

/**
 * @desc  服务
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 17:22:02
 */
public interface CategoryAttrService extends IService<CategoryAttr> {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<CategoryAttr> param);

}
