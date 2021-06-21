package com.zxkj.common.web;

/**
 * 返回状态信息枚举
 */
public enum RespCodeEnum {

    SUCCESS(0, "处理成功"),
    ERROR(-1, "处理失败"),
    SYSTEM_ERROR(9999, "系统异常");

    private RespCodeEnum(int returnCode, String message) {
        this.returnCode = returnCode;
        this.message = message;
    }

    private int returnCode;
    private String message;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }}
