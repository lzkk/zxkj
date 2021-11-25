package com.zxkj.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.cart.condition.CartCondition;
import com.zxkj.cart.feign.CartFeign;
import com.zxkj.cart.vo.CartVo;
import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.util.BeanUtil;
import com.zxkj.common.web.JsonUtil;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.feign.SkuFeign;
import com.zxkj.goods.model.Sku;
import com.zxkj.order.mapper.OrderMapper;
import com.zxkj.order.mapper.OrderRefundMapper;
import com.zxkj.order.mapper.OrderSkuMapper;
import com.zxkj.order.model.Order;
import com.zxkj.order.model.OrderRefund;
import com.zxkj.order.model.OrderSku;
import com.zxkj.order.service.OrderService;
import com.zxkj.order.vo.OrderSkuVo;
import com.zxkj.seckill.feign.SeckillGoodsFeign;
import com.zxkj.seckill.model.SeckillGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*****
 * @Author:
 * @Description:
 ****/
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderSkuMapper orderSkuMapper;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SeckillGoodsFeign seckillGoodsFeign;

    /****
     * 退款申请
     * @param orderRefund
     * @return
     */
    @Override
    public int refund(OrderRefund orderRefund) {
        //1记录退款申请
        orderRefundMapper.insert(orderRefund);

        //2修改订单状态
        Order order = new Order();
        order.setOrderStatus(4);    //申请退款

        //构建条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>();
        queryWrapper.eq("id", orderRefund.getOrderNo()); //订单ID
        queryWrapper.eq("username", orderRefund.getUsername()); //用户名
        queryWrapper.eq("order_status", 1);
        queryWrapper.eq("pay_status", 1);
        int count = orderMapper.update(order, queryWrapper);
        return count;
    }

    /***
     * 添加订单
     */
//    @GlobalTransactional
    @Override
    public Boolean add(Order order) {
        //数据完善
        order.setId(IdWorker.getIdStr());   //ID
        order.setCreateTime(new Date());    //创建时间
        order.setUpdateTime(order.getCreateTime());

        //1、查询购物车数据
        List<CartVo> carts = cartFeign.list(order.getCartIds()).getResultWithException();
        if (carts == null || carts.size() == 0) {
            throw new BusinessException("请选择商品再下单");
        }

        //2、递减库存
        List<CartCondition> cartConditions = BeanUtil.copyList(carts, CartCondition.class);
        skuFeign.dcount(cartConditions).getResultWithException();

        //3、添加订单明细
        int totalNum = 0;
        int moneys = 0;
        for (CartVo cart : carts) {
            //将Cart转成OrderSku
            OrderSku orderSku = BeanUtil.copyObject(cart, OrderSku.class);
            orderSku.setId(IdWorker.getIdStr());
            orderSku.setOrderId(order.getId()); //提前赋值
            orderSku.setMoney(orderSku.getPrice() * orderSku.getNum());

            //添加
            orderSkuMapper.insert(orderSku);

            //统计计算
            totalNum += orderSku.getNum();
            moneys += orderSku.getMoney();
        }

        //4、添加订单
        order.setTotalNum(totalNum);
        order.setMoneys(moneys);
        orderMapper.insert(order);

//        int q = 10 / 0;

        //5、删除购物车数据
        cartFeign.delete(order.getCartIds());
        return true;
    }

    /****
     * 支付成功后状态修改
     * @param id
     * @return
     */
    @Override
    public int updateAfterPayStatus(String id) {
        //修改后的状态
        Order order = new Order();
        order.setId(id);
        order.setOrderStatus(1);    // 待发货
        order.setPayStatus(1);  //已支付

        //修改条件
        QueryWrapper<Order> queryWrapper = new QueryWrapper<Order>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("order_status", 0);
        queryWrapper.eq("pay_status", 0);
        return orderMapper.update(order, queryWrapper);
    }

    @Override
    public Order getById(String id) {
        return orderMapper.selectById(id);
    }

    @Override
    public Order getByUserName(String username) {
        return orderMapper.getByUserName(username);
    }

    @Override
    public List<OrderSkuVo> getOrderSkuByUserName(String username) {
        List<OrderSkuVo> orderSkuVoList = orderSkuMapper.getOrderSkuByUserName(username);
        if (orderSkuVoList != null && orderSkuVoList.size() > 0) {
            for (OrderSkuVo OrderSkuVo : orderSkuVoList) {
                RespResult<Sku> respResult = skuFeign.one(OrderSkuVo.getSkuId());
                log.info(JsonUtil.toJsonString(respResult));
            }
        }
        return orderSkuVoList;
    }

    @Override
    public List<OrderSkuVo> getCart(List<String> ids) {
        List<OrderSkuVo> orderSkuVoList = new ArrayList<>();
        RespResult<List<CartVo>> respResult = cartFeign.list(ids);
        log.info(JsonUtil.toJsonString(respResult));
        return orderSkuVoList;
    }

    @Override
    public void ribbonTest() {
        RespResult<Sku> respResult1 = skuFeign.one("1318596430360813570");
        log.info("1--" + JsonUtil.toJsonString(respResult1));
        RespResult<List<CartVo>> respResult2 = cartFeign.list(Arrays.asList("gpNo1226524616676216832"));
        log.info("2--" + JsonUtil.toJsonString(respResult2));
        RespResult<SeckillGoods> respResult3 = seckillGoodsFeign.one("111");
        log.info("3--" + JsonUtil.toJsonString(respResult3));
    }
}
