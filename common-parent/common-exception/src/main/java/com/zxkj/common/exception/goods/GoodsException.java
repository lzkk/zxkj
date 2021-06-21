package com.zxkj.common.exception.goods;

import com.zxkj.common.exception.BusinessException;

public class GoodsException extends BusinessException {

    public GoodsException() {
        super();
    }

    public GoodsException(String message) {
        super(message);
    }

    public GoodsException(Integer errorCode) {
        super(errorCode);
    }

    public GoodsException(Integer errorCode, String message) {
        super(errorCode, message);
    }

}
