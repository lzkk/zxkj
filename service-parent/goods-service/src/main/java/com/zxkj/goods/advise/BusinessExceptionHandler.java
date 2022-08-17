package com.zxkj.goods.advise;

import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.web.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class BusinessExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public RespResult<Object> handler(HttpServletRequest request, Exception ex) throws Exception {
        String message;
        // 根据不同错误转向不同页面
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            message = messageSource.getMessage(String.valueOf(businessException.getErrorCode()), null, request.getLocale());
        } else {
            message = ex.getMessage();
        }
        log.error(message, ex);
        return RespResult.error(message);
    }

}
