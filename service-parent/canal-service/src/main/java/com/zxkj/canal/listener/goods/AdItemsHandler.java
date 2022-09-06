package com.zxkj.canal.listener.goods;

import com.zxkj.canal.model.goods.AdItemsModel;
import com.zxkj.goods.feign.SkuFeign;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

import javax.annotation.Resource;

/*****
 * @Author:
 * @Description:
 ****/
@Component
@CanalTable(value = "ad_items")
public class AdItemsHandler implements EntryHandler<AdItemsModel> {

    @Resource
    private SkuFeign skuFeign;

    /****
     * 数据库增加数据，执行该方法
     * @param adItems
     */
    @Override
    public void insert(AdItemsModel adItems) {
        //重新加载缓存
        skuFeign.updateTypeItems(adItems.getType());
    }

    /****
     * 数据库修改数据，执行该方法
     */
    @Override
    public void update(AdItemsModel before, AdItemsModel after) {
        if (before.getType().intValue() != after.getType().intValue()) {
            //重新加载变更前分类的ID对应的推广产品
            skuFeign.updateTypeItems(before.getType());
        }

        //重新加载缓存
        skuFeign.updateTypeItems(after.getType());
    }

    /****
     * 数据库删除数据，执行该方法
     */
    @Override
    public void delete(AdItemsModel adItems) {
        skuFeign.delTypeItems(adItems.getType());
    }
}
