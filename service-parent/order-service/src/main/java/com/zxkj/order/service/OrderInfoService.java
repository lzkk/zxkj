package com.zxkj.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxkj.goods.vo.SkuVo;
import com.zxkj.order.condition.OrderInfoCondition;
import com.zxkj.order.entity.OrderInfo;
import com.zxkj.order.vo.OrderInfoVo;
import com.zxkj.order.vo.OrderSkuVo;

import java.util.List;

/**
 * @desc  服务
 *
 * @author yuhui
 * @version 1.0
 * @date 2022-09-05 13:47:42
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     *  批量新增重写
     * @param param model集合
     * @return Boolean
     */
    Boolean saveBatch(List<OrderInfo> param);

    /***
     * 添加订单
     */
    Boolean add(OrderInfoCondition order);

    /***
     * 支付成功修改状态
     */
    int updateAfterPayStatus(String id);

    OrderInfoVo getById(String id);

    OrderInfoVo getByUserName(String username);

    List<OrderSkuVo> getOrderSkuByUserName(String username);

    List<OrderSkuVo> getCart(List<String> ids);

    SkuVo ribbonTest(String skuId);

}
