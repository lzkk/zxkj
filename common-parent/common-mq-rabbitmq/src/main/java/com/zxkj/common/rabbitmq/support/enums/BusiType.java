package com.zxkj.common.rabbitmq.support.enums;

import java.util.Objects;

/**
 * 业务类型
 */
public enum BusiType {
    /**
     * 订单创建
     */
    ORDER_CREATE_BUSI(String.class),
    /**
     * 订单闭环
     */
    ORDER_FINISH_BUSI(String.class);

    private Class<?> busiObjectClass;

    BusiType(Class<?> busiObjectClass) {
        Objects.requireNonNull(busiObjectClass);
        this.busiObjectClass = busiObjectClass;
    }

    public Class<?> getBusiObjectClass() {
        return busiObjectClass;
    }
}
