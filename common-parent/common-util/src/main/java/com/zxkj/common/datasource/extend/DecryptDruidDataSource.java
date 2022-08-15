package com.zxkj.common.datasource.extend;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yuhui
 * @Description 继承Druid的Datasource，只做一件事：密码解密
 */
public class DecryptDruidDataSource extends DruidDataSource {
    private final static Logger logger = LoggerFactory.getLogger(DecryptDruidDataSource.class);

    @Override
    public void setPassword(String pwd) {
        String password = pwd;
        try {
            password = ConfigTools.decrypt(pwd);
        } catch (Exception e) {
            logger.error("DecryptDruidSource.password exception:" + e.getMessage(),e);
        }
        super.setPassword(password);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(ConfigTools.encrypt("123456"));
        System.out.println(ConfigTools.decrypt("Biyu5YzU+6sxDRbmWEa3B2uUcImzDo0BuXjTlL505+/pTb+/0Oqd3ou1R6J8+9Fy3CYrM18nBDqf6wAaPgUGOg=="));
    }

}
