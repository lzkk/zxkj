package com.zxkj.common.mongo.enums;

/**
 * 操作类型枚举
 */
public enum MultiTypeEnum {

    OPERATE_AND("and", "and"),
    OPERATE_OR("or", "or");

    private String code;
    private String name;

    private MultiTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MultiTypeEnum getOprTypeEnum(String code) {
        if (null == code) {
            return null;
        }
        for (MultiTypeEnum roleEnum : values()) {
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
