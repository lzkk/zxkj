package com.zxkj.common.util.greyPublish;

import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.context.domain.CustomerInfo;
import org.apache.commons.lang3.StringUtils;

public class GreyPublishUtil {

    private static ThreadLocal<CustomerInfo> currentCustomer = new ThreadLocal<>();

    public static CustomerInfo getPublishInfo(String[] paramArray) {
        CustomerInfo customerInfo = new CustomerInfo();
        String regionPublishStr = getValueByKey(paramArray, ContextConstant.REGION_PUBLISH_FLAG);
        customerInfo.setRegionPublish(regionPublishStr);
        String greyPublishStr = getValueByKey(paramArray, ContextConstant.GREY_PUBLISH_FLAG);
        customerInfo.setGreyPublish(greyPublishStr);
        return customerInfo;
    }

    private static String getValueByKey(String[] paramArray, String flag) {
        if (paramArray == null || paramArray.length == 0) {
            return null;
        }
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
            if (StringUtils.isBlank(value)) {
                continue;
            }
            if (key.equals(flag)) {
                return value;
            }
        }
        return null;
    }

    private static String isGreyPublish(String configInfo, String version, String grp) {
        if (StringUtils.isBlank(configInfo)) {
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
                if (config.equals(reqKey)) {
                    return greyEnvFlag;
                }
            }
        }
        return null;
    }

    public static CustomerInfo getCustomerInfo(String[] paramArray, String configInfo, String version, String grp) {
        CustomerInfo customerInfo = new CustomerInfo();
        String regionPublishStr = getValueByKey(paramArray, ContextConstant.REGION_PUBLISH_FLAG);
        customerInfo.setRegionPublish(regionPublishStr);
        String greyPublish = isGreyPublish(configInfo, version, grp);
        customerInfo.setGreyPublish(greyPublish);
        currentCustomer.set(customerInfo);
        return customerInfo;
    }

    public static CustomerInfo getCustomerInfo() {
        CustomerInfo customerInfo = currentCustomer.get();
        if (customerInfo == null) {
            customerInfo = new CustomerInfo();
        }
        return customerInfo;
    }

}
