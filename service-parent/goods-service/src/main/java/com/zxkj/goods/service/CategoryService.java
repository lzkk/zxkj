package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.goods.entity.Category;
import com.zxkj.goods.vo.CategoryVo;

import java.util.List;

/**
 * @desc 商品类目 服务
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-02 17:22:02
 */
public interface CategoryService extends IService<Category> {

    /**
     * 商品类目 批量新增重写
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<Category> param);

    List<CategoryVo> findByParentId(Integer pid);

}
