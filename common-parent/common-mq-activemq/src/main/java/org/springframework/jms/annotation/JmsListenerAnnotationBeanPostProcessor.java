//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.jms.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.zxkj.common.activemq.grey.GreyActivemqUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.MethodIntrospector.MetadataLookup;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.MethodJmsListenerEndpoint;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

public class JmsListenerAnnotationBeanPostProcessor implements MergedBeanDefinitionPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {
    static final String DEFAULT_JMS_LISTENER_CONTAINER_FACTORY_BEAN_NAME = "jmsListenerContainerFactory";
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Nullable
    private JmsListenerEndpointRegistry endpointRegistry;
    @Nullable
    private String containerFactoryBeanName = "jmsListenerContainerFactory";
    private final JmsListenerAnnotationBeanPostProcessor.MessageHandlerMethodFactoryAdapter messageHandlerMethodFactory = new JmsListenerAnnotationBeanPostProcessor.MessageHandlerMethodFactoryAdapter();
    @Nullable
    private BeanFactory beanFactory;
    @Nullable
    private StringValueResolver embeddedValueResolver;
    private final JmsListenerEndpointRegistrar registrar = new JmsListenerEndpointRegistrar();
    private final AtomicInteger counter = new AtomicInteger();
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap(64));

    public JmsListenerAnnotationBeanPostProcessor() {
    }

    public int getOrder() {
        return 2147483647;
    }

    public void setEndpointRegistry(JmsListenerEndpointRegistry endpointRegistry) {
        this.endpointRegistry = endpointRegistry;
    }

    public void setContainerFactoryBeanName(String containerFactoryBeanName) {
        this.containerFactoryBeanName = containerFactoryBeanName;
    }

    public void setMessageHandlerMethodFactory(MessageHandlerMethodFactory messageHandlerMethodFactory) {
        this.messageHandlerMethodFactory.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        if (beanFactory instanceof ConfigurableBeanFactory) {
            this.embeddedValueResolver = new EmbeddedValueResolver((ConfigurableBeanFactory)beanFactory);
        }

        this.registrar.setBeanFactory(beanFactory);
    }

    public void afterSingletonsInstantiated() {
        this.nonAnnotatedClasses.clear();
        if (this.beanFactory instanceof ListableBeanFactory) {
            Map<String, JmsListenerConfigurer> beans = ((ListableBeanFactory)this.beanFactory).getBeansOfType(JmsListenerConfigurer.class);
            List<JmsListenerConfigurer> configurers = new ArrayList(beans.values());
            AnnotationAwareOrderComparator.sort(configurers);
            Iterator var3 = configurers.iterator();

            while(var3.hasNext()) {
                JmsListenerConfigurer configurer = (JmsListenerConfigurer)var3.next();
                configurer.configureJmsListeners(this.registrar);
            }
        }

        if (this.registrar.getEndpointRegistry() == null) {
            if (this.endpointRegistry == null) {
                Assert.state(this.beanFactory != null, "BeanFactory must be set to find endpoint registry by bean name");
                this.endpointRegistry = (JmsListenerEndpointRegistry)this.beanFactory.getBean("org.springframework.jms.config.internalJmsListenerEndpointRegistry", JmsListenerEndpointRegistry.class);
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

    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!this.nonAnnotatedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
            Map<Method, Set<JmsListener>> annotatedMethods = MethodIntrospector.selectMethods(targetClass, (MetadataLookup<Set<JmsListener>>) (method) -> {
                Set<JmsListener> listenerMethods = AnnotatedElementUtils.getMergedRepeatableAnnotations(method, JmsListener.class, JmsListeners.class);
                return !listenerMethods.isEmpty() ? listenerMethods : null;
            });
            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add(bean.getClass());
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("No @JmsListener annotations found on bean type: " + bean.getClass());
                }
            } else {
                annotatedMethods.forEach((method, listeners) -> {
                    listeners.forEach((listener) -> {
                        this.processJmsListener(listener, method, bean);
                    });
                });
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug(annotatedMethods.size() + " @JmsListener methods processed on bean '" + beanName + "': " + annotatedMethods);
                }
            }
        }

        return bean;
    }

    protected void processJmsListener(JmsListener jmsListener, Method mostSpecificMethod, Object bean) {
        Method invocableMethod = AopUtils.selectInvocableMethod(mostSpecificMethod, bean.getClass());
        MethodJmsListenerEndpoint endpoint = this.createMethodJmsListenerEndpoint();
        endpoint.setBean(bean);
        endpoint.setMethod(invocableMethod);
        endpoint.setMostSpecificMethod(mostSpecificMethod);
        endpoint.setMessageHandlerMethodFactory(this.messageHandlerMethodFactory);
        endpoint.setEmbeddedValueResolver(this.embeddedValueResolver);
        endpoint.setBeanFactory(this.beanFactory);
        endpoint.setId(this.getEndpointId(jmsListener));
        String destination = jmsListener.destination();
        destination += GreyActivemqUtil.generateGreySuffix();
        endpoint.setDestination(this.resolve(destination));
        if (StringUtils.hasText(jmsListener.selector())) {
            endpoint.setSelector(this.resolve(jmsListener.selector()));
        }

        if (StringUtils.hasText(jmsListener.subscription())) {
            endpoint.setSubscription(this.resolve(jmsListener.subscription()));
        }

        if (StringUtils.hasText(jmsListener.concurrency())) {
            endpoint.setConcurrency(this.resolve(jmsListener.concurrency()));
        }

        JmsListenerContainerFactory<?> factory = null;
        String containerFactoryBeanName = this.resolve(jmsListener.containerFactory());
        if (StringUtils.hasText(containerFactoryBeanName)) {
            Assert.state(this.beanFactory != null, "BeanFactory must be set to obtain container factory by bean name");

            try {
                factory = (JmsListenerContainerFactory)this.beanFactory.getBean(containerFactoryBeanName, JmsListenerContainerFactory.class);
            } catch (NoSuchBeanDefinitionException var9) {
                throw new BeanInitializationException("Could not register JMS listener endpoint on [" + mostSpecificMethod + "], no " + JmsListenerContainerFactory.class.getSimpleName() + " with id '" + containerFactoryBeanName + "' was found in the application context", var9);
            }
        }

        this.registrar.registerEndpoint(endpoint, factory);
    }

    protected MethodJmsListenerEndpoint createMethodJmsListenerEndpoint() {
        return new MethodJmsListenerEndpoint();
    }

    private String getEndpointId(JmsListener jmsListener) {
        if (StringUtils.hasText(jmsListener.id())) {
            String id = this.resolve(jmsListener.id());
            return id != null ? id : "";
        } else {
            return "org.springframework.jms.JmsListenerEndpointContainer#" + this.counter.getAndIncrement();
        }
    }

    @Nullable
    private String resolve(String value) {
        return this.embeddedValueResolver != null ? this.embeddedValueResolver.resolveStringValue(value) : value;
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
            if (JmsListenerAnnotationBeanPostProcessor.this.beanFactory != null) {
                defaultFactory.setBeanFactory(JmsListenerAnnotationBeanPostProcessor.this.beanFactory);
            }

            defaultFactory.afterPropertiesSet();
            return defaultFactory;
        }
    }
}
