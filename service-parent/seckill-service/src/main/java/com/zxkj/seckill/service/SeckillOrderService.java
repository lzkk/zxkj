package com.zxkj.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.seckill.entity.SeckillOrder;

import java.util.List;
import java.util.Map;

/**
 * @desc  服务
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 14:37:18
 */
public interface SeckillOrderService extends IService<SeckillOrder> {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<SeckillOrder> param);

    int add(Map<String, Object> dataMap);

}
