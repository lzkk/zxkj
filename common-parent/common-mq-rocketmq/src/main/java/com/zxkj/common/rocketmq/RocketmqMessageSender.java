package com.zxkj.common.rocketmq;

import brave.propagation.TraceContext;
import com.zxkj.common.rocketmq.grey.GreyRocketmqUtil;
import com.zxkj.common.rocketmq.support.enums.RocketmqTopicTagEnum;
import com.zxkj.common.sleuth.TraceUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Rocketmq消息发送类
 *
 * @author yuhui
 */
public class RocketmqMessageSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RocketmqMessageSender.class);

    private String greyQueueSuffix = "";

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    /**
     * 普通消息
     *
     * @param rocketmqTopicTagEnum
     * @param msgInfo
     * @return
     */
    public SendResult send(RocketmqTopicTagEnum rocketmqTopicTagEnum, String msgInfo) {
        SendResult sendResult = null;
        try {
            String topicName = rocketmqTopicTagEnum.getTopic() + greyQueueSuffix;
            Message sendMsg = new Message(topicName, rocketmqTopicTagEnum.getTag(), "", msgInfo.getBytes());
            sendResult = defaultMQProducer.send(supplyMessageTrace(sendMsg));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sendResult;
    }

    /**
     * 事务消息
     *
     * @param rocketmqTopicTagEnum
     * @param msgInfo
     * @param transactionMQProducer
     * @return
     */
    public SendResult sendMessageInTransaction(RocketmqTopicTagEnum rocketmqTopicTagEnum, String msgInfo, TransactionMQProducer transactionMQProducer) {
        SendResult sendResult = null;
        try {
            if (transactionMQProducer == null) {
                throw new RuntimeException("transactionMQProducer未初始化，请检查！");
            }
            String topicName = rocketmqTopicTagEnum.getTopic() + greyQueueSuffix;
            Message sendMsg = new Message(topicName, rocketmqTopicTagEnum.getTag(), "", msgInfo.getBytes());
            sendResult = transactionMQProducer.sendMessageInTransaction(supplyMessageTrace(sendMsg), msgInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sendResult;
    }

    /**
     * 填充trace数据
     *
     * @return
     */
    private Message supplyMessageTrace(Message sendMsg) {
        TraceContext traceContext = TraceUtil.getTraceContext();
        if (traceContext != null) {
            sendMsg.getProperties().put(TraceUtil.TRACE_ID, String.valueOf(traceContext.traceId()));
            sendMsg.getProperties().put(TraceUtil.TRACE_ID_HIGH, String.valueOf(traceContext.traceIdHigh()));
            sendMsg.getProperties().put(TraceUtil.SPAN_ID, String.valueOf(traceContext.spanId()));
        }
        return sendMsg;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        greyQueueSuffix = GreyRocketmqUtil.generateGreySuffix();
    }
}
