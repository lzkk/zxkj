package com.zxkj.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.goods.condition.BrandCondition;
import com.zxkj.goods.entity.Brand;
import com.zxkj.goods.entity.CategoryBrand;
import com.zxkj.goods.mapper.BrandMapper;
import com.zxkj.goods.mapper.CategoryBrandMapper;
import com.zxkj.goods.service.BrandService;
import com.zxkj.goods.vo.BrandVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuhui
 * @version 1.0
 * @desc 品牌表 服务实现
 * @date 2022-09-02 17:22:02
 */
@Service
@Slf4j
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Resource
    private CategoryBrandMapper categoryBrandMapper;

    /**
     * 品牌表 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<Brand> param) {
        return this.getBaseMapper().insertAll(param);
    }

    /****
     * 条件查询
     * return List<Brand>
     */
    @Override
    public List<BrandVo> queryList(BrandCondition condition) {
        //条件包装对象
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        //根据name查询品牌
        queryWrapper.like("name", condition.getName());
        //根据initial查询
        queryWrapper.eq("initial", condition.getInitial());
        List<Brand> brands = this.getBaseMapper().selectList(queryWrapper);
        return BeanUtil.copyList(brands, BrandVo.class);
    }

    /****
     * 条件分页查询
     * return Page<Brand>
     */
    @Override
    public PageInfo<BrandVo> queryPageList(BrandCondition condition) {
        return PageHelper.startPage(condition.getPageNum(), condition.getPageSize()).doSelectPageInfo(() -> {
            //条件包装对象
            QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
            //根据name查询品牌
            queryWrapper.like("name", condition.getName());
            BeanUtil.copyList(getBaseMapper().selectList(queryWrapper), BrandVo.class);
        });
    }

    /****
     * 根据分类ID查询品牌集合
     * @param id:分类ID
     * @return
     */
    @Override
    public List<BrandVo> queryByCategoryId(Integer id) {
        //根据分类ID查询品牌ID集合
        QueryWrapper<CategoryBrand> queryWrapper = new QueryWrapper<>();
        //根据name查询品牌
        queryWrapper.eq("category_id", id);
        List<CategoryBrand> categoryBrandList = categoryBrandMapper.selectList(queryWrapper);
        //根据品牌ID集合查询品牌集合
        if (categoryBrandList != null && categoryBrandList.size() > 0) {
            List<Integer> brandIds = categoryBrandList.stream().map(CategoryBrand::getBrandId).collect(Collectors.toList());
            List<Brand> brandList = getBaseMapper().selectList(new QueryWrapper<Brand>().in("id", brandIds));
            return BeanUtil.copyList(brandList, BrandVo.class);
        }
        return null;
    }

}
