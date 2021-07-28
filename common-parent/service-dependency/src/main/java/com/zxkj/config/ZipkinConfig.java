package com.zxkj.config;

import com.zxkj.common.kafka.support.KafkaMessageConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Zipkin依赖Kafka启用配置
 *
 * @author ：yuhui
 * @date ：Created in 2020/9/24 17:43
 */
@Configuration
@Import(KafkaMessageConfig.class)
public class ZipkinConfig {


}
