package com.zxkj.gateway.grey;

import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.context.domain.ContextInfo;
import com.zxkj.common.util.sys.SysConfigUtil;
import org.apache.commons.lang3.StringUtils;

public class GreyUtil {

    private static ThreadLocal<ContextInfo> greyMap = new InheritableThreadLocal<>();

    public static ContextInfo getCurrentContext() {
        ContextInfo contextInfo = greyMap.get();
        if (contextInfo == null) {
            contextInfo = new ContextInfo();
        }
        return contextInfo;
    }

    public static ContextInfo initContext(String configInfo, String version, String grp) {
        ContextInfo contextInfo = new ContextInfo();
        String regionPublishStr = SysConfigUtil.getSysConfigValue(ContextConstant.REGION_PUBLISH_FLAG);
        contextInfo.setRegionPublish(regionPublishStr);
        String greyPublish = judgeGrey(configInfo, version, grp);
        if (greyPublish == null) {
            greyPublish = SysConfigUtil.getSysConfigValue(ContextConstant.GREY_PUBLISH_FLAG);
        }
        contextInfo.setGreyPublish(greyPublish);
        greyMap.set(contextInfo);
        return contextInfo;
    }

    public static void fillContext(ContextInfo contextInfo) {
        greyMap.set(contextInfo);
    }

    public static void clearContext() {
        greyMap.remove();
    }


    private static String judgeGrey(String configInfo, String version, String grp) {
        if (StringUtils.isBlank(configInfo) || StringUtils.isBlank(version) || StringUtils.isBlank(grp)) {
            return null;
        }
        String reqKey = version + "_" + grp;
        String[] greyPublishConfigArray = configInfo.split(";");
        for (String greyPublishConfigStr : greyPublishConfigArray) {
            String[] multiEnvArray = greyPublishConfigStr.split(":");
            if (multiEnvArray.length != 2) {
                continue;
            }
            String greyEnvFlag = multiEnvArray[0];
            String configStr = multiEnvArray[1];
            String[] configArray = configStr.split(",");
            if (configArray.length == 0) {
                continue;
            }
            for (String config : configArray) {
                if ("*".equals(config)) {
                    return greyEnvFlag;
                }
                if (config.startsWith("*_")) {
                    String configGrp = config.substring(2);
                    if (grp.equals(configGrp)) {
                        return greyEnvFlag;
                    }
                }
                if (config.equals(reqKey)) {
                    return greyEnvFlag;
                }
            }
        }
        return null;
    }

}
