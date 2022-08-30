package com.zxkj.common.activemq.support;

import brave.propagation.CurrentTraceContext;
import com.zxkj.common.activemq.ActivemqMessageListener;
import com.zxkj.common.activemq.ActivemqMessageSender;
import com.zxkj.common.activemq.grey.GreyActivemqUtil;
import com.zxkj.common.sleuth.TraceUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Activemq消息配置类
 *
 * @author yuhui
 */
public class ActivemqMessageConfig implements BeanPostProcessor, BeanFactoryAware, SmartInitializingSingleton {
    private static final Logger logger = LoggerFactory.getLogger(ActivemqMessageConfig.class);
    private final ActivemqMessageConfig.MessageHandlerMethodFactoryAdapter messageHandlerMethodFactory = new ActivemqMessageConfig.MessageHandlerMethodFactoryAdapter();
    private final JmsListenerEndpointRegistrar registrar = new JmsListenerEndpointRegistrar();
    private JmsListenerEndpointRegistry endpointRegistry;
    private final AtomicInteger counter = new AtomicInteger();
    private DefaultListableBeanFactory beanFactory;
    private final String connectionFactoryBeanName = "activeMQConnectionFactory";
    private final String containerFactoryBeanName = "jmsListenerContainerFactory";
    private final String endpointIdPrefix = "org.springframework.jms.JmsListenerEndpointContainer#";

    @Value("${spring.activemq.broker-url}")
    private String brokenUrl;
    @Value("${spring.activemq.user}")
    private String userName;
    @Value("${spring.activemq.password}")
    private String password;

