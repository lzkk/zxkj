package com.zxkj.common.exception.order;

import com.zxkj.common.exception.BusinessException;

public class OrderException extends BusinessException {

    public OrderException() {
        super();
    }

    public OrderException(String message) {
        super(message);
    }

    public OrderException(Integer errorCode) {
        super(errorCode);
    }

    public OrderException(Integer errorCode, String message) {
        super(errorCode, message);
    }

}
