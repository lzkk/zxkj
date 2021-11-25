package com.zxkj.common.context;

import com.zxkj.common.context.constants.ContextConstant;
import com.zxkj.common.context.domain.CustomerInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 客户数据上下文,用于获取当前客户数据
 */
public class CustomerContext {

    private static ThreadLocal<CustomerInfo> currentCustomer = new ThreadLocal<>();

    /**
     * 获取当前客户信息
     */
    public static CustomerInfo getCurrentCustomer() {
        return currentCustomer.get();
    }

    public static void initContext() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        CustomerInfo customerUser = new CustomerInfo();
        String regionPublishStr = getHeaderString(request, ContextConstant.REGION_PUBLISH);
        if ("true".equals(regionPublishStr)) {
            customerUser.setRegionPublish(true);
        } else {
            customerUser.setRegionPublish(false);
        }
        currentCustomer.set(customerUser);
    }

    private static String getHeaderString(HttpServletRequest request, String key) {
        String header = request.getHeader(key);
        if (StringUtils.isBlank(header)) {
            return header;
        }
        try {
            return URLDecoder.decode(header, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
