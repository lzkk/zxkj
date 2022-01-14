package com.zxkj.common.context.support;

import com.zxkj.common.context.CustomerContext;
import org.springframework.context.annotation.Bean;

import javax.servlet.*;
import java.io.IOException;

/**
 * 上下文配置类
 */
public class CustomerContextConfig {

    @Bean(value = "customerContextFilter")
    public Filter contextInitFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                CustomerContext.initContext();
                chain.doFilter(request, response);
                CustomerContext.clearContext();
            }

            @Override
            public void destroy() {

            }
        };
    }
}
