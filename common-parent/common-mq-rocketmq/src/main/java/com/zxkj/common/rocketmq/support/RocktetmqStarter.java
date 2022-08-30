package com.zxkj.common.rocketmq.support;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RocketmqMessageConfig.class)
public class RocktetmqStarter {

}