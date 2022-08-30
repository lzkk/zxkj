package com.zxkj.common.sleuth;

import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import com.zxkj.common.spring.SpringContext;

import java.util.Map;

/**
 * @author ：yuhui
 * @date ：Created in 2022/1/13 16:47
 */
public class TraceUtil {

    public static final String TRACE_ID = "traceId";
    public static final String TRACE_ID_HIGH = "traceIdHigh";
    public static final String SPAN_ID = "spanId";

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

    /**
     * 组装链路Scope对象
     *
     * @param dataMap
     * @return
     */
    public static CurrentTraceContext.Scope getTraceContextScope(Map<String, Object> dataMap) {
        CurrentTraceContext.Scope scope = null;
        try {
            if (dataMap != null) {
                long traceId = dataMap.get(TRACE_ID) == null ? 0L : Long.parseLong(String.valueOf(dataMap.get(TRACE_ID)));
                long traceIdHigh = dataMap.get(TRACE_ID_HIGH) == null ? 0L : Long.parseLong(String.valueOf(dataMap.get(TRACE_ID_HIGH)));
                long spanId = dataMap.get(SPAN_ID) == null ? 0L : Long.parseLong(String.valueOf(dataMap.get(SPAN_ID)));
                TraceContext traceContext = TraceContext.newBuilder().traceId(traceId).traceIdHigh(traceIdHigh).spanId(spanId).build();
                scope = TraceUtil.getTraceContextScope(traceContext);
            }
        } catch (Exception e) {
        }
        return scope;
    }

}
