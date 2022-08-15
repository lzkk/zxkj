package com.zxkj.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import com.zxkj.common.datasource.extend.DecryptDruidDataSource;
import com.zxkj.common.datasource.extend.DecryptHikariDataSource;
import com.zxkj.common.datasource.support.DataSourceAspect;
import com.zxkj.common.datasource.support.DynamicDataSource;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 读写分离数据源
 *
 * @author yuhui
 */
public class DataSourceConfig implements EnvironmentAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    protected DataSourceAspect dataSourceAspect() {
        return new DataSourceAspect();
    }

    /**
     * 创建动态DruidDataSource
     *
     * @param sign
     * @return
     */
    public DataSource createDynamicDruidDataSource(String sign) {
        String url = getProperty(sign, "url", true);
        if (StringUtils.isBlank(url)) {
            DataSource dataSource = druidDataSource(sign, false);
            return dataSource;
        }
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setDefaultTargetDataSource(druidDataSource(sign, false));
        HashMap<Object, Object> map = new HashMap<>(1);
        map.put(DynamicDataSource.READONLY, druidDataSource(sign, true));
        dataSource.setTargetDataSources(map);
        return dataSource;
    }

    /**
     * 创建动态HikariDataSource
     *
     * @param sign
     * @return
     */
    public DataSource createDynamicHikariDataSource(String sign) {
        String url = getProperty(sign, "url", true);
        if (StringUtils.isBlank(url)) {
            DataSource dataSource = hikariDataSource(sign, false);
            return dataSource;
        }
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setDefaultTargetDataSource(hikariDataSource(sign, false));
        HashMap<Object, Object> map = new HashMap<>(1);
        map.put(DynamicDataSource.READONLY, hikariDataSource(sign, true));
        dataSource.setTargetDataSources(map);
        return dataSource;
    }

    /**
     * druid数据源配置
     *
     * @param sign
     * @param isReadonly
     * @return
     */
    private DataSource druidDataSource(String sign, boolean isReadonly) {
        DruidDataSource dataSource = new DecryptDruidDataSource();
        dataSource.setDriverClassName(getProperty(sign, "driver-class-name", isReadonly));
        dataSource.setUrl(getProperty(sign, "url", isReadonly));
        dataSource.setUsername(getProperty(sign, "username", isReadonly));
        dataSource.setPassword(getProperty(sign, "password", isReadonly));
        dataSource.setInitialSize(NumberUtils.toInt(getProperty(sign, "initialSize", isReadonly)));
        dataSource.setMinIdle(NumberUtils.toInt(getProperty(sign, "minIdle", isReadonly)));
        dataSource.setMaxActive(NumberUtils.toInt(getProperty(sign, "maxActive", isReadonly)));
        dataSource.setMaxWait(NumberUtils.toLong(getProperty(sign, "maxWait", isReadonly)));
        dataSource.setTimeBetweenConnectErrorMillis(NumberUtils.toLong(getProperty(sign, "timeBetweenEvictionRunsMillis", isReadonly)));
        dataSource.setMinEvictableIdleTimeMillis(NumberUtils.toLong(getProperty(sign, "minEvictableIdleTimeMillis", isReadonly)));
        dataSource.setValidationQuery(getProperty(sign, "validationQuery", isReadonly));
        dataSource.setTestWhileIdle(BooleanUtils.toBoolean(getProperty(sign, "testWhileIdle", isReadonly)));
        dataSource.setTestOnBorrow(BooleanUtils.toBoolean(getProperty(sign, "testOnBorrow", isReadonly)));
        dataSource.setTestOnReturn(BooleanUtils.toBoolean(getProperty(sign, "testOnReturn", isReadonly)));
        dataSource.setPoolPreparedStatements(BooleanUtils.toBoolean(getProperty(sign, "poolPreparedStatements", isReadonly)));
        dataSource.setConnectionProperties(getProperty(sign, "connection-properties", isReadonly));
        try {
            dataSource.setFilters(getProperty(sign, "filters", isReadonly));
        } catch (SQLException e) {
            LOGGER.error("[Sign: " + sign + ", readonly: " + isReadonly + "] datasource filter setting error:", e);
        }
        // 用来关闭连接
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(1800);
        dataSource.setLogAbandoned(true);
        return dataSource;
    }

    /**
     * hikari数据源配置
     *
     * @param sign
     * @param isReadonly
     * @return
     */
    private DataSource hikariDataSource(String sign, boolean isReadonly) {
        HikariDataSource dataSource = new DecryptHikariDataSource();
        dataSource.setDriverClassName(getProperty(sign, "driver-class-name", isReadonly));
        dataSource.setJdbcUrl(getProperty(sign, "url", isReadonly));
        dataSource.setUsername(getProperty(sign, "username", isReadonly));
        dataSource.setPassword(getProperty(sign, "password", isReadonly));
        dataSource.setMinimumIdle(NumberUtils.toInt(getProperty(sign, "minIdle", isReadonly)));
        dataSource.setMaximumPoolSize(NumberUtils.toInt(getProperty(sign, "maxActive", isReadonly)));
        dataSource.setMaxLifetime(NumberUtils.toLong(getProperty(sign, "maxLifeTime", isReadonly)));
        dataSource.setConnectionTestQuery(getProperty(sign, "validationQuery", isReadonly));
        return dataSource;
    }

    /**
     * 获取jdbc配置
     *
     * @param sign
     * @param key
     * @param isReadonly
     * @return
     */
    public final String getProperty(String sign, String key, boolean isReadonly) {
        String property = environment.getProperty(sign + (isReadonly ? ".readonly" : "") + ".datasource." + key);
        return StringUtils.isNotBlank(property) ? property : environment.getProperty("common.datasource." + key);
    }
}
