package com.zxkj.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zxkj.goods.condition.BrandCondition;
import com.zxkj.goods.entity.Brand;
import com.zxkj.goods.vo.BrandVo;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 品牌表 服务
 * @date 2022-09-02 17:22:02
 */
public interface BrandService extends IService<Brand> {

    /**
     * 品牌表 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<Brand> param);

    List<BrandVo> queryList(BrandCondition condition);

    PageInfo<BrandVo> queryPageList(BrandCondition condition);

    List<BrandVo> queryByCategoryId(Integer id);

}
