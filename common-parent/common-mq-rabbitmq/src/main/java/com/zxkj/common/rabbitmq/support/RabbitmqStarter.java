package com.zxkj.common.rabbitmq.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RabbitmqMessageConfig.class)
public class RabbitmqStarter {

}