package com.zxkj.common.web;

/**
 * 返回状态信息枚举
 */
public enum RespCodeEnum {

    SUCCESS(1, "处理成功!"),
    FAIL(-1, "处理失败!"),
    ERROR(10000, "系统繁忙，请稍后再试!");

    RespCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }}
