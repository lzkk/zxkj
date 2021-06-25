package com.zxkj.common.cache.redis;

/**
 * redis配置信息
 *
 * @author ：yuhui
 * @date ：Created in 2020/9/24 17:20
 */
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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxRedirections() {
        return maxRedirections;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSentinelMasterName() {
        return sentinelMasterName;
    }

    public void setSentinelMasterName(String sentinelMasterName) {
        this.sentinelMasterName = sentinelMasterName;
    }

    public String getSentinelNodes() {
        return sentinelNodes;
    }

    public void setSentinelNodes(String sentinelNodes) {
        this.sentinelNodes = sentinelNodes;
    }

    public String getClusterAddress() {
        return clusterAddress;
    }

    public void setClusterAddress(String clusterAddress) {
        this.clusterAddress = clusterAddress;
    }

    public String getSingleIp() {
        return singleIp;
    }

    public void setSingleIp(String singleIp) {
        this.singleIp = singleIp;
    }

    public Integer getSinglePort() {
        return singlePort;
    }

    public void setSinglePort(Integer singlePort) {
        this.singlePort = singlePort;
    }
}
