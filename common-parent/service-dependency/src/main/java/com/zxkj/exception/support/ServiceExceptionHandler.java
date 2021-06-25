package com.zxkj.exception.support;

import com.netflix.client.ClientException;
import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.web.RespCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ServiceExceptionHandler implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Integer code;
        String message;
        // 根据不同错误转向不同页面
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            code = businessException.getErrorCode();
            if (null == code) {
                code = RespCodeEnum.ERROR.getReturnCode();
            }
            message = businessException.getMessage();
        } else {
            code = RespCodeEnum.SYSTEM_ERROR.getReturnCode();
            message = ex.getMessage();
        }
        log.error(ex.getMessage(), ex);
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.addStaticAttribute("returnCode", code);
        view.addStaticAttribute("message", message);
        view.addStaticAttribute("result", null);
        return new ModelAndView(view);
    }
}  