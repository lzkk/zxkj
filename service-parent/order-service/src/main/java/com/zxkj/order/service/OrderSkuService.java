package com.zxkj.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.order.entity.OrderSku;

import java.util.List;

/**
 * @desc  服务
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 13:47:42
 */
public interface OrderSkuService extends IService<OrderSku> {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<OrderSku> param);

}
