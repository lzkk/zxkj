package com.zxkj.common.cache.constant;

/**
 * @author yuhui
 * @version 1.0
 * @Description 缓存Key前缀全局定义
 */
public class CacheKeyPrefix {

    /**
     * 顺序消费缓存前缀
     */
    public static final String SEQUENCE_QUEUE_PRE = "sequence_queue_pre_";
    /**
     * 业务消息ID,SortedSet
     */
    public static final String BUSI_MESSAGE_ID = "BUSI:MESSAGE:ID:";
    /**
     * 业务消息内容,Hash
     */
    public static final String BUSI_MESSAGE_BODY = "BUSI:MESSAGE:BODY:";
    /**
     * 业务消息处理去重
     */
    public static final String EVENT_MESSAGE_HANDLER = "BUSI:MESSAGE:HANDLER:";

}
