package com.zxkj.common.context.domain;

import lombok.Data;

/**
 * 客户数据
 */
@Data
public class CustomerInfo {
    /**
     * 客户主键ID
     */
    private Long customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户层级ID
     */
    private Long customerOrgId;
    /**
     * 版本号
     */
    private String version;
    /**
     * 客户端来源
     */
    private String grp;
    /**
     * 区域发布标识
     */
    private String regionPublish;
    /**
     * 灰度发布标识
     */
    private String greyPublish;

}
