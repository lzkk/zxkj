package com.zxkj.common.rabbitmq.delay;

import com.zxkj.common.rabbitmq.delay.constant.DelayQueuePrefix;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列配置类
 */
public class DelayedRabbitMqConfig {

    @Bean
    public Queue immediateQueue() {
        return new Queue(DelayQueuePrefix.DELAYED_QUEUE_NAME);
    }

    @Bean
    public CustomExchange customExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DelayQueuePrefix.DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingNotify(@Qualifier("immediateQueue") Queue queue,
                                 @Qualifier("customExchange") CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with(DelayQueuePrefix.DELAYED_ROUTING_KEY).noargs();
    }
}