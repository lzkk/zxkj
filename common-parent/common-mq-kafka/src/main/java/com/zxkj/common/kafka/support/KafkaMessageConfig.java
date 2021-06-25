package com.zxkj.common.kafka.support;

import com.zxkj.common.kafka.KafkaMessageListener;
import com.zxkj.common.kafka.KafkaMessageSender;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
     * 消费消息工厂
     *
     * @return
     */
    public ConsumerFactory<String, String> consumerFactory() {
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
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, environment.getProperty("spring.kafka.consumer.enable-auto-commit"));
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, environment.getProperty("spring.kafka.consumer.auto.commit.interval"));
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, environment.getProperty("spring.kafka.consumer.session.timeout"));
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group-id"));
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, environment.getProperty("spring.kafka.consumer.max.pollrecordes"));// 每一批数量
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("spring.kafka.consumer.auto.offset.reset"));
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
        props.put(ProducerConfig.RETRIES_CONFIG, environment.getProperty("spring.kafka.producer.retries"));
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, environment.getProperty("spring.kafka.producer.batch.size"));
        props.put(ProducerConfig.LINGER_MS_CONFIG, environment.getProperty("spring.kafka.producer.linger"));
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, environment.getProperty("spring.kafka.producer.buffer.memory"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate kafkaTemplate() {
        return new KafkaTemplate<String, String>(producerFactory());
    }

    @Bean
    public KafkaProducer kafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaProducer<>(props);
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
                ContainerProperties containerProperties = new ContainerProperties(handler.getTopicName());
                containerProperties.setMessageListener((MessageListener<String, String>) record -> {
                    try {
                        KafkaMessageHelper.BusiTransferObject<?> transferObject;
                        try {
                            transferObject = KafkaMessageHelper.toTransferObject(record.value(), handler.getBusiObjectClass());
                        } catch (IOException e) {
                            logger.error("业务消息接收异常: " + e.toString(), e);
                            throw new RuntimeException("业务消息接收异常: " + e.toString(), e);
                        }
                        method.invoke(bean, transferObject.getBusiKey(), transferObject.getBusiObject());
                    } catch (Exception e) {
                        logger.error("业务消息处理异常: " + record.value() + ", " + e.toString(), e);
                    }
                });
                ConcurrentMessageListenerContainer containers = new ConcurrentMessageListenerContainer(consumerFactory(), containerProperties);
                containers.start();
                beanFactory.registerSingleton(beanName + "#" + method.getName(), containers);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }

}