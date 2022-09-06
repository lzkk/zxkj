package com.zxkj.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.order.entity.OrderSku;
import com.zxkj.order.mapper.OrderSkuMapper;
import com.zxkj.order.service.OrderSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc  服务实现
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 13:47:42
 */
@Service
@Slf4j
public class OrderSkuServiceImpl extends ServiceImpl<OrderSkuMapper, OrderSku> implements OrderSkuService {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<OrderSku> param) {
        return this.getBaseMapper().insertAll(param);
    }

}
