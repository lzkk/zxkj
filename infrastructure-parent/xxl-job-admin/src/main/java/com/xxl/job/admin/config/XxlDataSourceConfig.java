package com.xxl.job.admin.config;

import com.zxkj.common.datasource.DataSourceConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = XxlDataSourceConfig.PACKAGE, sqlSessionFactoryRef = XxlDataSourceConfig.SQL_SESSION_FACTORY)
public class XxlDataSourceConfig extends DataSourceConfig {
    // 唯一标识
    public static final String SIGN = "xxl-job-mysql";
    public static final String DATASOURCE = SIGN + "DataSource";
    public static final String TRANSACTION_MANAGER = SIGN + "TransactionManager";
    public static final String SQL_SESSION_FACTORY = SIGN + "SqlSessionFactory";
    public static final String PACKAGE = "com.xxl.job.admin.dao";
    public static final String MAPPER_LOCATION = "classpath*:com/xxl/job/admin/dao/*.xml";

    @Bean(name = DATASOURCE)
    public DataSource dataSource() {
        return createDynamicHikariDataSource(SIGN);
    }

    @Bean(name = SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory(@Qualifier(DATASOURCE) DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

    @Bean(name = TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource());
    }

}