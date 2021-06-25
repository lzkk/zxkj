package com.zxkj.common.mongo.enums;

/**
 * 操作类型枚举
 */
public enum SingleTypeEnum {

    OPERATE_IS("is", "="),
    OPERATE_IS_NOT("is not", "!="),
    OPERATE_LIKE("like", "like"),
    OPERATE_IN("in", "in"),
    OPERATE_NOT_IN("not in", "not in"),
    OPERATE_GT("gt", ">"),
    OPERATE_GTE("gte", ">="),
    OPERATE_LT("lt", "<"),
    OPERATE_LTE("lte", "<=");

    private String code;
    private String name;

    private SingleTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SingleTypeEnum getOprTypeEnum(String code) {
        if (null == code) {
            return null;
        }
        for (SingleTypeEnum roleEnum : values()) {
            if (roleEnum.getCode().equals(code)) {
                return roleEnum;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
