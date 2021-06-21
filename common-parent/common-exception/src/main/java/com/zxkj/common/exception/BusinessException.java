package com.zxkj.common.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1158732945315921142L;
    protected Integer errorCode;
    protected String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        this();
        this.message = message;
    }


    public BusinessException(Integer errorCode) {
        this();
        this.errorCode = errorCode;
    }

    public BusinessException(Integer errorCode, String message) {
        this(errorCode);
        this.message = message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
