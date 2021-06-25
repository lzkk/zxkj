package com.zxkj.common.mongo.utils;

import com.zxkj.common.mongo.enums.SingleTypeEnum;

/**
 * Mongo 单条件操作辅助类
 *
 * @author ：yuhui
 * @date ：Created in 2020/10/12 17:45
 */
public class SingleOprHelp {

    private String key;

    private SingleTypeEnum singleTypeEnum;

    private Object value;

    public SingleOprHelp(String key, SingleTypeEnum singleTypeEnum, Object value) {
        this.key = key;
        this.singleTypeEnum = singleTypeEnum;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SingleTypeEnum getSingleTypeEnum() {
        return singleTypeEnum;
    }

    public void setSingleTypeEnum(SingleTypeEnum singleTypeEnum) {
        this.singleTypeEnum = singleTypeEnum;
    }
}
