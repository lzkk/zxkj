package com.zxkj.goods.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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
@MapperScan(basePackages = {"com.zxkj.goods.mapper"})
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

//    @Bean
//    public ServletRegistrationBean statViewServlet() {
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
////        // 添加IP白名单
////        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
////        // 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
////        servletRegistrationBean.addInitParameter("deny", "127.0.0.1");
//        // 添加控制台管理用户
//        servletRegistrationBean.addInitParameter("loginUsername", "admin");
//        servletRegistrationBean.addInitParameter("loginPassword", "123456");
//        // 是否能够重置数据
//        servletRegistrationBean.addInitParameter("resetEnable", "false");
//        return servletRegistrationBean;
//    }
//
//    /**
//     * 配置服务过滤器
//     *
//     * @return 返回过滤器配置对象
//     */
//    @Bean
//    public FilterRegistrationBean statFilter() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
//        // 添加过滤规则
//        filterRegistrationBean.addUrlPatterns("/*");
//        // 忽略过滤格式
//        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
//        return filterRegistrationBean;
//    }
}
