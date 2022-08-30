//package com.zxkj.goods.listener;
//
//import com.zxkj.common.activemq.ActivemqMessageListener;
//import com.zxkj.common.activemq.support.enums.ActivemqDestinationEnum;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ActivemqConsumerService {
//    protected final Logger logger = LoggerFactory.getLogger(ActivemqConsumerService.class);
//
//    @ActivemqMessageListener(destination = ActivemqDestinationEnum.ACTIVEMQ_TEST)
//    public void receive(String message) throws Exception {
//        logger.info("从队列获取到数据：" + message);
//    }
//
////    @JmsListener(destination = ActivemqDestinationEnum.ACTIVEMQ_TEST)
////    public void receive2(String message) throws Exception {
////        logger.info("从队列获取到数据2：" + message);
////    }
//
//}
