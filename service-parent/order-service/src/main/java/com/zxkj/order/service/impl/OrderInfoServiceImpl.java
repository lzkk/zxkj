package com.zxkj.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxkj.cart.condition.CartCondition;
import com.zxkj.cart.feign.CartFeign;
import com.zxkj.cart.vo.CartVo;
import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.util.bean.BeanUtil;
import com.zxkj.common.web.JsonUtil;
import com.zxkj.common.web.RespResult;
import com.zxkj.goods.feign.SkuFeign;
import com.zxkj.goods.vo.SkuVo;
import com.zxkj.order.condition.OrderInfoCondition;
import com.zxkj.order.entity.OrderInfo;
import com.zxkj.order.entity.OrderSku;
import com.zxkj.order.mapper.OrderInfoMapper;
import com.zxkj.order.mapper.OrderSkuMapper;
import com.zxkj.order.service.OrderInfoService;
import com.zxkj.order.vo.OrderInfoVo;
import com.zxkj.order.vo.OrderSkuVo;
import com.zxkj.seckill.feign.SeckillGoodsFeign;
import com.zxkj.seckill.vo.SeckillGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author yuhui
 * @version 1.0
 * @desc 服务实现
 * @date 2022-09-05 13:47:42
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Resource
    private OrderSkuMapper orderSkuMapper;

    @Resource
    private SkuFeign skuFeign;

    @Resource
    private CartFeign cartFeign;

    @Resource
    private SeckillGoodsFeign seckillGoodsFeign;

    @Resource
    private Executor executor;

    /**
     * 批量新增重写
     *
     * @param param model集合
     * @return Boolean
     */
    @Override
    public Boolean saveBatch(List<OrderInfo> param) {
        return this.getBaseMapper().insertAll(param);
    }

    /***
     * 添加订单
     */
//    @GlobalTransactional
    @Override
    public Boolean add(OrderInfoCondition orderInfoCondition) {
        OrderInfo order = new OrderInfo();
        BeanUtils.copyProperties(orderInfoCondition, order);
        //数据完善
        order.setId(IdWorker.getIdStr());   //ID
        order.setCreateTime(new Date());    //创建时间
        order.setUpdateTime(order.getCreateTime());

        //1、查询购物车数据
        List<CartVo> carts = cartFeign.list(orderInfoCondition.getCartIds()).getDataWithException();
        if (carts == null || carts.size() == 0) {
            throw new BusinessException("请选择商品再下单");
        }

        //2、递减库存
        List<CartCondition> cartConditions = BeanUtil.copyList(carts, CartCondition.class);
        skuFeign.dcount(cartConditions).getDataWithException();

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
        this.getBaseMapper().insert(order);

//        int q = 10 / 0;

        //5、删除购物车数据
        cartFeign.delete(orderInfoCondition.getCartIds());
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
        OrderInfo order = new OrderInfo();
        order.setId(id);
        order.setOrderStatus(1);    // 待发货
        order.setPayStatus(1);  //已支付

        //修改条件
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("order_status", 0);
        queryWrapper.eq("pay_status", 0);
        return this.getBaseMapper().update(order, queryWrapper);
    }

    @Override
    public OrderInfoVo getById(String id) {
        return BeanUtil.copyObject(this.getBaseMapper().selectById(id), OrderInfoVo.class);
    }

    @Override
    public OrderInfoVo getByUserName(String username) {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        OrderInfo orderInfo = this.getBaseMapper().selectOne(queryWrapper);
        return BeanUtil.copyObject(orderInfo, OrderInfoVo.class);
    }

    @Override
    public List<OrderSkuVo> getOrderSkuByUserName(String username) {
        List<OrderSkuVo> orderSkuVoList = orderSkuMapper.getOrderSkuByUserName(username);
        if (orderSkuVoList != null && orderSkuVoList.size() > 0) {
            for (OrderSkuVo OrderSkuVo : orderSkuVoList) {
                RespResult<SkuVo> respResult = skuFeign.one(OrderSkuVo.getSkuId());
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
        RespResult<SkuVo> respResult1 = skuFeign.one("1318596430360813570");
        log.info("1--" + JsonUtil.toJsonString(respResult1));

        Thread t = new Thread(() -> {
            RespResult<List<CartVo>> respResult2 = cartFeign.list(Arrays.asList("gp1318596430398562305"));
            log.info("2--" + JsonUtil.toJsonString(respResult2));
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.execute((Runnable) () -> {
            RespResult<SeckillGoodsVo> respResult3 = seckillGoodsFeign.one("111");
            log.info("3--" + JsonUtil.toJsonString(respResult3));
        });

    }
}
