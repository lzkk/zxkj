package com.zxkj.common.datasource.extend;

import com.alibaba.druid.filter.config.ConfigTools;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuhui
 * @Description 继承Hikari的Datasource，只做一件事：密码解密
 */
public class DecryptHikariDataSource extends HikariDataSource {
    private final static Logger logger = LoggerFactory.getLogger(DecryptHikariDataSource.class);

    @Override
    public void setPassword(String pwd) {
        String password = pwd;
        try {
            password = ConfigTools.decrypt(pwd);
        } catch (Exception e) {
            logger.error("DecryptHikariDataSource.password exception:" + e.getMessage(), e);
        }
        super.setPassword(password);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(ConfigTools.encrypt("123#abc"));
        System.out.println(ConfigTools.decrypt("Biyu5YzU+6sxDRbmWEa3B2uUcImzDo0BuXjTlL505+/pTb+/0Oqd3ou1R6J8+9Fy3CYrM18nBDqf6wAaPgUGOg=="));
    }

}
