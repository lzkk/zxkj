package com.zxkj.common.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zxkj.common.exception.business.BusinessException;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DataResponse<T> implements Serializable {

    private static final long serialVersionUID = 2456967161175690965L;
    private static final int ERROR_CODE = 9999;

    private int returnCode;// 返回码

    private String message;// 返回信息

    private T result; // 数据信息

    private DataResponse() {
    }

    private DataResponse(int returnCode, String message) {
        this.returnCode = returnCode;
        this.message = message;
    }

    private DataResponse(int returnCode, String message, T result) {
        this.returnCode = returnCode;
        this.message = message;
        this.result = result;
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

    public static <T> DataResponse<T> ok(T t) {
        DataResponse<T> response = new DataResponse<T>();
        response.setReturnCode(RespCodeEnum.SUCCESS_200.getRespCode());
        response.setMessage(RespCodeEnum.SUCCESS_200.getRespDesc());
        response.setResult(t);
        return response;
    }

    public static <T> DataResponse<T> no(String message) {
        DataResponse<T> response = new DataResponse<T>();
        response.setReturnCode(RespCodeEnum.ERR_9999.getRespCode());
        response.setMessage(message);
        return response;
    }

    @JsonIgnore
    public T getDataWithException() {
        if (returnCode == RespCodeEnum.ERR_9999.getRespCode()) {
            throw new BusinessException(message);
        }
        return result;
    }

}
