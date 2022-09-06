package com.zxkj.xxl.util;

import com.zxkj.common.context.constants.ContextConstant;

/**
 * @author ：yuhui
 * @date ：Created in 2022/1/19 16:11
 */
public class GreyXxlUtil {

    /**
     * 根据命令行参数生成灰度发布的后缀信息
     *
     * @return
     */
    public static String generateGreySuffix() {
        String suffix = "";
        String greyStr = System.getProperty(ContextConstant.GREY_PUBLISH_FLAG);
        if (greyStr != null && greyStr.trim().length() > 0) {
            suffix += "_g" + greyStr;
        }
        String regionStr = System.getProperty(ContextConstant.REGION_PUBLISH_FLAG);
        if (regionStr != null && regionStr.trim().length() > 0) {
            suffix += "_r" + regionStr;
        }
        return suffix;
    }
}
