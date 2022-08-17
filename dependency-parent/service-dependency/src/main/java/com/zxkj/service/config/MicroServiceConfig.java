package com.zxkj.service.config;

import com.zxkj.common.exception.BusinessException;
import com.zxkj.common.web.RespCodeEnum;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微服务通用配置类
 *
 * @author yuhui
 */
@Slf4j
@Configuration
public class MicroServiceConfig {

    @Bean
    InitializingBean forcePostProcessor(BeanPostProcessor meterRegistryPostProcessor, MeterRegistry registry) {
        return () -> meterRegistryPostProcessor.postProcessAfterInitialization(registry, "");
    }

    /**
     * 异常统一处理类
     *
     * @return
     */
    @Bean
    public HandlerExceptionResolver serviceExceptionHandler(@Autowired MessageSource messageSource) {
        class MyCustomHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {

            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception ex) {
                Integer code;
                String message;
                // 根据不同错误转向不同页面
                if (ex instanceof BusinessException) {
                    BusinessException businessException = (BusinessException) ex;
                    if (businessException.getMessage() == null && businessException.getErrorCode() != null) {
                        code = businessException.getErrorCode();
                        message = messageSource.getMessage(String.valueOf(businessException.getErrorCode()), null, RequestContextUtils.getLocale(request));
                        if (message == null) {
                            message = RespCodeEnum.FAIL.getMessage();
                        }
                    } else {
                        code = businessException.getErrorCode() == null ? RespCodeEnum.FAIL.getCode() : businessException.getErrorCode();
                        message = businessException.getMessage() == null ? RespCodeEnum.FAIL.getMessage() : businessException.getMessage();
                    }
                } else {
                    code = RespCodeEnum.ERROR.getCode();
                    message = RespCodeEnum.ERROR.getMessage();
                }
                log.error(message, ex);
                MappingJackson2JsonView view = new MappingJackson2JsonView();
                view.addStaticAttribute("code", code);
                view.addStaticAttribute("message", message);
                view.addStaticAttribute("timestamp", System.currentTimeMillis());
                return new ModelAndView(view);
            }

            @Override
            public int getOrder() {
                return 1;
            }
        }
        return new MyCustomHandlerExceptionResolver();
    }

}
