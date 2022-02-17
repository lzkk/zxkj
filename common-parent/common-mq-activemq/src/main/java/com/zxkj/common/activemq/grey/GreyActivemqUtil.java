package com.zxkj.common.activemq.grey;

/**
 * @author ：yuhui
 * @date ：Created in 2022/1/19 16:11
 */
public class GreyActivemqUtil {

    /**
     * 根据命令行参数生成灰度发布的后缀信息
     *
     * @return
     */
    public static String generateGreySuffix() {
        String suffix = "";
        String greyPublish = System.getProperty("greySuffix");
        if (greyPublish != null && greyPublish.trim().length() > 0) {
            suffix += "_g" + greyPublish;
        }
        return suffix;
    }
}
