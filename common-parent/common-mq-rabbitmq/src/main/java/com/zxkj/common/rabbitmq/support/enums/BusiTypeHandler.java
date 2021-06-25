package com.zxkj.common.rabbitmq.support.enums;

/**
 * 业务处理对象
 */
public enum BusiTypeHandler {
    /**
     * 订单创建
     */
    ORDER_CREATE_BUSI_TASK(BusiType.ORDER_CREATE_BUSI),
    /**
     * 订单闭环
     */
    ORDER_FINISH_BUSI_TASK(BusiType.ORDER_FINISH_BUSI);

    /**
     * 监听的事件类型
     */
    private BusiType busiType;

    BusiTypeHandler(BusiType busiType) {
        this.busiType = busiType;
    }

    public BusiType getBusiType() {
        return busiType;
    }
}
