package com.zxkj.common.cache.redis.codis;

import com.zxkj.common.cache.redis.AbstractRedis;
import com.zxkj.common.cache.redis.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis-codis
 */
public class RedisCodis extends AbstractRedis {
    private static final Logger log = LoggerFactory.getLogger(RedisCodis.class);

    private static String REDIS_FLAG = "redis-codis";
    private static final String CONN_TEST_KEY = "conn_test";
    private static final Integer CONN_TEST_MICRO_SECONDS = 3000;

    protected String REDIS_PRE_KEY = "";
    protected RedisConfig redisConfig = null;
    private JedisPool jedisPool;

    public RedisCodis(String redisPreKey, RedisConfig redisConfig) {
        this.REDIS_PRE_KEY = redisPreKey;
        this.redisConfig = redisConfig;
    }

    public RedisCodis(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    /**
     * 初始化
     */
    public void init() {
        if (jedisPool != null) {
            log.info("{},已经建立连接!!!!!!", REDIS_FLAG);
            return;
        }
        if (redisConfig == null) {
            throw new RuntimeException(REDIS_FLAG + "配置信息为空，请先添加配置");
        }
        log.info("初始化{}连接开始-------------", REDIS_FLAG);
        String ip = redisConfig.getSingleIp();
        Integer port = redisConfig.getSinglePort();
        if (ip == null || port == 0) {
            throw new RuntimeException(REDIS_FLAG + ",IP或端口信息为空，请先添加配置");
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(redisConfig.getMinIdle());
        jedisPoolConfig.setMaxIdle(redisConfig.getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
        jedisPool = new JedisPool(jedisPoolConfig, ip, port, redisConfig.getTimeout());
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
        if (jedisPool != null) {
            return jedisPool.getResource();
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
