package com.zxkj.common.rabbitmq;

import com.zxkj.common.cache.constant.CacheKeyPrefix;
import com.zxkj.common.cache.redis.Cache;
import com.zxkj.common.rabbitmq.delay.constant.DelayQueuePrefix;
import com.zxkj.common.rabbitmq.grey.GreyRabbitUtil;
import com.zxkj.common.rabbitmq.support.RabbitmqCorrelationData;
import com.zxkj.common.rabbitmq.support.RabbitmqMessageHelper;
import com.zxkj.common.rabbitmq.support.enums.BusiType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    private Cache cache;

    private String greyQueueSuffix = "";

    /**
     * 发送事件消息
     *
     * @param busiType
     * @param busiKey    请使用业务相关Id,例如:订单相关事件可用订单编号
     * @param busiObject 例如:订单相关事件可用订单对象
     * @param <T>
     */
    public <T> void send(BusiType busiType, String busiKey, T busiObject) {
        Objects.requireNonNull(busiKey, "busiKey不能为null");
        Objects.requireNonNull(busiObject, "busiObject不能为null");
        String json = RabbitmqMessageHelper.toJson(busiKey, busiObject);
        String idKey = CacheKeyPrefix.BUSI_MESSAGE_ID + busiType.toString();
        String bodyKey = CacheKeyPrefix.BUSI_MESSAGE_BODY + busiType.toString();
        cache.zadd(idKey, System.currentTimeMillis(), busiKey);
        cache.hset(bodyKey, busiKey, json);
        logger.info("业务消息发送开始: {} - {}", busiKey, busiObject);
        String exchangeName = busiType.toString() + greyQueueSuffix;
        rabbitTemplate.convertAndSend(exchangeName, null, json, new RabbitmqCorrelationData(busiKey, busiType));
    }

    /**
     * 发送延迟消息
     *
     * @param msg
     * @param delayTime
     */
    public void sendDelayMsg(String msg, Integer delayTime) {
        rabbitTemplate.convertAndSend(DelayQueuePrefix.DELAYED_EXCHANGE_NAME, DelayQueuePrefix.DELAYED_ROUTING_KEY, msg, a -> {
            a.getMessageProperties().setDelay(delayTime);
            return a;
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        greyQueueSuffix = GreyRabbitUtil.generateGreySuffix();
    }
}
