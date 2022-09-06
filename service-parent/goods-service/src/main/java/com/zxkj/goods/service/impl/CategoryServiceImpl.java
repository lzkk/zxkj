package com.zxkj.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.goods.entity.Category;
import com.zxkj.goods.mapper.CategoryMapper;
import com.zxkj.goods.service.CategoryService;
import com.zxkj.goods.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yuhui
 * @version 1.0
 * @desc 商品类目 服务实现
 * @date 2022-09-02 17:22:02
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    /**
     * 商品类目 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<Category> param) {
        return this.getBaseMapper().insertAll(param);
    }

    @Override
    public List<CategoryVo> findByParentId(Integer pid) {
        //条件封装对象
        QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
        queryWrapper.eq("parent_id", pid);
        return BeanUtil.copyList(this.getBaseMapper().selectList(queryWrapper), CategoryVo.class);
    }

}
