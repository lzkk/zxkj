package com.zxkj.common.rabbitmq;

import brave.propagation.TraceContext;
import com.zxkj.common.rabbitmq.grey.GreyRabbitmqUtil;
import com.zxkj.common.rabbitmq.support.RabbitmqCorrelationData;
import com.zxkj.common.rabbitmq.support.RabbitmqMessageHelper;
import com.zxkj.common.rabbitmq.support.enums.BusiType;
import com.zxkj.common.sleuth.TraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * Rabbitmq消息发送类
 *
 * @author yuhui
 */
public class RabbitmqMessageSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RabbitmqMessageSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private String greyQueueSuffix = "";

    /**
     * 发送事件消息
     *
     * @param busiType
     * @param busiKey    请使用业务相关Id,例如:订单相关事件可用订单编号
     * @param busiObject 例如:订单相关事件可用订单对象
     * @param <T>
     */
    public <T> void send(BusiType busiType, String busiKey, Object busiObject) {
        Objects.requireNonNull(busiKey, "busiKey不能为null");
        Objects.requireNonNull(busiObject, "busiObject不能为null");
        logger.info("业务消息发送开始: {} - {}", busiKey, busiObject);
        String json = RabbitmqMessageHelper.toJson(busiKey, busiObject);
        CorrelationData correlationData = new RabbitmqCorrelationData(busiKey, busiType);
        realSend(busiType.toString(), json, null, correlationData);
    }

    /**
     * 发送延迟消息
     *
     * @param busiType
     * @param busiKey
     * @param busiObject
     * @param delayMilliseconds
     */
    public void send(BusiType busiType, String busiKey, Object busiObject, Integer delayMilliseconds) {
        Objects.requireNonNull(busiKey, "busiKey不能为null");
        Objects.requireNonNull(busiObject, "busiObject不能为null");
        logger.info("延时业务消息发送开始: {} - {}", busiKey, busiObject);
        MessagePostProcessor processor = message1 -> {
            message1.getMessageProperties().setDelay(delayMilliseconds);
            return message1;
        };
        String json = RabbitmqMessageHelper.toJson(busiKey, busiObject);
        CorrelationData correlationData = new RabbitmqCorrelationData(busiKey, busiType);
        realSend(busiType.toString(), json, processor, correlationData);
    }

    /**
     * 发送消息
     *
     * @param exchangeName
     * @param json
     * @param processor
     * @param correlationData
     */
    private void realSend(String exchangeName, String json, MessagePostProcessor processor, CorrelationData correlationData) {
        exchangeName = exchangeName + greyQueueSuffix;
        Message message = new Message(json.getBytes(), initMessageProperties());
        if (processor != null) {
            rabbitTemplate.convertAndSend(exchangeName, null, message, processor, correlationData);
        } else {
            rabbitTemplate.convertAndSend(exchangeName, null, message, correlationData);
        }
    }

    /**
     * 组装MessageProperties(包含traceId)
     *
     * @return
     */
    private MessageProperties initMessageProperties() {
        MessageProperties messageProperties = new MessageProperties();
        TraceContext traceContext = TraceUtil.getTraceContext();
        if (traceContext != null) {
            messageProperties.setHeader(TraceUtil.TRACE_ID, traceContext.traceId());
            messageProperties.setHeader(TraceUtil.TRACE_ID_HIGH, traceContext.traceIdHigh());
            messageProperties.setHeader(TraceUtil.SPAN_ID, traceContext.spanId());
        }
        return messageProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        greyQueueSuffix = GreyRabbitmqUtil.generateGreySuffix();
    }

}
