package com.zxkj.service.config;

import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.web.RespCodeEnum;
import com.zxkj.common.web.RespResult;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 微服务通用配置类
 *
 * @author yuhui
 */
@Slf4j
@Configuration
public class MicroServiceConfig {

    /**
     * springbootAdmin 信息上报
     *
     * @param meterRegistryPostProcessor
     * @param registry
     * @return
     */
    @Bean
    InitializingBean forcePostProcessor(BeanPostProcessor meterRegistryPostProcessor, MeterRegistry registry) {
        return () -> meterRegistryPostProcessor.postProcessAfterInitialization(registry, "");
    }

    /**
     * 全局异常处理
     */
    @Slf4j
    @RestControllerAdvice
    protected static class UnifiedExceptionHandler {

        @Autowired
        private MessageSource messageSource;

        @ExceptionHandler({
                NoHandlerFoundException.class,
                HttpRequestMethodNotSupportedException.class,
                HttpMediaTypeNotSupportedException.class,
                MissingPathVariableException.class,
                MissingServletRequestParameterException.class,
                TypeMismatchException.class,
                HttpMessageNotReadableException.class,
                HttpMessageNotWritableException.class,
                HttpMediaTypeNotAcceptableException.class,
                ServletRequestBindingException.class,
                ConversionNotSupportedException.class,
                MissingServletRequestPartException.class,
                AsyncRequestTimeoutException.class
        })
        public RespResult handleServletException(Exception e) {
            log.error("springMvc前置异常", e);
            return RespResult.error(e.getMessage());
        }

        @ExceptionHandler(value = BindException.class)
        public RespResult handleBindException(BindException e) {
            log.error("参数绑定校验异常", e);
            return wrapperBindingResult(e.getBindingResult());
        }

        @ExceptionHandler(value = MethodArgumentNotValidException.class)
        public RespResult handleValidException(MethodArgumentNotValidException e) {
            log.error("参数校验异常", e);
            return wrapperBindingResult(e.getBindingResult());
        }

        private RespResult wrapperBindingResult(BindingResult bindingResult) {
            StringBuilder msg = new StringBuilder();
            for (ObjectError error : bindingResult.getAllErrors()) {
                msg.append(", ");
                if (error instanceof FieldError) {
                    msg.append(((FieldError) error).getField()).append(": ");
                }
                msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
            }
            return RespResult.error(msg.substring(2));
        }

        ////////////////////////下面开始是业务处理异常////////////////////////////////////////

        /**
         * 自定义异常(业务定义的异常)
         *
         * @param e
         * @return
         */
        @ExceptionHandler(value = BusinessException.class)
        public RespResult handleBusinessException(BusinessException e) {
            String message;
            if (e.getMessage() == null && e.getErrorCode() != null) {
                message = messageSource.getMessage(String.valueOf(e.getErrorCode()), null, null);
                if (message == null) {
                    message = RespCodeEnum.FAIL.getMessage();
                }
            } else {
                message = e.getMessage() == null ? RespCodeEnum.FAIL.getMessage() : e.getMessage();
            }
            log.error("handleBusinessException:{}", message, e);
            return RespResult.error(message);
        }

        /**
         * 未定义异常(数据库异常、数组越界异常等等)
         *
         * @param e
         * @return
         */
        @ExceptionHandler(value = Exception.class)
        public RespResult handleException(Exception e) {
            log.error("发生自定义异常", e);
            return RespResult.error(RespCodeEnum.ERROR.getMessage());
        }

    }

}
