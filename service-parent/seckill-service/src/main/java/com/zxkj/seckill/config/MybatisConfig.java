package com.zxkj.seckill.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * mybatis配置
 */
@Configuration
@MapperScan(basePackages = {"com.zxkj.seckill.mapper"})
public class MybatisConfig {
    private static final String SIGN = "seckill";
    private static final String DATASOURCE = SIGN + "Datasource";
    private static final String TRANSACTION_MANAGER = SIGN + "TransactionManager";
    static final String SQL_SESSION_FACTORY = SIGN + "SqlSessionFactory";

    @Autowired
    private Environment environment;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Bean(name = DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        String envPrefix = environment.getProperty("spring.application.name");
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(environment.getProperty(envPrefix + ".datasource.driver-class-name"));
        druidDataSource.setUrl(environment.getProperty(envPrefix + ".datasource.url"));
        druidDataSource.setUsername(environment.getProperty(envPrefix + ".datasource.username"));
        druidDataSource.setPassword(environment.getProperty(envPrefix + ".datasource.password"));
        return druidDataSource;
    }

    @Bean(name = SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier(DATASOURCE) DataSource dataSource) throws Exception {
        //这里用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        sessionFactoryBean.setPlugins(mybatisPlusInterceptor());
        return sessionFactoryBean.getObject();
    }

    /**
     * mybatisPlus 分页配置
     *
     * @return
     */
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
