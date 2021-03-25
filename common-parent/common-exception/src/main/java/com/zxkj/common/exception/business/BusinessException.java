package com.zxkj.common.exception.business;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1158732945315921142L;
    protected int errorCode;
    protected String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        this();
        this.message = message;
    }


    public BusinessException(int errorCode) {
        this();
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, String message) {
        this(errorCode);
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
