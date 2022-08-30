package com.zxkj.common.activemq;

import brave.propagation.TraceContext;
import com.zxkj.common.activemq.grey.GreyActivemqUtil;
import com.zxkj.common.sleuth.TraceUtil;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
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

    public void send(String destinationName, Object message) {
        send(destinationName, message, null);
    }

    /**
     * @param destinationName
     * @param message
     * @param delay           延时毫秒数
     */
    public void send(String destinationName, Object message, Long delay) {
        destinationName = destinationName + greyQueueSuffix;
        smsJmsTemplate.send(destinationName, (Session session) -> {
            Message msg = smsJmsTemplate.getMessageConverter().toMessage(message, session);
            supplyMessageTrace(msg);
            if (delay != null) {
                msg.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
            }
            return msg;
        });
    }

    /**
     * 填充trace参数
     */
    private void supplyMessageTrace(Message msg) throws JMSException {
        TraceContext traceContext = TraceUtil.getTraceContext();
        if (traceContext != null) {
            msg.setLongProperty(TraceUtil.TRACE_ID, traceContext.traceId());
            msg.setLongProperty(TraceUtil.TRACE_ID_HIGH, traceContext.traceIdHigh());
            msg.setLongProperty(TraceUtil.SPAN_ID, traceContext.spanId());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        greyQueueSuffix = GreyActivemqUtil.generateGreySuffix();
    }
}
