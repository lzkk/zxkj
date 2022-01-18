package com.zxkj.common.util.sys;

public class SysConfigUtil {

    public static String getSysConfigValue(String key) {
        return System.getProperty(key);
    }

}
