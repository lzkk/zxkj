package com.zxkj.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.goods.entity.CategoryAttr;
import com.zxkj.goods.entity.SkuAttribute;
import com.zxkj.goods.mapper.CategoryAttrMapper;
import com.zxkj.goods.mapper.SkuAttributeMapper;
import com.zxkj.goods.service.SkuAttributeService;
import com.zxkj.goods.vo.SkuAttributeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务实现
 * @date 2022-09-02 17:22:02
 */
@Service
@Slf4j
public class SkuAttributeServiceImpl extends ServiceImpl<SkuAttributeMapper, SkuAttribute> implements SkuAttributeService {

    @Resource
    private CategoryAttrMapper categoryAttrMapper;

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<SkuAttribute> param) {
        return this.getBaseMapper().insertAll(param);
    }

    @Override
    public List<SkuAttributeVo> queryList(Integer id) {
        //根据分类ID查询Sku属性集合
        QueryWrapper<CategoryAttr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", id);
        List<CategoryAttr> categoryAttrList = categoryAttrMapper.selectList(queryWrapper);
        if (categoryAttrList != null && categoryAttrList.size() > 0) {
            List<Integer> attrIds = categoryAttrList.stream().map(CategoryAttr::getAttrId).collect(Collectors.toList());
            List<SkuAttribute> skuAttributeList = getBaseMapper().selectList(new QueryWrapper<SkuAttribute>().in("id", attrIds));
            return BeanUtil.copyList(skuAttributeList, SkuAttributeVo.class);
        }
        return null;
    }

}
