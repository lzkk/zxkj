package com.zxkj.common.activemq.support;

import com.zxkj.common.activemq.ActivemqMessageSender;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 * Activemq消息配置类
 *
 * @author yuhui
 */
public class ActivemqMessageConfig {
    private static final Logger logger = LoggerFactory.getLogger(ActivemqMessageConfig.class);

    @Value("${spring.activemq.broker-url}")
    private String brokenUrl;
    @Value("${spring.activemq.user}")
    private String userName;
    @Value("${spring.activemq.password}")
    private String password;

    public RedeliveryPolicy redeliveryPolicy() {
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
    @ConditionalOnMissingBean
    public ActiveMQConnectionFactory factory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName, password, brokenUrl);
        factory.setRedeliveryPolicy(redeliveryPolicy());
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory factory) {
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
    @ConditionalOnMissingBean
    public ActivemqMessageSender activemqMessageSender() {
        return new ActivemqMessageSender();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultJmsListenerContainerFactory listener(ActiveMQConnectionFactory factory) {
        DefaultJmsListenerContainerFactory listener = new DefaultJmsListenerContainerFactory();
        listener.setConnectionFactory(factory);
        listener.setConcurrency("1-10");//设置连接数
        listener.setRecoveryInterval(1000L);//重连间隔时间
        listener.setSessionAcknowledgeMode(4);
        return listener;
    }

}