    @Bean(name = connectionFactoryBeanName)
    public ActiveMQConnectionFactory factory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName, password, brokenUrl);
        factory.setRedeliveryPolicy(redeliveryPolicy());
        return factory;
    }

    private RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        //是否在每次失败重发是，增长等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        //设置重发最大拖延时间，-1 表示没有拖延，只有setUseExponentialBackOff(true)时生效
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);
        //重发次数
        redeliveryPolicy.setMaximumRedeliveries(10);
        //重发时间间隔
        redeliveryPolicy.setInitialRedeliveryDelay(1);
        //第一次失败后重发前等待500毫秒，第二次500*2，依次递增
        redeliveryPolicy.setBackOffMultiplier(2);
        //是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);
        return redeliveryPolicy;
    }

    @Bean
    public JmsTemplate jmsTemplate(@Qualifier(value = connectionFactoryBeanName) ActiveMQConnectionFactory factory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        //设置持久化，1 非持久， 2 持久化
        jmsTemplate.setDeliveryMode(2);
        jmsTemplate.setConnectionFactory(factory);
        /*
          SESSION_TRANSACTED = 0  事物提交并确认
          AUTO_ACKNOWLEDGE = 1    自动确认
          CLIENT_ACKNOWLEDGE = 2    客户端手动确认   
          DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
          INDIVIDUAL_ACKNOWLEDGE = 4    单条消息确认 activemq 独有
        */
        //消息确认模式
        jmsTemplate.setSessionAcknowledgeMode(4);
        return jmsTemplate;
    }

    @Bean
    public ActivemqMessageSender activemqMessageSender() {
        return new ActivemqMessageSender();
    }

    @Bean(name = containerFactoryBeanName)
    public DefaultJmsListenerContainerFactory listener(@Qualifier(value = connectionFactoryBeanName) ActiveMQConnectionFactory factory) {
        DefaultJmsListenerContainerFactory listener = new DefaultJmsListenerContainerFactory();
        listener.setConnectionFactory(factory);
        listener.setConcurrency("1-10");//设置连接数
        listener.setRecoveryInterval(1000L);//重连间隔时间
        listener.setSessionAcknowledgeMode(4);
        return listener;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        this.registrar.setBeanFactory(beanFactory);
    }

    public void afterSingletonsInstantiated() {
        if (this.beanFactory instanceof ListableBeanFactory) {
            Map<String, JmsListenerConfigurer> beans = ((ListableBeanFactory) this.beanFactory).getBeansOfType(JmsListenerConfigurer.class);
            List<JmsListenerConfigurer> configurers = new ArrayList(beans.values());
            AnnotationAwareOrderComparator.sort(configurers);
            Iterator var3 = configurers.iterator();

            while (var3.hasNext()) {
                JmsListenerConfigurer configurer = (JmsListenerConfigurer) var3.next();
                configurer.configureJmsListeners(this.registrar);
            }
        }
        if (this.registrar.getEndpointRegistry() == null) {
            if (this.endpointRegistry == null) {
                Assert.state(this.beanFactory != null, "BeanFactory must be set to find endpoint registry by bean name");
                this.endpointRegistry = (JmsListenerEndpointRegistry) this.beanFactory.getBean("org.springframework.jms.config.internalJmsListenerEndpointRegistry", JmsListenerEndpointRegistry.class);
            }

            this.registrar.setEndpointRegistry(this.endpointRegistry);
        }

        if (this.containerFactoryBeanName != null) {
            this.registrar.setContainerFactoryBeanName(this.containerFactoryBeanName);
        }

        MessageHandlerMethodFactory handlerMethodFactory = this.registrar.getMessageHandlerMethodFactory();
        if (handlerMethodFactory != null) {
            this.messageHandlerMethodFactory.setMessageHandlerMethodFactory(handlerMethodFactory);
        }

        this.registrar.afterPropertiesSet();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, method -> {
            ActivemqMessageListener annotation = AnnotationUtils.getAnnotation(method, ActivemqMessageListener.class);
            if (annotation != null) {
                String destination = annotation.destination();
                destination += GreyActivemqUtil.generateGreySuffix();
                MethodJmsListenerEndpoint endpoint = new MethodJmsListenerEndpoint();
                endpoint.setBean(bean);
                endpoint.setMethod(method);
                endpoint.setConcurrency(String.valueOf(annotation.concurrency()));
                endpoint.setMessageHandlerMethodFactory(this.messageHandlerMethodFactory);
                endpoint.setBeanFactory(this.beanFactory);
                endpoint.setDestination(destination);
                endpoint.setId(endpointIdPrefix + this.counter.getAndIncrement());
                JmsListenerContainerFactory containerFactory = this.beanFactory.getBean(containerFactoryBeanName, JmsListenerContainerFactory.class);
                MessageListenerContainer messageListenerContainer = containerFactory.createListenerContainer(endpoint);
                messageListenerContainer.setupMessageListener((MessageListener) message -> {
                    CurrentTraceContext.Scope scope = TraceUtil.getTraceContextScope(getTraceMap(message));
                    try {
                        if (null != message && message instanceof TextMessage) {
                            TextMessage textMessage = (TextMessage) message;
                            method.invoke(bean, textMessage.getText());
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    } finally {
                        try {
                            TraceUtil.closeScope(scope);
                            message.acknowledge();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (messageListenerContainer instanceof InitializingBean) {
                    try {
                        ((InitializingBean) messageListenerContainer).afterPropertiesSet();
                    } catch (Exception var5) {
                        throw new BeanInitializationException("Failed to initialize message listener container", var5);
                    }
                }
                messageListenerContainer.start();
                beanFactory.registerSingleton(beanName + "#" + method.getName(), messageListenerContainer);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }

    /**
     * 获取trace数据
     *
     * @param message
     * @return
     */
    private Map<String, Object> getTraceMap(Message message) {
        Map<String, Object> traceMap = new HashMap<>();
        try {
            traceMap.put(TraceUtil.TRACE_ID, message.getLongProperty(TraceUtil.TRACE_ID));
            traceMap.put(TraceUtil.TRACE_ID_HIGH, message.getLongProperty(TraceUtil.TRACE_ID_HIGH));
            traceMap.put(TraceUtil.SPAN_ID, message.getLongProperty(TraceUtil.SPAN_ID));
        } catch (Exception e) {
        }
        return traceMap;
    }

    private class MessageHandlerMethodFactoryAdapter implements MessageHandlerMethodFactory {
        @Nullable
        private MessageHandlerMethodFactory messageHandlerMethodFactory;

        private MessageHandlerMethodFactoryAdapter() {
        }

        public void setMessageHandlerMethodFactory(MessageHandlerMethodFactory messageHandlerMethodFactory) {
            this.messageHandlerMethodFactory = messageHandlerMethodFactory;
        }

        public InvocableHandlerMethod createInvocableHandlerMethod(Object bean, Method method) {
            return this.getMessageHandlerMethodFactory().createInvocableHandlerMethod(bean, method);
        }

        private MessageHandlerMethodFactory getMessageHandlerMethodFactory() {
            if (this.messageHandlerMethodFactory == null) {
                this.messageHandlerMethodFactory = this.createDefaultJmsHandlerMethodFactory();
            }

            return this.messageHandlerMethodFactory;
        }

        private MessageHandlerMethodFactory createDefaultJmsHandlerMethodFactory() {
            DefaultMessageHandlerMethodFactory defaultFactory = new DefaultMessageHandlerMethodFactory();
            defaultFactory.afterPropertiesSet();
            return defaultFactory;
        }
    }

}