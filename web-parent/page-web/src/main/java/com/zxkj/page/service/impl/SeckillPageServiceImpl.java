package com.zxkj.page.service.impl;

import com.zxkj.common.web.RespResult;
import com.zxkj.page.service.SeckillPageService;
import com.zxkj.seckill.feign.SeckillGoodsFeign;
import com.zxkj.seckill.model.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/*****
 * @Author:
 * @Description:
 ****/
@Service
public class SeckillPageServiceImpl implements SeckillPageService {

    @Value("${seckill_spuItem_out_path}")
    private String seckillSpuItemOutPath;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SeckillGoodsFeign seckillGoodsFeign;

    /****
     * 生成秒杀详情页静态页
     * @param id
     * @throws Exception
     */
    @Override
    public void html(String id) throws Exception {
        //1、创建容器对象(上下文对象)
        Context context = new Context();
        //2、设置模板数据  loadData(id)-》Map
        context.setVariables(loadData(id));
        //3、指定文件生成后存储路径
        File file = new File(seckillSpuItemOutPath, id + ".html");
        PrintWriter writer = new PrintWriter(file, "UTF-8");
        //4、执行合成生成
        templateEngine.process("seckillSpuItem", context, writer);
        writer.close();
    }

    @Override
    public void delete(String id) {
        //创建要删除的文件对象
        File file = new File(seckillSpuItemOutPath, id + ".html");
        if (file.exists()) {
            file.delete();
        }
    }

    /****
     * 数据加载
     */
    public Map<String, Object> loadData(String id) {
        //查询数据
        RespResult<SeckillGoods> goodsResp = seckillGoodsFeign.one(id);
        if (goodsResp.getResult() != null) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("item", goodsResp.getResult());
            dataMap.put("images", goodsResp.getResult().getImages().split(","));
            return dataMap;
        }
        return null;
    }
}
