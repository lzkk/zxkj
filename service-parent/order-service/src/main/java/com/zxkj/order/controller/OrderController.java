package com.zxkj.order.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.zxkj.common.rocketmq.RocketmqMessageSender;
import com.zxkj.common.rocketmq.support.RocketmqTopicTagEnum;
import com.zxkj.common.web.RespResult;
import com.zxkj.order.model.Order;
import com.zxkj.order.model.OrderRefund;
import com.zxkj.order.pay.WeixinPayParam;
import com.zxkj.order.service.OrderService;
import com.zxkj.order.vo.OrderSkuVo;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 订单信息Controller
 *
 * @author ：yuhui
 * @date ：Created in 2020/8/5 15:26
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    @Autowired
    private WeixinPayParam weixinPayParam;

//    @Autowired
//    private RocketmqMessageSender rocketmqMessageSender;
//
//    @Resource(name = "transactionMQProducer1")
//    private TransactionMQProducer transactionMQProducer1;
//
//    @Resource(name = "transactionMQProducer2")
//    private TransactionMQProducer transactionMQProducer2;
//
//    /****
//     * 申请取消订单（模拟测试退款的订单）
//     */
//    @PutMapping(value = "/refund/{id}")
//    public RespResult refund(@PathVariable(value = "id") String id, HttpServletRequest request) throws Exception {
//        //用户名
//        String username = "gp";
//        //查询订单，是否符合退款要求
//        Order order = orderService.getById(id);
//        if (order.getPayStatus().intValue() == 1 && order.getOrderStatus().intValue() == 1) {
//            //添加退款记录,更新订单状态
//            OrderRefund orderRefund = new OrderRefund(
//                    IdWorker.getIdStr(),
//                    id,
//                    1,
//                    null,
//                    username,
//                    0,//申请退款
//                    new Date(),
//                    order.getMoneys()
//            );
//            orderService.refund(orderRefund);
//
//            //向MQ发消息（申请退款）  out_trade_no（订单号）  out_refund_no（退款订单号）  total_fee（订单金额）  refund_fee（退款金额）
//            String msgInfo = weixinPayParam.weixinRefundParam(orderRefund);
//            SendResult transactionSendResult = rocketmqMessageSender.sendMessageInTransaction(RocketmqTopicTagEnum.TOPIC_TAG_TEST3, msgInfo, transactionMQProducer1);
//            if (transactionSendResult != null && transactionSendResult.getSendStatus() == SendStatus.SEND_OK) {
//                return RespResult.ok();
//            }
//        }
//        //不符合直接返回错误
//        return RespResult.error("当前订单不符合取消操作要求！");
//    }
//
//    @PostMapping(value = "/testTransaction1")
//    public RespResult testTransaction1(@RequestBody Order order) throws Exception {
//        //用户名字
//        order.setUsername("gp");
//        SendResult transactionSendResult = rocketmqMessageSender.sendMessageInTransaction(RocketmqTopicTagEnum.TOPIC_TAG_TEST2, order.getId(), transactionMQProducer1);
//        if (transactionSendResult != null && transactionSendResult.getSendStatus() == SendStatus.SEND_OK) {
//            return RespResult.ok();
//        }
//        return RespResult.ok();
//    }
//
//    @PostMapping(value = "/testTransaction2")
//    public RespResult testTransaction2(@RequestBody Order order) throws Exception {
//        //用户名字
//        order.setUsername("gp");
//        SendResult transactionSendResult = rocketmqMessageSender.sendMessageInTransaction(RocketmqTopicTagEnum.TOPIC_TAG_TEST2, order.getId(), transactionMQProducer2);
//        if (transactionSendResult != null && transactionSendResult.getSendStatus() == SendStatus.SEND_OK) {
//            return RespResult.ok();
//        }
//        return RespResult.ok();
//    }
//
//    @PostMapping(value = "/test")
//    public RespResult test(@RequestBody Order order) throws Exception {
//        //用户名字
//        order.setUsername("gp");
//        SendResult transactionSendResult = rocketmqMessageSender.send(RocketmqTopicTagEnum.TOPIC_TAG_TEST, order.getId());
//        if (transactionSendResult != null && transactionSendResult.getSendStatus() == SendStatus.SEND_OK) {
//            return RespResult.ok();
//        }
//        return RespResult.ok();
//    }

    /***
     * 添加订单
     */
    @PostMapping(value = "/add")
    public RespResult add(@RequestBody Order order, HttpServletRequest request) throws Exception {
        //用户名字
        order.setUsername("gp");
        //下单
        Boolean bo = orderService.add(order);
        String result = weixinPayParam.weixinParam(order, request);
        return bo ? RespResult.ok(result) : RespResult.error();
    }

    @GetMapping(value = "/getById")
    public RespResult getById(@RequestParam(value = "id") String id, HttpServletRequest request) throws Exception {
        Order order = orderService.getById(id);
        return RespResult.ok(order);
    }

    @GetMapping(value = "/getByUserName")
    public RespResult getByUserName(@RequestParam(value = "userName") String userName, HttpServletRequest request) throws Exception {
        Order order = orderService.getByUserName(userName);
        return RespResult.ok(order);
    }

    @GetMapping(value = "/getOrderSkuByUserName")
    public RespResult<List<OrderSkuVo>> getOrderSkuByUserName(@RequestParam(value = "userName") String userName, HttpServletRequest request) throws Exception {
        List<OrderSkuVo> orderSkuVoList = orderService.getOrderSkuByUserName(userName);
        return RespResult.ok(orderSkuVoList);
    }

    @GetMapping(value = "/getCartTest")
    public RespResult<List<OrderSkuVo>> getCart(@RequestBody List<String> ids) throws Exception {
        List<OrderSkuVo> orderSkuVoList = orderService.getCart(ids);
        return RespResult.ok(orderSkuVoList);
    }

    @GetMapping(value = "/ribbonTest")
    public RespResult<Boolean> ribbonTest(@RequestParam(value = "info") String info) throws Exception {
        orderService.ribbonTest();
        return RespResult.ok(true);
    }

}
