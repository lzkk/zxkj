package com.zxkj.common.exception.gateway;

import com.zxkj.common.exception.BusinessException;

public class GatewayException extends BusinessException {

    public GatewayException() {
        super();
    }

    public GatewayException(String message) {
        super(message);
    }

    public GatewayException(Integer errorCode) {
        super(errorCode);
    }

    public GatewayException(Integer errorCode, String message) {
        super(errorCode, message);
    }

}
