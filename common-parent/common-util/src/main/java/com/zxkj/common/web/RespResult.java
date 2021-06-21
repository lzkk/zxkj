package com.zxkj.common.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zxkj.common.exception.BusinessException;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RespResult<T> implements Serializable {

    private static final long serialVersionUID = 2456967161175690965L;
    private static final int SUCCESS_CODE = 0;

    private int returnCode;// 返回码

    private String message;// 返回信息

    private T result; // 数据信息

    private RespResult() {
    }

    private RespResult(RespCodeEnum respCodeEnum) {
        this.returnCode = respCodeEnum.getReturnCode();
        this.message = respCodeEnum.getMessage();
    }

    private RespResult(T result, RespCodeEnum respCodeEnum) {
        this.result = result;
        this.returnCode = respCodeEnum.getReturnCode();
        this.message = respCodeEnum.getMessage();
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getMessage() {
        return message;
    }

    public T getResult() {
        return result;
    }

    public String toJsonString() {
        return JsonUtil.jsonFromObject(this);
    }

    public static <T> RespResult<T> ok() {
        return new RespResult(null, RespCodeEnum.SUCCESS);
    }

    public static <T> RespResult<T> ok(T t) {
        return new RespResult(t, RespCodeEnum.SUCCESS);
    }

    public static <T> RespResult<T> error() {
        return new RespResult(null, RespCodeEnum.ERROR);
    }

    public static <T> RespResult<T> error(String message) {
        RespResult respResult = new RespResult();
        respResult.setReturnCode(RespCodeEnum.ERROR.getReturnCode());
        respResult.setMessage(message);
        return respResult;
    }

    public static <T> RespResult<T> error(RespCodeEnum respCodeEnum) {
        return new RespResult(respCodeEnum);
    }

    @JsonIgnore
    public T getDataWithException() {
        if (returnCode != RespCodeEnum.SUCCESS.getReturnCode()) {
            throw new BusinessException(message);
        }
        return result;
    }

}
