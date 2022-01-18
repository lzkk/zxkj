package com.zxkj.common.context.support;

import com.zxkj.common.context.GreyContext;
import org.springframework.context.annotation.Bean;

import javax.servlet.*;
import java.io.IOException;

/**
 * 上下文配置类
 */
public class GreyContextConfig {

    @Bean(value = "greyContextFilter")
    public Filter contextInitFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                GreyContext.initContext();
                chain.doFilter(request, response);
                GreyContext.clearContext();
            }

            @Override
            public void destroy() {

            }
        };
    }
}
