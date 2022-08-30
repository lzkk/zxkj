package com.zxkj.common.rabbitmq.grey;

/**
 * @author ：yuhui
 * @date ：Created in 2022/1/19 16:11
 */
public class GreyRabbitmqUtil {

    /**
     * 根据命令行参数生成灰度发布的后缀信息
     *
     * @return
     */
    public static String generateGreySuffix() {
        String suffix = "";
        String greyPublish = System.getProperty("mqSuffix");
        if (greyPublish != null && greyPublish.trim().length() > 0) {
            suffix += "_g" + greyPublish;
        }
        return suffix;
    }
}
