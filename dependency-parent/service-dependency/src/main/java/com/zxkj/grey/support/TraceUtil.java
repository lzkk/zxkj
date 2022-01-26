package com.zxkj.grey.support;

import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import com.zxkj.common.util.SpringContext;

/**
 * @author ：yuhui
 * @date ：Created in 2022/1/13 16:47
 */
public class TraceUtil {

    public static TraceContext getTraceContext() {
        TraceContext invocationContext = null;
        CurrentTraceContext currentTraceContext = SpringContext.getBeanFix(CurrentTraceContext.class);
        if (currentTraceContext != null) {
            invocationContext = currentTraceContext.get();
        }
        return invocationContext;
    }


    public static CurrentTraceContext.Scope getTraceContextScope(TraceContext traceContext) {
        CurrentTraceContext.Scope scope = null;
        CurrentTraceContext currentTraceContext = SpringContext.getBeanFix(CurrentTraceContext.class);
        if (currentTraceContext != null) {
            scope = currentTraceContext.maybeScope(traceContext);
        }
        return scope;
    }

    public static void closeScope(CurrentTraceContext.Scope scope) {
        if (scope != null) {
            try {
                scope.close();
            } catch (Throwable var4) {
            }
        }
    }
}
