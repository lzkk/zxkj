package com.zxkj.common.rabbitmq.support;

import com.zxkj.common.cache.constant.CacheKeyPrefix;
import com.zxkj.common.cache.lock.RedisLock;
import com.zxkj.common.cache.redis.Cache;
import com.zxkj.common.rabbitmq.RabbitmqMessageListener;
import com.zxkj.common.rabbitmq.RabbitmqMessageSender;
import com.zxkj.common.rabbitmq.delay.DelayedRabbitMqConfig;
import com.zxkj.common.rabbitmq.grey.GreyRabbitUtil;
import com.zxkj.common.rabbitmq.support.enums.BusiType;
import com.zxkj.common.rabbitmq.support.enums.BusiTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Rabbitmq消息配置类
 *
 * @author yuhui
 */
@Import(DelayedRabbitMqConfig.class)
public class RabbitmqMessageConfig implements BeanPostProcessor, BeanFactoryAware, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(RabbitmqMessageConfig.class);

    // 业务消息锁最长有效时间
    private static final int LOCK_EXPIRES = 12000;
    // 尝试获取业务消息锁超时时间
    private static final int LOCK_TRY_MS = 6000;
    private Map<String, String> sequenceKeyMap = new ConcurrentHashMap<>();
    private DefaultListableBeanFactory beanFactory;
    private static final String MANUAL_EXCHANGE = "manualExchange";
    private static final String MANUAL_QUEUE = "manualQueue";
    private static final String MANUAL_ROUTING_KEY = "manualRoutingKey";

    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory;
    @Autowired
    private Cache cache;
    @Value("${server.port}")
    private String serverPort;


    @Bean(name = MANUAL_EXCHANGE)
    public DirectExchange ManualExchange() {
        return new DirectExchange(MANUAL_EXCHANGE);
    }

    @Bean(name = MANUAL_QUEUE)
    public Queue ManualQueue() {
        return new Queue(MANUAL_QUEUE);
    }

    @Bean
    public Binding ManualBinding(@Qualifier(MANUAL_QUEUE) Queue queue, @Qualifier(MANUAL_EXCHANGE) DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(MANUAL_ROUTING_KEY);
    }

    @Bean
    public MessageRecoverer messageRecoverer(@Qualifier("rabbitTemplate") RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, MANUAL_EXCHANGE, MANUAL_ROUTING_KEY);
    }

    @Bean(name = "rabbitTemplate")
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            RabbitmqCorrelationData data = (RabbitmqCorrelationData) correlationData;
            if (ack) {
                String idKey = CacheKeyPrefix.BUSI_MESSAGE_ID + data.getBusiType().toString();
                String bodyKey = CacheKeyPrefix.BUSI_MESSAGE_BODY + data.getBusiType().toString();
                cache.zrem(idKey, data.getId());
                cache.hdel(bodyKey, data.getId());
                logger.info("业务消息发送成功: {} - {}", data.getBusiType(), data.getId());
            } else {
                logger.warn("业务消息发送失败: {} - {}, {}", data.getBusiType(), data.getId(), cause);
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public List<Declarable> declarable() {
        List<Declarable> list = new ArrayList<>();
        String greySuffix = GreyRabbitUtil.generateGreySuffix();
        for (BusiTypeHandler handler : BusiTypeHandler.values()) {
            BusiType dest = handler.getBusiType();
            FanoutExchange exchange = new FanoutExchange(dest.toString() + greySuffix, true, false);
            list.add(exchange);
            Queue queue = new Queue(handler.toString() + greySuffix, true, false, false);
            list.add(queue);
            Binding binding = BindingBuilder.bind(queue).to(exchange);
            list.add(binding);
        }
        return list;
    }

    @Bean
    public RabbitmqMessageSender rabbitMessageSender() {
        return new RabbitmqMessageSender();
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
                String queueName = handler.toString() + GreyRabbitUtil.generateGreySuffix();
                int concurrentConsumers = annotation.concurrentConsumers();
                int maxConcurrentConsumers = annotation.maxConcurrentConsumers();
                int preFetchCount = annotation.prefetchCount();
                // 这里实现rabbitmq的队列顺序消费
                boolean isSequentialExec = false;
                if (1 == concurrentConsumers && 1 == maxConcurrentConsumers && 1 == preFetchCount) {
                    isSequentialExec = true;
                    String tmpKey = CacheKeyPrefix.SEQUENCE_QUEUE_PRE + queueName;
                    String localAddress = getLocalAddress();
                    boolean flag = cache.setnx(tmpKey, localAddress);
                    if (!flag) {
                        logger.error("该顺序消费队列已经存在消费者，忽略: {},{}", tmpKey, localAddress);
                        return;
                    } else {
                        sequenceKeyMap.put(tmpKey, localAddress);
                    }
                }
                SimpleRabbitListenerEndpoint simpleRabbitListenerEndpoint = new SimpleRabbitListenerEndpoint();
                simpleRabbitListenerEndpoint.setMessageListener(new AwareMessageListener(bean, method, handler, queueName, isSequentialExec));
                SimpleMessageListenerContainer listenerContainer = simpleRabbitListenerContainerFactory.createListenerContainer(simpleRabbitListenerEndpoint);
                listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
                listenerContainer.setQueueNames(queueName);
                if (concurrentConsumers > 0) {
                    listenerContainer.setConcurrentConsumers(concurrentConsumers);
                }
                if (maxConcurrentConsumers > 0) {
                    listenerContainer.setMaxConcurrentConsumers(maxConcurrentConsumers);
                }
                if (preFetchCount > 0) {
                    listenerContainer.setPrefetchCount(preFetchCount);
                }
                beanFactory.registerSingleton(beanName + "#" + method.getName(), listenerContainer);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }

    @Override
    public void destroy() throws Exception {
        Iterator<String> it = sequenceKeyMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String sequenceData = cache.get(key);
            String localAddress = getLocalAddress();
            if (sequenceData != null && sequenceData.equals(localAddress)) {
                logger.info("服务销毁，redis顺序消费队列待清除key: {},value:{}", key, localAddress);
                cache.del(key);
            }
        }
        logger.info("服务销毁清除key完毕！");
    }

    private class AwareMessageListener implements MessageListener {
        private Object bean;
        private Method method;
        private BusiTypeHandler busiTypeHandler;
        private String queueName;
        private boolean isSequentialExec;

        public AwareMessageListener(Object bean, Method method, BusiTypeHandler busiTypeHandler, String queueName, boolean isSequentialExec) {
            this.bean = bean;
            this.method = method;
            this.busiTypeHandler = busiTypeHandler;
            this.queueName = queueName;
            this.isSequentialExec = isSequentialExec;
        }

        @Override
        public void onMessage(Message message) {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            RabbitmqMessageHelper.BusiTransferObject<?> transferObject;
            try {
                transferObject = RabbitmqMessageHelper.toTransferObject(body, busiTypeHandler.getBusiType().getBusiObjectClass());
            } catch (IOException e) {
                logger.error("业务消息接收异常: " + e.toString(), e);
                return;
            }
            String key = CacheKeyPrefix.EVENT_MESSAGE_HANDLER + queueName;
            if (!isSequentialExec) {
                key += ":" + transferObject.getBusiKey();
            }
            RedisLock redisLock = new RedisLock(cache, key, LOCK_EXPIRES);
            if (!redisLock.tryLock(LOCK_TRY_MS, TimeUnit.MILLISECONDS)) {
                logger.warn("业务消息接收超时: {}", key);
                throw new RuntimeException("事件消息接收超时: " + key);
            }
            try {
                method.invoke(bean, transferObject.getBusiKey(), transferObject.getBusiObject());
//                logger.info("Rabbitmq业务消息处理完成: {}", body);
            } catch (Exception e) {
                logger.error("Rabbitmq消息处理异常: " + body, e);
                throw new RuntimeException("Rabbitmq消息处理异常: " + body, e);
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