package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.goods.entity.SkuAttribute;
import com.zxkj.goods.vo.SkuAttributeVo;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务
 * @date 2022-09-02 17:22:02
 */
public interface SkuAttributeService extends IService<SkuAttribute> {

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<SkuAttribute> param);

    List<SkuAttributeVo> queryList(Integer id);

}
