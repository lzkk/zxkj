package com.zxkj.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.goods.condition.ProductCondition;
import com.zxkj.goods.condition.SkuCondition;
import com.zxkj.goods.condition.SpuCondition;
import com.zxkj.goods.entity.Brand;
import com.zxkj.goods.entity.Category;
import com.zxkj.goods.entity.Sku;
import com.zxkj.goods.entity.Spu;
import com.zxkj.goods.mapper.BrandMapper;
import com.zxkj.goods.mapper.CategoryMapper;
import com.zxkj.goods.mapper.SkuMapper;
import com.zxkj.goods.mapper.SpuMapper;
import com.zxkj.goods.service.SpuService;
import com.zxkj.goods.vo.ProductVo;
import com.zxkj.goods.vo.SkuVo;
import com.zxkj.goods.vo.SpuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务实现
 * @date 2022-09-02 17:22:02
 */
@Service
@Slf4j
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private BrandMapper brandMapper;

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<Spu> param) {
        return this.getBaseMapper().insertAll(param);
    }

    /***
     * 产品保存
     * @param product
     */
    @Override
    public void saveProduct(ProductCondition product) {
        //1.保存Spu
        SpuCondition spuCondition = product.getSpu();
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuCondition, spu);
        if (StringUtils.isEmpty(spuCondition.getId())) {
            spu.setIsMarketable(1); //已上架
            spu.setIsDelete(0); //未删除
            spu.setStatus(1);   //审核已通过
            this.getBaseMapper().insert(spu);
        } else {
            //修改
            this.getBaseMapper().updateById(spu);
            //删除Sku集合
            skuMapper.delete(new QueryWrapper<Sku>().eq("spu_id", spu.getId()));
        }

        //2.保存List<Sku>
        Date date = new Date();
        Category category = categoryMapper.selectById(spu.getCategoryThreeId());
        Brand brand = brandMapper.selectById(spu.getBrandId());
        for (SkuCondition skuCondition : product.getSkus()) {
            Sku sku = new Sku();
            BeanUtils.copyProperties(skuCondition, sku);
            //SKU名称
            // {"适合人群":"有一定java基础的人","书籍分类":"科技"}
            String name = spu.getName();
            Map<String, String> skuattrMap = JSON.parseObject(sku.getSkuAttribute(), Map.class);
            for (Map.Entry<String, String> entry : skuattrMap.entrySet()) {
                name += "  " + entry.getValue();
            }
            sku.setName(name);
            //创建时间
            sku.setCreateTime(date);
            //修改时间
            sku.setUpdateTime(date);
            //分类ID
            sku.setCategoryId(spu.getCategoryThreeId());
            //分类名字
            sku.setBrandName(brand.getName());
            //品牌ID
            sku.setBrandId(spu.getBrandId());
            //品牌名字
            sku.setCategoryName(category.getName());
            //spuid
            sku.setSpuId(spu.getId());
            //状态 商品状态 1-正常，2-下架，3-删除
            sku.setStatus(1);
            //添加
            skuMapper.insert(sku);
        }
    }

    /****
     * 根据spuid查询Product
     * @param id
     * @return
     */
    @Override
    public ProductVo findBySupId(String id) {
        //查询Spu
        Spu spu = this.getBaseMapper().selectById(id);
        //查询Sku集合
        QueryWrapper<Sku> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spu_id", id);
        List<Sku> skus = skuMapper.selectList(queryWrapper);

        //Product
        ProductVo product = new ProductVo();
        product.setSpu(BeanUtil.copyObject(spu, SpuVo.class));
        product.setSkus(BeanUtil.copyList(skus, SkuVo.class));
        return product;
    }

}
