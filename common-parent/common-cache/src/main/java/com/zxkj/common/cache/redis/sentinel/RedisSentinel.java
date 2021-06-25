package com.zxkj.common.cache.redis.sentinel;

import com.zxkj.common.cache.redis.AbstractRedis;
import com.zxkj.common.cache.redis.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * redis-sentinel
 */
public class RedisSentinel extends AbstractRedis {
    private static final Logger log = LoggerFactory.getLogger(RedisSentinel.class);

    private static String REDIS_FLAG = "redis-sentinel";
    private static final String CONN_TEST_KEY = "conn_test";
    private static final Integer CONN_TEST_MICRO_SECONDS = 3000;
    protected String REDIS_PRE_KEY = "";
    protected RedisConfig redisConfig = null;
    private JedisSentinelPool jedisSentinelPool;

    public RedisSentinel(String redisPreKey, RedisConfig redisConfig) {
        this.REDIS_PRE_KEY = redisPreKey;
        this.redisConfig = redisConfig;
    }

    public RedisSentinel(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    /**
     * 初始化
     */
    public void init() {
        if (jedisSentinelPool != null) {
            log.info("{},已经建立连接!!!!!!", REDIS_FLAG);
            return;
        }
        if (redisConfig == null) {
            throw new RuntimeException(REDIS_FLAG + "配置信息为空，请先添加配置");
        }
        log.info("初始化{}连接开始-------------", REDIS_FLAG);
        String masterName = redisConfig.getSentinelMasterName();
        if (masterName == null) {
            throw new RuntimeException(REDIS_FLAG + ",masterName为空，请先添加配置");
        }
        String nodes = redisConfig.getSentinelNodes();
        if (nodes == null || nodes.length() == 0) {
            throw new RuntimeException(REDIS_FLAG + ",IP和端口信息为空，请先添加配置");
        }
        String[] configArray = nodes.split(",");
        Set<String> nodeList = new HashSet<>();
        nodeList.addAll(Arrays.asList(configArray));

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getMaxIdle());
        poolConfig.setMinIdle(redisConfig.getMinIdle());
        poolConfig.setMaxTotal(redisConfig.getMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
        jedisSentinelPool = new JedisSentinelPool(redisConfig.getSentinelMasterName(), nodeList, poolConfig, redisConfig.getTimeout(), redisConfig.getPassword());
        log.info("初始化{}连接结束-------------", REDIS_FLAG);
        mock();
    }

    /**
     * 测试连接
     */
    private void mock() {
        log.info("测试{}连接开始---", REDIS_FLAG);
        try {
            this.set(CONN_TEST_KEY, CONN_TEST_KEY, CONN_TEST_MICRO_SECONDS);
            log.info(this.get(CONN_TEST_KEY));
        } catch (Exception e) {
            log.error("测试{}连接失败", REDIS_FLAG, e);
            throw new RuntimeException(e);
        }
        log.info("测试{}连接结束---", REDIS_FLAG);
    }


    @Override
    public Jedis getJedis() {
        if (jedisSentinelPool != null) {
            return jedisSentinelPool.getResource();
        }
        return null;
    }

    @Override
    public void close(Jedis jedis) {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
