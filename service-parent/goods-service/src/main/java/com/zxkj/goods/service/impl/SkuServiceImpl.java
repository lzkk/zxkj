package com.zxkj.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.cart.condition.CartCondition;
import com.zxkj.common.datasource.support.Readonly;
import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.goods.entity.AdItems;
import com.zxkj.goods.entity.Sku;
import com.zxkj.goods.entity.Spu;
import com.zxkj.goods.mapper.AdItemsMapper;
import com.zxkj.goods.mapper.SkuMapper;
import com.zxkj.goods.mapper.SpuMapper;
import com.zxkj.goods.service.SkuService;
import com.zxkj.goods.vo.SkuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuhui
 * @version 1.0
 * @desc 商品表 服务实现
 * @date 2022-09-02 17:22:02
 */
@Service
@Slf4j
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    @Resource
    private AdItemsMapper adItemsMapper;

    @Resource
    private SpuMapper spuMapper;

    /**
     * 商品表 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<Sku> param) {
        return this.getBaseMapper().insertAll(param);
    }

    @Override
    public SkuVo selectOne(String id) {
        return BeanUtil.copyObject(this.getBaseMapper().selectById(id), SkuVo.class);
    }

    @Readonly
    @Override
    public SkuVo selectOne2(String id) {
        return BeanUtil.copyObject(this.getBaseMapper().selectById(id), SkuVo.class);
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
            int dcount = this.getBaseMapper().dcount(cart.getSkuId(), cart.getNum());
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
    public List<SkuVo> typeSkuItems(Integer id) {
        //1.查询当前分类下的所有列表信息
        QueryWrapper<AdItems> adItemsQueryWrapper = new QueryWrapper<>();
        adItemsQueryWrapper.eq("type", id);
        List<AdItems> adItems = adItemsMapper.selectList(adItemsQueryWrapper);
        //2.根据推广列表查询产品列表信息
        List<String> skuids = adItems.stream().map(adItem -> adItem.getSkuId()).collect(Collectors.toList());
        return skuids == null || skuids.size() <= 0 ? null : BeanUtil.copyList(getBaseMapper().selectBatchIds(skuids), SkuVo.class);
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
    public List<SkuVo> updateTypeSkuItems(Integer id) {
        //1.查询当前分类下的所有列表信息
        QueryWrapper<AdItems> adItemsQueryWrapper = new QueryWrapper<>();
        adItemsQueryWrapper.eq("type", id);
        List<AdItems> adItems = adItemsMapper.selectList(adItemsQueryWrapper);

        //2.根据推广列表查询产品列表信息
        List<String> skuids = adItems.stream().map(adItem -> adItem.getSkuId()).collect(Collectors.toList());
        return skuids == null || skuids.size() <= 0 ? null : BeanUtil.copyList(getBaseMapper().selectBatchIds(skuids), SkuVo.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTest(String skuId, String spuId) {
        Sku sku = new Sku();
        sku.setUpdateTime(new Date());
        sku.setId(skuId);
        int res = getBaseMapper().updateById(sku);
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
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCompletion(int status) {
                super.afterCompletion(status);
                if (status == STATUS_COMMITTED) {
                    Sku sku = new Sku();
                    sku.setSpuId(spuId);
                    sku.setId(skuId);
                    int res = getBaseMapper().updateById(sku);
                    log.info("yingxiang-" + res);
                }
            }
        });

    }


}
