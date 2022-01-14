package com.zxkj.common.context;

import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.context.domain.CustomerInfo;
import com.zxkj.common.context.support.WrappedExecutorService;
import com.zxkj.common.trace.TraceUtil;
import com.zxkj.common.util.SpringContext;
import com.zxkj.common.util.greyPublish.GreyPublishUtil;
import com.zxkj.common.web.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 客户数据上下文,用于获取当前客户数据
 */
@Slf4j
public class CustomerContext {
    private static ThreadLocal<CustomerInfo> customerInfoMap = new InheritableThreadLocal<>();

    /**
     * 获取当前信息
     */
    public static CustomerInfo getCurrentCustomer() {
        CustomerInfo customerInfo = customerInfoMap.get();
        if (customerInfo == null) {
            ApplicationArguments applicationArguments = SpringContext.getBeanFix(ApplicationArguments.class);
            if (applicationArguments != null) {
                customerInfo = GreyPublishUtil.getPublishInfo(applicationArguments.getSourceArgs());
                customerInfoMap.set(customerInfo);
            } else {
                customerInfo = new CustomerInfo();
            }
        }
        return customerInfo;
    }

    public static void fillContext(CustomerInfo customerInfo) {
        customerInfoMap.set(customerInfo);
    }

    public static void initContext() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        CustomerInfo customerUser = new CustomerInfo();
        String regionPublishStr = getHeaderString(request, ContextConstant.REGION_PUBLISH_FLAG);
        customerUser.setRegionPublish(regionPublishStr);
        String greyPublishStr = getHeaderString(request, ContextConstant.GREY_PUBLISH_FLAG);
        customerUser.setGreyPublish(greyPublishStr);
        String traceId = getHeaderString(request, ContextConstant.TRACE_ID_FLAG);
        customerUser.setTraceId(traceId);
        customerInfoMap.set(customerUser);
        log.info("currentContext:" + JsonUtil.toJsonString(customerUser));
    }

    public static void clearContext() {
        customerInfoMap.remove();
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
        CustomerInfo customerInfo = customerInfoMap.get();
        final TraceContext traceContext = TraceUtil.getTraceContext();
        class MyCustomCallable implements Callable<C> {

            @Override
            public C call() throws Exception {
                //信息给子线程
                customerInfoMap.set(customerInfo);
                CurrentTraceContext.Scope scope = TraceUtil.getTraceContextScope(traceContext);
                C obj;
                try {
                    obj = task.call();
                } catch (Throwable var11) {
                    throw var11;
                } finally {
                    //子线程用完删除
                    customerInfoMap.remove();
                    TraceUtil.closeScope(scope);
                }
                return obj;
            }
        }
        return new MyCustomCallable();
    }

    private static Runnable wrap(Runnable task) {
        //获取父线程中的Trace
        CustomerInfo customerInfo = customerInfoMap.get();
        final TraceContext traceContext = TraceUtil.getTraceContext();
        class MyCustomRunnable implements Runnable {
            @Override
            public void run() {
                //信息给子线程
                customerInfoMap.set(customerInfo);
                CurrentTraceContext.Scope scope = TraceUtil.getTraceContextScope(traceContext);
                try {
                    task.run();
                } catch (Throwable var11) {
                    throw var11;
                } finally {
                    //子线程用完删除
                    customerInfoMap.remove();
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
                delegate.execute(CustomerContext.wrap(task));
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
                return CustomerContext.wrap(task);
            }

            protected Runnable wrap(Runnable task) {
                return CustomerContext.wrap(task);
            }
        }

        return new MyCustomExecutorService();
    }

}
