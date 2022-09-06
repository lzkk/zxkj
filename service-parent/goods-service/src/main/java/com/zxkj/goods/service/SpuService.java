package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.goods.condition.ProductCondition;
import com.zxkj.goods.entity.Spu;
import com.zxkj.goods.vo.ProductVo;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务
 * @date 2022-09-02 17:22:02
 */
public interface SpuService extends IService<Spu> {

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<Spu> param);

    void saveProduct(ProductCondition product);

    ProductVo findBySupId(String id);

}
