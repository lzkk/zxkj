package com.zxkj.common.activemq.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ActivemqMessageConfig.class)
public class ActivemqStarter {

}