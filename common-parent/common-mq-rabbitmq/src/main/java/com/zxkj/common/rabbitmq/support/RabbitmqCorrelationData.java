package com.zxkj.common.rabbitmq.support;

import com.zxkj.common.rabbitmq.support.enums.BusiType;
import org.springframework.amqp.rabbit.support.CorrelationData;

public class RabbitmqCorrelationData extends CorrelationData {
    private BusiType busiType;

    public RabbitmqCorrelationData(String id, BusiType busiType) {
        super(id);
        this.busiType = busiType;
    }

    public BusiType getBusiType() {
        return busiType;
    }
}
