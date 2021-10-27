package com.zxkj.common.cache.redis.cluster;

import com.zxkj.common.cache.redis.RedisConfig;
import com.zxkj.common.cache.redis.Cache;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ScanParams;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * redis集群对象
 */
public class RedisCluster implements Cache {
    private static final Logger log = LoggerFactory.getLogger(RedisCluster.class);
    private static String REDIS_FLAG = "redis-cluster";
    private static final String CONN_TEST_KEY = "conn_test";
    private static final Integer CONN_TEST_MICRO_SECONDS = 3000;

    protected String REDIS_PRE_KEY = "";
    protected RedisConfig redisConfig = null;
    protected JedisCluster jedisCluster;

    public RedisCluster(String redisPreKey, RedisConfig redisConfig) {
        this.REDIS_PRE_KEY = redisPreKey;
        this.redisConfig = redisConfig;
    }

    public RedisCluster(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    /**
     * 初始化
     */
    public void init() {
        if (jedisCluster != null) {
            log.info("{},已经建立连接!!!!!!", REDIS_FLAG);
            return;
        }
        if (redisConfig == null) {
            throw new RuntimeException(REDIS_FLAG + "配置信息为空，请先添加配置");
        }
        log.info("初始化{}连接开始-------------", REDIS_FLAG);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getMaxIdle());
        poolConfig.setMinIdle(redisConfig.getMinIdle());
        poolConfig.setMaxTotal(redisConfig.getMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
        String address = redisConfig.getClusterAddress();
        if (address == null || address.length() == 0) {
            throw new RuntimeException(REDIS_FLAG + ",IP或端口信息为空，请先添加配置");
        }
        Set<HostAndPort> nodeList = new HashSet<HostAndPort>();
        String[] configArray = address.split(",");
        if (configArray != null && configArray.length > 0) {
            for (String tmp : configArray) {
                String[] tmpAddress = tmp.split(":");
                String ip = tmpAddress[0];
                String port = tmpAddress[1];
                log.info("{}:{}", ip, port);
                nodeList.add(new HostAndPort(ip, Integer.parseInt(port)));
            }
        }
        this.jedisCluster = new JedisCluster(nodeList, redisConfig.getTimeout(), redisConfig.getMaxRedirections(), poolConfig);
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

    private String getPreKey(String key) {
        return REDIS_PRE_KEY + key;
    }

    public String get(String key) {
        key = getPreKey(key);
        try {
            String value = jedisCluster.get(key);
            return value;
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return null;
    }

    public void set(String key, String value) {
        key = getPreKey(key);
        try {
            jedisCluster.set(key, value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
    }

    public void set(String key, String value, int seconds) {
        key = getPreKey(key);
        try {
            jedisCluster.set(key, value);
            if (seconds > 0) {
                jedisCluster.expire(key, seconds);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
    }

    public boolean setnx(String key, String value) {
        key = getPreKey(key);
        try {
            long count = jedisCluster.setnx(key, value);
            return count == 1;
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return false;
    }

    public boolean setnx(String key, String value, int seconds) {
        key = getPreKey(key);
        try {
            long count = jedisCluster.setnx(key, value);
            if (count == 1) {
                jedisCluster.expire(key, seconds);
            }
            return count == 1;
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return false;
    }

    public long expire(String key, int seconds) {
        key = getPreKey(key);
        try {
            return jedisCluster.expire(key, seconds);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return -1L;
    }

    public void del(String key) {
        key = getPreKey(key);
        try {
            jedisCluster.del(key);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
    }

    public Long incr(String key) {
        key = getPreKey(key);
        try {
            long count = jedisCluster.incr(key);
            return count;
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    public Long incr(String key, int seconds) {
        key = getPreKey(key);
        try {
            long count = jedisCluster.incr(key);
            if (count == 1) {
                jedisCluster.expire(key, seconds);
            }
            return count;
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    public void lset(String key, String... value) {
        key = getPreKey(key);
        try {
            jedisCluster.lpush(key, value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
    }

    public List<String> lget(String key, long start, long end) {
        key = getPreKey(key);
        try {
            List<String> lrange = jedisCluster.lrange(key, start, end);
            return lrange;
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return null;
    }

    public long lsize(String key) {
        key = getPreKey(key);
        try {
            return jedisCluster.llen(key);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    public Long lpush(String key, String value) {
        key = getPreKey(key);
        try {
            return jedisCluster.lpush(key, value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    public Long rpush(String key, String value) {
        key = getPreKey(key);
        try {
            return jedisCluster.rpush(key, value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    public String lpop(String key) {
        key = getPreKey(key);
        try {
            return jedisCluster.lpop(key);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return null;
    }

    public String rpop(String key) {
        key = getPreKey(key);
        try {
            return jedisCluster.rpop(key);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return null;
    }

    public List<String> brpop(String... key) {
//        key = getPreKey(key);
        try {
            return jedisCluster.brpop(0, key);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return null;
    }

    public List<String> blpop(String key) {
        key = getPreKey(key);
        try {
            return jedisCluster.blpop(0, key);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return null;
    }

    @Override
    public Long zadd(String key, double score, String value) {
        key = getPreKey(key);
        try {
            return jedisCluster.zadd(key, score, value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    @Override
    public Long zrem(String key, String value) {
        key = getPreKey(key);
        try {
            return jedisCluster.zrem(key, value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    @Override
    public Long hset(String key, String field, String value) {
        key = getPreKey(key);
        try {
            return jedisCluster.hset(key, field, value);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    @Override
    public String hget(String key, String field) {
        key = getPreKey(key);
        try {
            return jedisCluster.hget(key, field);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return null;
    }

    @Override
    public Long hincre(String key, String field, Integer num) {
        key = getPreKey(key);
        try {
            return jedisCluster.hincrBy(key, field, num);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    @Override
    public Long hdel(String key, String field) {
        key = getPreKey(key);
        try {
            return jedisCluster.hdel(key, field);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return 0L;
    }

    @Override
    public String getSet(String key, String field) {
        key = getPreKey(key);
        try {
            return jedisCluster.getSet(key, field);
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return null;
    }

    @Override
    public List<String> scan(String key) {
        key = getPreKey(key);
        try {
            ScanParams scanParams = new ScanParams();
            scanParams.match(key + "*");
            return jedisCluster.scan("0", scanParams).getResult();
        } catch (Exception e) {
            log.warn(e.getMessage());
        } finally {

        }
        return null;
    }
}
