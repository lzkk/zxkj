package com.zxkj.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomUrlBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException ex) throws Exception {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        String msg = null;
        if (ex instanceof FlowException) {
            msg = "限流了";
        } else if (ex instanceof DegradeException) {
            msg = "降级了";
        } else if (ex instanceof ParamFlowException) {
            msg = "热点参数限流";
        } else if (ex instanceof SystemBlockException) {
            msg = "系统规则（负载/...不满足要求）";
        } else if (ex instanceof AuthorityException) {
            msg = "授权规则不通过";
        }
        String message = "{\"returnCode\":9999,\"message\":" + msg + "}";
        //使用类创建就json对象
        httpServletResponse.getWriter().write(message);
        httpServletResponse.getWriter().flush();
    }
}