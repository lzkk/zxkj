package com.zxkj.common.practice.thread.log;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadPoolTracing {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTracing.class);
    public static ThreadLocal<String> threadLocalTraceId = new TransmittableThreadLocal<>();


    public static void main(String[] args) {
        MyTest2 myTest2 = new MyTest2();
        myTest2.test1();
    }

    static class Task implements Runnable {

        @Override
        public void run() {
            String traceId = threadLocalTraceId.get();
            logger.info("traceId={}", traceId);
        }
    }
}

class MyTest2 {

    private static final Logger loger = LoggerFactory.getLogger(MyTest2.class);
    //线程池大小设置为一，保证是同一个线程run之前获取traceId，run后删除，便于测试
    private static Executor executorService = Executors.newFixedThreadPool(2);

    public void test1() {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        ThreadPoolTracing.threadLocalTraceId.set(traceId);
        loger.info("父线程={}；traceId={}", Thread.currentThread().getName(), traceId);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //ThreadLocal 拿不到值；如果是InheritableThreadLocal，可以拿到值
                String id0 = ThreadPoolTracing.threadLocalTraceId.get();
                loger.info("子线程={},traceId={}", Thread.currentThread().getName(), id0);
            }
        };

//        new Thread(runnable).start();//结果为空
//
//        traceId = UUID.randomUUID().toString().replace("-", "");
//        ThreadPoolTracing.threadLocalTraceId.set(traceId);
//        new Thread(runnable).start();//结果为空
//
//        for (int k = 0; k < 6; k++) {
//            new Thread(runnable).start();//结果为空
//        }


        executorService.execute(TtlRunnable.get(runnable, false, false));//结果为空
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        traceId = UUID.randomUUID().toString().replace("-", "");
        ThreadPoolTracing.threadLocalTraceId.set(traceId);
        loger.info("二次改变,{}", traceId);
        executorService.execute(TtlRunnable.get(runnable, false, false));//结果为空
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        traceId = UUID.randomUUID().toString().replace("-", "");
        ThreadPoolTracing.threadLocalTraceId.set(traceId);
        loger.info("三次改变,{}", traceId);
//        Runnable wrap = wrap(runnable);
        executorService.execute(TtlRunnable.get(runnable, false, false));//结果为空
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int k = 0; k < 6; k++) {
            executorService.execute(TtlRunnable.get(runnable, false, false));//结果为空
        }

        Runnable wrap = wrap(runnable);
        executorService.execute(wrap);//可以获取traceId
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        traceId = UUID.randomUUID().toString().replace("-", "");
//        //traceId 重新复制
//        ThreadPoolTracing.threadLocalTraceId.set(traceId);
//        loger.info("父线程={}；traceId={}", Thread.currentThread().getName(), traceId);
//        //线程池中的traceId跟着变更
//        wrap = wrap(runnable);
//        executorService.execute(wrap);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public Runnable wrap(Runnable task) {
        //获取父线程中的Trace
        String id0 = ThreadPoolTracing.threadLocalTraceId.get();
        class CurrentTraceContextRunnable implements Runnable {
            @Override
            public void run() {
                //traceId 给子线程
                ThreadPoolTracing.threadLocalTraceId.set(id0);
                task.run();
                //子线程用完删除
                ThreadPoolTracing.threadLocalTraceId.remove();
            }
        }
        return new CurrentTraceContextRunnable();

    }


}