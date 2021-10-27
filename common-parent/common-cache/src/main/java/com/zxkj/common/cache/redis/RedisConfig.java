package com.zxkj.common.cache.redis;

import lombok.Data;

/**
 * redis配置信息
 *
 * @author ：yuhui
 * @date ：Created in 2020/9/24 17:20
 */
@Data
public class RedisConfig {

    //redis连接超时时间
    private int timeout;

    //redis池最大数
    private int maxTotal;

    //redis池最大空闲数
    private int maxIdle;

    //redis吃最小空闲数
    private int minIdle;

    //redis最大重定向数
    private int maxRedirections;

    // redis最大等待时间
    private int maxWaitMillis;

    // redis 连接密码
    private String password;

    // redis 哨兵master name
    private String sentinelMasterName;

    // redis 哨兵节点列表
    private String sentinelNodes;

    // redis 集群地址
    private String clusterAddress;

    // redis 单节点Ip
    private String singleIp;

    // redis 单节点端口
    private Integer singlePort;
}
