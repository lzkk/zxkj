package com.zxkj.common.datasource.support;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 读写分离aop
 *
 * @author yuhui
 */
@Aspect
public class DataSourceAspect {

    @Pointcut("@annotation(com.zxkj.common.datasource.support.Readonly)")
    private void dbSwitcher() {
    }

    @Around(value = "dbSwitcher()")
    public Object setDbToReadonly(ProceedingJoinPoint joinPoint) throws Throwable {
        DynamicDataSource.setReadonly();
        try {
            return joinPoint.proceed();
        } finally {
            DynamicDataSource.reset();
        }
    }
}
