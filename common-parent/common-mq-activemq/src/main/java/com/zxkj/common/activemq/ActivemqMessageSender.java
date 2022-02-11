package com.zxkj.common.activemq;

import com.zxkj.common.activemq.grey.GreyActivemqUtil;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Message;
import javax.jms.Session;

/**
 * Activemq消息发送类
 *
 * @author yuhui
 */
public class ActivemqMessageSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ActivemqMessageSender.class);

    private String greyQueueSuffix = "";

    @Autowired
    private JmsTemplate smsJmsTemplate;

    public void send(String destinationName, final Object message) {
        destinationName = destinationName + greyQueueSuffix;
        smsJmsTemplate.convertAndSend(destinationName, message);
    }

    /**
     * @param destinationName
     * @param message
     * @param delay           延时毫秒数
     */
    public void send(String destinationName, final Object message, final long delay) {
        destinationName = destinationName + greyQueueSuffix;
        smsJmsTemplate.send(destinationName, (Session session) -> {
            Message msg = smsJmsTemplate.getMessageConverter().toMessage(message, session);
            msg.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
            return msg;
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        greyQueueSuffix = GreyActivemqUtil.generateGreySuffix();
    }
}
