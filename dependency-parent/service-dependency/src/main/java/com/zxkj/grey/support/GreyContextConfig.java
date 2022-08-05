package com.zxkj.grey.support;

import com.zxkj.grey.GreyContext;
import org.springframework.context.annotation.Bean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 上下文配置类
 */
public class GreyContextConfig {

    @Bean(value = "greyContextFilter")
    public Filter contextInitFilter() {
        return new Filter() {

            private Set<String> IGNORE_PATHS = Collections.unmodifiableSet(new HashSet<>(
                    Arrays.asList("/xxl-job/run")));

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                String requestURI = httpServletRequest.getRequestURI();
                boolean ignorePath = IGNORE_PATHS.contains(requestURI);
                if (!ignorePath) {
                    GreyContext.initContext();
                }
                chain.doFilter(request, response);
                if (!ignorePath) {
                    GreyContext.clearContext();
                }
            }

            @Override
            public void destroy() {

            }
        };
    }
}
