package com.zxkj.common.util;

import com.zxkj.common.context.constants.ContextConstant;

public class RegionPublishUtil {

    public static boolean isRegionPublish(String[] paramArray) {
        boolean regionPublish = false;
        if (paramArray != null && paramArray.length > 0) {
            for (String tmp : paramArray) {
                if (!tmp.contains("=")) {
                    continue;
                }
                String[] tmpArray = tmp.split("=");
                String key = tmpArray[0];
                if (key.startsWith("-D")) {
                    key = key.substring(2);
                }
                String value = tmpArray[1];
                if (ContextConstant.REGION_PUBLISH.equals(key) && "true".equals(value)) {
                    regionPublish = true;
                }
            }
        }
        return regionPublish;
    }
}
