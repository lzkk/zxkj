package com.zxkj.seckill.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zxkj.common.datasource.DataSourceConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * mybatis配置
 */
@Configuration
@MapperScan(basePackages = SeckillDataSourceConfig.PACKAGE, sqlSessionFactoryRef = SeckillDataSourceConfig.SQL_SESSION_FACTORY)
public class SeckillDataSourceConfig extends DataSourceConfig {
    private static final String SIGN = "seckill-mysql";
    private static final String DATASOURCE = SIGN + "Datasource";
    private static final String TRANSACTION_MANAGER = SIGN + "TransactionManager";
    public static final String SQL_SESSION_FACTORY = SIGN + "SqlSessionFactory";
    public static final String PACKAGE = "com.zxkj.seckill.mapper";
    private static final String MAPPER_LOCATION = "classpath*:com/zxkj/seckill/mapper/**/*.xml";

    @Bean(name = DATASOURCE)
    public DataSource druidDataSource() {
        return createDynamicDruidDataSource(SIGN);
    }

    @Bean(name = SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier(DATASOURCE) DataSource dataSource) throws Exception {
        //这里用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        sessionFactoryBean.setPlugins(mybatisPlusInterceptor());
        return sessionFactoryBean.getObject();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean(name = TRANSACTION_MANAGER)
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(druidDataSource());
    }

}
