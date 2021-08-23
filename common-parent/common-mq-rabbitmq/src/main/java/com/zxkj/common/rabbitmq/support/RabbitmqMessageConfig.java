package com.zxkj.common.rabbitmq.support;

import com.alibaba.fastjson.JSON;
import com.zxkj.common.cache.constant.CacheKeyPrefix;
import com.zxkj.common.cache.lock.RedisLock;
import com.zxkj.common.cache.redis.RedisUtil;
import com.zxkj.common.rabbitmq.RabbitmqMessageListener;
import com.zxkj.common.rabbitmq.RabbitmqMessageSender;
import com.zxkj.common.rabbitmq.delay.DelayedRabbitMqConfig;
import com.zxkj.common.rabbitmq.support.enums.BusiType;
import com.zxkj.common.rabbitmq.support.enums.BusiTypeHandler;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Rabbitmq消息配置类
 *
 * @author yuhui
 */
@Import(DelayedRabbitMqConfig.class)
public class RabbitmqMessageConfig implements BeanPostProcessor, BeanFactoryAware, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(RabbitmqMessageConfig.class);
    /**
     * 业务消息锁最长有效时间
     */
    private static final int LOCK_EXPIRES = 12000;
    /**
     * 尝试获取业务消息锁超时时间
     */
    private static final int LOCK_TRY_MS = 6000;

    private DefaultListableBeanFactory beanFactory;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public RabbitTemplate busiRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            RabbitmqCorrelationData data = (RabbitmqCorrelationData) correlationData;
            if (ack) {
                String idKey = CacheKeyPrefix.BUSI_MESSAGE_ID + data.getBusiType().toString();
                String bodyKey = CacheKeyPrefix.BUSI_MESSAGE_BODY + data.getBusiType().toString();
                redisUtil.zrem(idKey, data.getId());
                redisUtil.hdel(bodyKey, data.getId());
                logger.info("业务消息发送成功: {} - {}", data.getBusiType(), data.getId());
            } else {
                logger.warn("业务消息发送失败: {} - {}, {}", data.getBusiType(), data.getId(), cause);
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public RabbitmqMessageSender rabbitMessageSender() {
        return new RabbitmqMessageSender();
    }

    @Bean
    public Declarables declarableBusiList() {
        List<Declarable> list = new ArrayList<>();
        for (BusiTypeHandler handler : BusiTypeHandler.values()) {
            BusiType dest = handler.getBusiType();
            FanoutExchange exchange = new FanoutExchange(dest.toString(), true, false);
            list.add(exchange);
            Queue queue = new Queue(handler.toString(), true, false, false);
            list.add(queue);
            Binding binding = BindingBuilder.bind(queue).to(exchange);
            list.add(binding);
        }
        Declarables declarables = new Declarables(list);
        return declarables;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, method -> {
            RabbitmqMessageListener annotation = AnnotationUtils.getAnnotation(method, RabbitmqMessageListener.class);
            if (annotation != null) {
                BusiTypeHandler handler = annotation.value();
                Class<?> busiObjectClass = handler.getBusiType().getBusiObjectClass();
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes == null || parameterTypes.length != 2
                        || !String.class.isAssignableFrom(parameterTypes[0])
                        || !busiObjectClass.isAssignableFrom(parameterTypes[1])) {
                    throw new IllegalArgumentException("业务消息监听方法参数错误: " + targetClass.getCanonicalName() + "#" + method.getName());
                }
                SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
                listenerContainer.setQueueNames(handler.toString());
                listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
                String concurrentConsumers = null;
                if (NumberUtils.isParsable(annotation.concurrentConsumers())) {
                    concurrentConsumers = annotation.concurrentConsumers();
                    listenerContainer.setConcurrentConsumers(Integer.parseInt(concurrentConsumers));
                }
                String maxConcurrentConsumers = null;
                if (NumberUtils.isParsable(annotation.maxConcurrentConsumers())) {
                    maxConcurrentConsumers = annotation.maxConcurrentConsumers();
                    listenerContainer.setMaxConcurrentConsumers(Integer.parseInt(annotation.maxConcurrentConsumers()));
                }
                int preFetchCount = annotation.prefetchCount();
                if (preFetchCount > 0) {
                    listenerContainer.setPrefetchCount(preFetchCount);
                }
                // 这里实现rabbitmq的队列顺序消费
                boolean isSequentialExec = false;
                if ("1".equals(concurrentConsumers) && "1".equals(maxConcurrentConsumers) && 1 == preFetchCount) {
                    isSequentialExec = true;
                }
                if (isSequentialExec) {
                    String tmpKey = CacheKeyPrefix.SEQUENCE_QUEUE_PRE + handler.toString();
                    String localAddress = getLocalAddress();
                    boolean flag = redisUtil.setnx(tmpKey, localAddress);
                    if (!flag) {
                        logger.error("该顺序消费队列已经存在消费者，忽略: {},{}", tmpKey, localAddress);
                        return;
                    }
                }
                listenerContainer.setMessageListener(new BusiChannelAwareMessageListener(bean, method, handler, isSequentialExec));
                beanFactory.registerSingleton(beanName + "#" + method.getName(), listenerContainer);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }

    @Override
    public void destroy() throws Exception {
        List<String> sequenceQueueList = redisUtil.scan(CacheKeyPrefix.SEQUENCE_QUEUE_PRE);
        logger.info("服务销毁，redis顺序消费队列待清除key列表: {}", JSON.toJSONString(sequenceQueueList));
        if (sequenceQueueList == null || sequenceQueueList.size() == 0) {
            return;
        }
        for (String tmp : sequenceQueueList) {
            String sequenceData = redisUtil.get(tmp);
            String localAddress = getLocalAddress();
            if (sequenceData != null && sequenceData.equals(localAddress)) {
                logger.info("顺序消费队列清除key:{}，value:{}", tmp, localAddress);
                redisUtil.del(tmp);
            }
        }
    }

    private class BusiChannelAwareMessageListener implements MessageListener {
        private Object bean;
        private Method method;
        private BusiTypeHandler busiTypeHandler;
        private boolean isSequentialExec;

        public BusiChannelAwareMessageListener(Object bean, Method method, BusiTypeHandler busiTypeHandler, boolean isSequentialExec) {
            this.bean = bean;
            this.method = method;
            this.busiTypeHandler = busiTypeHandler;
            this.isSequentialExec = isSequentialExec;
        }

        @Override
        public void onMessage(Message message) {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            logger.info("-------body: {}", body);
            RabbitmqMessageHelper.BusiTransferObject<?> transferObject;
            try {
                transferObject = RabbitmqMessageHelper.toTransferObject(body, busiTypeHandler.getBusiType().getBusiObjectClass());
            } catch (IOException e) {
                logger.error("业务消息接收异常: " + e.toString(), e);
                throw new RuntimeException("业务消息接收异常: " + e.toString(), e);
            }
            String key = CacheKeyPrefix.EVENT_MESSAGE_HANDLER + busiTypeHandler.toString();
            if (!isSequentialExec) {
                key += ":" + transferObject.getBusiKey();
            }
            RedisLock redisLock = new RedisLock(redisUtil, key, LOCK_EXPIRES);
            if (!redisLock.tryLock(LOCK_TRY_MS, TimeUnit.MILLISECONDS)) {
                logger.warn("业务消息接收超时: {}", key);
                throw new RuntimeException("事件消息接收超时: " + key);
            }
            logger.info("业务消息处理开始: {}", key);
            try {
                method.invoke(bean, transferObject.getBusiKey(), transferObject.getBusiObject());
                logger.info("业务消息处理完成: {}", key);
            } catch (Exception e) {
                logger.error("业务消息处理异常: " + key + ", " + e.toString(), e);
                throw new RuntimeException("业务消息处理异常:" + key + ", " + e.toString(), e);
            } finally {
                redisLock.unlock();
            }
        }
    }

    private String getLocalAddress() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress() + ":" + serverPort;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

}