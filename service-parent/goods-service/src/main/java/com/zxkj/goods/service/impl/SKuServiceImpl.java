package com.zxkj.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.cart.condition.CartCondition;
import com.zxkj.common.datasource.support.Readonly;
import com.zxkj.common.exception.BusinessException;
import com.zxkj.goods.mapper.AdItemsMapper;
import com.zxkj.goods.mapper.SkuMapper;
import com.zxkj.goods.mapper.SpuMapper;
import com.zxkj.goods.model.AdItems;
import com.zxkj.goods.model.Sku;
import com.zxkj.goods.model.Spu;
import com.zxkj.goods.service.SKuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SKuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SKuService {

    @Autowired
    private AdItemsMapper adItemsMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Override
    public Sku selectOne(String id) {
        return skuMapper.selectById(id);
    }

    @Readonly
    @Override
    public Sku selectOne2(String id) {
        return skuMapper.selectById(id);
    }


    /***
     * 库存递减
     * @param carts
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int dcount(List<CartCondition> carts) {
        for (CartCondition cart : carts) {
            //库存递减
            int dcount = skuMapper.dcount(cart.getSkuId(), cart.getNum());
            System.out.println("dcount:" + dcount);
            if (dcount <= 0) {
                throw new RuntimeException("库存不足！");
            }
            return dcount;
        }
        return 0;
    }

    /***
     * 根据推广产品分类ID查询指定分类下的产品列表
     * @param id
     * @return
     * ad-items-skus::1
     */
    @Override
    public List<Sku> typeSkuItems(Integer id) {
        //1.查询当前分类下的所有列表信息
        QueryWrapper<AdItems> adItemsQueryWrapper = new QueryWrapper<AdItems>();
        adItemsQueryWrapper.eq("type", id);
        List<AdItems> adItems = adItemsMapper.selectList(adItemsQueryWrapper);

        //2.根据推广列表查询产品列表信息
        List<String> skuids = adItems.stream().map(adItem -> adItem.getSkuId()).collect(Collectors.toList());
        return skuids == null || skuids.size() <= 0 ? null : skuMapper.selectBatchIds(skuids);
    }

    /***
     * 根据分类id删除指定推广数据
     * @param id
     * @return
     */
    @Override
    public void delTypeSkuItems(Integer id) {
    }

    /****
     * 修改缓存
     * @param id
     * @return
     */
    @Override
    public List<Sku> updateTypeSkuItems(Integer id) {
        //1.查询当前分类下的所有列表信息
        QueryWrapper<AdItems> adItemsQueryWrapper = new QueryWrapper<AdItems>();
        adItemsQueryWrapper.eq("type", id);
        List<AdItems> adItems = adItemsMapper.selectList(adItemsQueryWrapper);

        //2.根据推广列表查询产品列表信息
        List<String> skuids = adItems.stream().map(adItem -> adItem.getSkuId()).collect(Collectors.toList());
        return skuids == null || skuids.size() <= 0 ? null : skuMapper.selectBatchIds(skuids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTest(String skuId, String spuId) {
        Sku sku = new Sku();
        sku.setUpdateTime(new Date());
        sku.setId(skuId);
        int res = skuMapper.updateById(sku);
        if (res != 1) {
            throw new BusinessException("sku update error");
        }
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setIntro(System.currentTimeMillis() + "");
        int res2 = spuMapper.updateById(spu);
        if (res2 != 1) {
            throw new BusinessException("spu update error");
        }
        System.out.println("ok");

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                super.afterCompletion(status);
                if (status == STATUS_COMMITTED) {
                    Sku sku = new Sku();
                    sku.setSpuId(spuId);
                    sku.setId(skuId);
                    int res = skuMapper.updateById(sku);
                    System.out.println("yingxiang-" + res);
                }
            }
        });

    }

}
