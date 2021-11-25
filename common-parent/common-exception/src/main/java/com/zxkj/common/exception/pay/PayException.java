package com.zxkj.common.exception.pay;

import com.zxkj.common.exception.BusinessException;

public class PayException extends BusinessException {

    public PayException() {
        super();
    }

    public PayException(String message) {
        super(message);
    }

    public PayException(Integer errorCode) {
        super(errorCode);
    }

    public PayException(Integer errorCode, String message) {
        super(errorCode, message);
    }

}
