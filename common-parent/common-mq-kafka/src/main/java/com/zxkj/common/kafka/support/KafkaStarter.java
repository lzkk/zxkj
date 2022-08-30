package com.zxkj.common.kafka.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(KafkaMessageConfig.class)
public class KafkaStarter {

}