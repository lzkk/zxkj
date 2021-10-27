package com.zxkj.order.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * mybatis配置
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.zxkj.order.mapper"})
public class MybatisConfig implements TransactionManagementConfigurer {

    @Autowired
    private Environment environment;

    @Bean(initMethod = "init")
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

//    @Bean
//    @ConditionalOnBean(DataSource.class)
//    public DataSourceProxy dataSourceProxy() {
//        return new DataSourceProxy(druidDataSource());
//    }

    /*****
     * 替换MyBatis的数据源（DataSourceProxy）
     */
    @Bean
    @ConfigurationProperties(prefix = "mybatis")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        // 这里用 MybatisSqlSessionFactoryBean 代替了 SqlSessionFactoryBean，否则 MyBatisPlus 不会生效
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        return mybatisSqlSessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(druidDataSource());
    }
}
