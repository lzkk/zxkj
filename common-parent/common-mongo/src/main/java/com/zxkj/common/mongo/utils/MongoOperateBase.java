package com.zxkj.common.mongo.utils;


import com.zxkj.common.mongo.enums.MultiTypeEnum;
import com.zxkj.common.mongo.enums.SingleTypeEnum;

import java.io.Serializable;

/**
 * @Description 工具类
 * @date 2020/7/24 16:09
 */
public class MongoOperateBase implements Serializable {
    private static final long serialVersionUID = 1L;
    private MultiOprHelp multiOprHelp = new MultiOprHelp();

    public static MongoOperateBase begin() {
        return new MongoOperateBase();
    }

    public MultiOprHelp end() {
        if (multiOprHelp.getMultiTypeEnum() == null) {
            multiOprHelp.setMultiTypeEnum(MultiTypeEnum.OPERATE_AND);
        }
        if (multiOprHelp.getList() == null || multiOprHelp.getList().size() == 0) {
            throw new RuntimeException("未装载操作数据，请先装载！");
        }
        return multiOprHelp;
    }

    public MongoOperateBase and() {
        if (multiOprHelp.getMultiTypeEnum() != null) {
            throw new RuntimeException("操作类型只能初始化一次，不能重复操作！");
        }
        multiOprHelp.setMultiTypeEnum(MultiTypeEnum.OPERATE_AND);
        return this;
    }

    public MongoOperateBase or() {
        if (multiOprHelp.getMultiTypeEnum() != null) {
            throw new RuntimeException("操作类型只能初始化一次，不能重复操作！");
        }
        multiOprHelp.setMultiTypeEnum(MultiTypeEnum.OPERATE_OR);
        return this;
    }

    public MongoOperateBase is(String key, Object value) {
        multiOprHelp.getList().add(new SingleOprHelp(key, SingleTypeEnum.OPERATE_IS, value));
        return this;
    }

    public MongoOperateBase isNot(String key, Object value) {
        multiOprHelp.getList().add(new SingleOprHelp(key, SingleTypeEnum.OPERATE_IS_NOT, value));
        return this;
    }

    public MongoOperateBase like(String key, Object value) {
        multiOprHelp.getList().add(new SingleOprHelp(key, SingleTypeEnum.OPERATE_LIKE, value));
        return this;
    }

    public MongoOperateBase in(String key, Object value) {
        multiOprHelp.getList().add(new SingleOprHelp(key, SingleTypeEnum.OPERATE_IN, value));
        return this;
    }

    public MongoOperateBase notIn(String key, Object value) {
        multiOprHelp.getList().add(new SingleOprHelp(key, SingleTypeEnum.OPERATE_NOT_IN, value));
        return this;
    }

    public MongoOperateBase gt(String key, Object value) {
        multiOprHelp.getList().add(new SingleOprHelp(key, SingleTypeEnum.OPERATE_GT, value));
        return this;
    }

    public MongoOperateBase gte(String key, Object value) {
        multiOprHelp.getList().add(new SingleOprHelp(key, SingleTypeEnum.OPERATE_GTE, value));
        return this;
    }

    public MongoOperateBase lt(String key, Object value) {
        multiOprHelp.getList().add(new SingleOprHelp(key, SingleTypeEnum.OPERATE_LT, value));
        return this;
    }

    public MongoOperateBase lte(String key, Object value) {
        multiOprHelp.getList().add(new SingleOprHelp(key, SingleTypeEnum.OPERATE_LTE, value));
        return this;
    }

}