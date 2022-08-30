package com.zxkj.common.kafka.support;

import brave.propagation.CurrentTraceContext;
import com.zxkj.common.kafka.KafkaMessageListener;
import com.zxkj.common.kafka.KafkaMessageSender;
import com.zxkj.common.kafka.grey.GreyKafkaUtil;
import com.zxkj.common.kafka.support.enums.KafkaTopicTypeEnum;
import com.zxkj.common.sleuth.TraceUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.util.ReflectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Kafka消息配置类
 *
 * @author yuhui
 */
public class KafkaMessageConfig implements BeanPostProcessor, BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageConfig.class);

    private DefaultListableBeanFactory beanFactory;

    @Autowired
    private Environment environment;

    /**
     * 原生kafka消费方式
     *
     * @return
     */
    @Bean(value = "kafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        String concurrencyStr = environment.getProperty("spring.kafka.listener.concurrency");
        if (concurrencyStr != null && concurrencyStr.trim().length() > 0) {
            factory.setConcurrency(Integer.valueOf(concurrencyStr));
            factory.setBatchListener(true);
        }
        return factory;
    }

    /**
     * 消费消息工厂
     *
     * @return
     */
    public DefaultKafkaConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * 消费消息配置
     *
     * @return
     */
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.bootstrap-servers"));
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, environment.getProperty("spring.kafka.consumer.enable.auto.commit"));
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, environment.getProperty("spring.kafka.consumer.auto.commit.interval"));
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("spring.kafka.consumer.auto.offset.reset"));
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, environment.getProperty("spring.kafka.consumer.session.timeout.ms"));
        propsMap.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, environment.getProperty("spring.kafka.consumer.heartbeat.interval.ms"));
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group.id"));
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, environment.getProperty("spring.kafka.consumer.max.poll.records"));
        propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, environment.getProperty("spring.kafka.consumer.max.poll.interval.ms"));
        propsMap.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RangeAssignor");
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return propsMap;
    }

    /**
     * 生产消息工厂
     *
     * @return
     */
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * 生产消息配置
     *
     * @return
     */
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ProducerConfig.ACKS_CONFIG, environment.getProperty("spring.kafka.producer.acks"));
        props.put(ProducerConfig.RETRIES_CONFIG, environment.getProperty("spring.kafka.producer.retry.count"));
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, environment.getProperty("spring.kafka.producer.retry.backoff.ms"));
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, environment.getProperty("spring.kafka.producer.retry.max.in.flight.requests.per.connection"));
        props.put(ProducerConfig.LINGER_MS_CONFIG, environment.getProperty("spring.kafka.producer.linger.ms"));
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, environment.getProperty("spring.kafka.producer.batch.size"));
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, environment.getProperty("spring.kafka.producer.buffer.memory"));
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, environment.getProperty("spring.kafka.producer.max.request.size"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaProducer kafkaProducer() {
        return new KafkaProducer(producerConfigs());
    }

    @Bean
    public KafkaMessageSender kafkaMessageSender() {
        return new KafkaMessageSender();
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
            KafkaMessageListener annotation = AnnotationUtils.getAnnotation(method, KafkaMessageListener.class);
            if (annotation != null) {
                KafkaTopicTypeEnum handler = annotation.value();
                Class<?> busiObjectClass = handler.getBusiObjectClass();
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes == null || parameterTypes.length != 2
                        || !String.class.isAssignableFrom(parameterTypes[0])
                        || !busiObjectClass.isAssignableFrom(parameterTypes[1])) {
                    throw new IllegalArgumentException("业务消息监听方法参数错误: " + targetClass.getCanonicalName() + "#" + method.getName());
                }
                String topicName = handler.getTopicName() + GreyKafkaUtil.generateGreySuffix();
                ContainerProperties containerProperties = new ContainerProperties(topicName);
                containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL);
                containerProperties.setMessageListener((BatchAcknowledgingMessageListener<String, String>) (records, acknowledgment) -> {
                    try {
                        for (ConsumerRecord<String, String> consumerRecord : records) {
                            CurrentTraceContext.Scope scope = TraceUtil.getTraceContextScope(getTraceMap(consumerRecord));
                            KafkaMessageHelper.BusiTransferObject<?> transferObject = null;
                            try {
                                transferObject = KafkaMessageHelper.toTransferObject(consumerRecord.value(), handler.getBusiObjectClass());
                                method.invoke(bean, transferObject.getBusiKey(), transferObject.getBusiObject());
//                                logger.info("Kafka process over,topic:{},key:{},value:{}", topicName, transferObject.getBusiKey(), transferObject.getBusiObject());
                            } catch (Exception e) {
                                logger.error("Kafka消息处理异常: " + consumerRecord.value(), e);
                                continue;
                            } finally {
                                TraceUtil.closeScope(scope);
                            }
                        }
                    } finally {
                        acknowledgment.acknowledge();
                    }
                });
                Map<String, Object> configurationProperties = consumerConfigs();
                String groupId = String.valueOf(configurationProperties.get(ConsumerConfig.GROUP_ID_CONFIG));
                configurationProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId + GreyKafkaUtil.generateGreySuffix());
                ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(configurationProperties);
                ConcurrentMessageListenerContainer containers = new ConcurrentMessageListenerContainer(consumerFactory, containerProperties);
                String concurrencyStr = environment.getProperty("spring.kafka.listener.concurrency");
                if (concurrencyStr != null && concurrencyStr.trim().length() > 0) {
                    containers.setConcurrency(Integer.valueOf(concurrencyStr));
                }
                containers.start();
                beanFactory.registerSingleton(beanName + "#" + method.getName(), containers);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }

    /**
     * 获取trace数据
     *
     * @param consumerRecord
     * @return
     */
    private Map<String, Object> getTraceMap(ConsumerRecord<String, String> consumerRecord) {
        Map<String, Object> traceMap = new HashMap<>();
        consumerRecord.headers().forEach(header -> {
            traceMap.put(header.key(), new String(header.value(), StandardCharsets.UTF_8));
        });
        return traceMap;
    }

}