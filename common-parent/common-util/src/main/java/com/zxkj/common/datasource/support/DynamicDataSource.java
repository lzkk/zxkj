package com.zxkj.common.datasource.support;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 读写分离数据源
 *
 * @author yuhui
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> currentDataSourceKey = new ThreadLocal<>();
    public static final String READONLY = "readonly";


    @Override
    protected Object determineCurrentLookupKey() {
        return currentDataSourceKey.get();
    }

    /**
     * 设置使用只读数据源
     */
    public static final void setReadonly() {
        currentDataSourceKey.set(READONLY);
    }

    /**
     * 清除数据源指定
     */
    public static final void reset() {
        currentDataSourceKey.remove();
    }
}