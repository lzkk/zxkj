package com.zxkj.service.grey;

import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.context.domain.ContextInfo;
import com.zxkj.service.grey.support.WrappedExecutorService;
import com.zxkj.service.grey.support.TraceUtil;
import com.zxkj.common.util.sys.SysConfigUtil;
import com.zxkj.common.web.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 用于获取当前上下文数据
 */
@Slf4j
public class GreyContext {
    private static ThreadLocal<ContextInfo> greyMap = new InheritableThreadLocal<>();

    /**
     * 获取当前信息
     */
    public static ContextInfo getCurrentContext() {
        ContextInfo contextInfo = greyMap.get();
        if (contextInfo == null) {
            contextInfo = new ContextInfo();
            contextInfo.setGreyPublish(SysConfigUtil.getSysConfigValue(ContextConstant.GREY_PUBLISH_FLAG));
            contextInfo.setRegionPublish(SysConfigUtil.getSysConfigValue(ContextConstant.REGION_PUBLISH_FLAG));
            greyMap.set(contextInfo);
        }
        return contextInfo;
    }

    public static void fillContext(ContextInfo contextInfo) {
        greyMap.set(contextInfo);
    }

    public static void initContext() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        ContextInfo contextInfo = new ContextInfo();
        String regionPublishStr = getHeaderString(request, ContextConstant.REGION_PUBLISH_FLAG);
        contextInfo.setRegionPublish(regionPublishStr);
        String greyPublishStr = getHeaderString(request, ContextConstant.GREY_PUBLISH_FLAG);
        contextInfo.setGreyPublish(greyPublishStr);
        String traceId = getHeaderString(request, ContextConstant.TRACE_ID_FLAG);
        contextInfo.setTraceId(traceId);
        greyMap.set(contextInfo);
        log.info("currentContext:" + JsonUtil.toJsonString(contextInfo));
    }

    public static void clearContext() {
        greyMap.remove();
    }

    private static String getHeaderString(HttpServletRequest request, String key) {
        String header = request.getHeader(key);
        if (StringUtils.isBlank(header)) {
            return header;
        }
        try {
            return URLDecoder.decode(header, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static <C> Callable<C> wrap(Callable<C> task) {
        //获取父线程中的Trace
        ContextInfo contextInfo = greyMap.get();
        final TraceContext traceContext = TraceUtil.getTraceContext();
        class MyCustomCallable implements Callable<C> {

            @Override
            public C call() throws Exception {
                //信息给子线程
                greyMap.set(contextInfo);
                CurrentTraceContext.Scope scope = TraceUtil.getTraceContextScope(traceContext);
                C obj;
                try {
                    obj = task.call();
                } catch (Throwable var11) {
                    throw var11;
                } finally {
                    //子线程用完删除
                    greyMap.remove();
                    TraceUtil.closeScope(scope);
                }
                return obj;
            }
        }
        return new MyCustomCallable();
    }

    private static Runnable wrap(Runnable task) {
        //获取父线程中的Trace
        ContextInfo contextInfo = greyMap.get();
        final TraceContext traceContext = TraceUtil.getTraceContext();
        class MyCustomRunnable implements Runnable {
            @Override
            public void run() {
                //信息给子线程
                greyMap.set(contextInfo);
                CurrentTraceContext.Scope scope = TraceUtil.getTraceContextScope(traceContext);
                try {
                    task.run();
                } catch (Throwable var11) {
                    throw var11;
                } finally {
                    //子线程用完删除
                    greyMap.remove();
                    TraceUtil.closeScope(scope);
                }
            }
        }
        return new MyCustomRunnable();
    }


    public static Executor executor(final Executor delegate) {
        class MyCustomExecutor implements Executor {
            MyCustomExecutor() {
            }

            public void execute(Runnable task) {
                delegate.execute(GreyContext.wrap(task));
            }
        }

        return new MyCustomExecutor();
    }

    public static ExecutorService executorService(final ExecutorService delegate) {
        class MyCustomExecutorService extends WrappedExecutorService {
            MyCustomExecutorService() {
            }

            protected ExecutorService delegate() {
                return delegate;
            }

            protected <C> Callable<C> wrap(Callable<C> task) {
                return GreyContext.wrap(task);
            }

            protected Runnable wrap(Runnable task) {
                return GreyContext.wrap(task);
            }
        }

        return new MyCustomExecutorService();
    }

}
