package com.zxkj.common.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zxkj.common.exception.BusinessException;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RespResult<T> implements Serializable {
    private static final long serialVersionUID = 2456967161175690965L;

    private int code = 1;// 返回码

    private String message;// 返回信息

    private T data; // 数据信息

    private long timestamp; // 时间戳

    private RespResult() {
    }

    private RespResult(RespCodeEnum respCodeEnum) {
        this.code = respCodeEnum.getCode();
        this.message = respCodeEnum.getMessage();
    }

    private RespResult(T data, RespCodeEnum respCodeEnum) {
        this.data = data;
        this.code = respCodeEnum.getCode();
        this.message = respCodeEnum.getMessage();
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public long getTimestamp() {
        if (timestamp == 0) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public String toJsonString() {
        return JsonUtil.toJsonString(this);
    }

    public static <T> RespResult<T> ok() {
        return new RespResult(null, RespCodeEnum.SUCCESS);
    }

    public static <T> RespResult<T> ok(T t) {
        return new RespResult(t, RespCodeEnum.SUCCESS);
    }

    public static <T> RespResult<T> error() {
        return new RespResult(null, RespCodeEnum.FAIL);
    }

    public static <T> RespResult<T> error(String message) {
        RespResult respResult = new RespResult();
        respResult.setCode(RespCodeEnum.FAIL.getCode());
        respResult.setMessage(message);
        return respResult;
    }

    public static <T> RespResult<T> error(int code, String message) {
        RespResult respResult = new RespResult();
        respResult.setCode(code);
        respResult.setMessage(message);
        return respResult;
    }

    @JsonIgnore
    public T getDataWithException() {
        if (code != RespCodeEnum.SUCCESS.getCode()) {
            throw new BusinessException(code, message);
        }
        return data;
    }

}
