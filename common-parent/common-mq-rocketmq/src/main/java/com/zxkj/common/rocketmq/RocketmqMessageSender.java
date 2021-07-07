package com.zxkj.common.rocketmq;

import com.zxkj.common.rocketmq.support.RocketmqTopicTagEnum;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Rocketmq消息发送类
 *
 * @author yuhui
 */
public class RocketmqMessageSender {
    private static final Logger logger = LoggerFactory.getLogger(RocketmqMessageSender.class);

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
            Message sendMsg = new Message(rocketmqTopicTagEnum.getTopic(), rocketmqTopicTagEnum.getTag(), "", msgInfo.getBytes());
            sendResult = defaultMQProducer.send(sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
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
            Message sendMsg = new Message(rocketmqTopicTagEnum.getTopic(), rocketmqTopicTagEnum.getTag(), "", msgInfo.getBytes());
            sendResult = transactionMQProducer.sendMessageInTransaction(sendMsg, msgInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendResult;
    }

}
