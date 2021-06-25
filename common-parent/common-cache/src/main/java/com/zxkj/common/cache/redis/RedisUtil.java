package com.zxkj.common.cache.redis;

import java.util.List;

/**
 * redis操作接口
 *
 * @author ：yuhui
 * @date ：Created in 2021/2/1 9:37
 */
public interface RedisUtil {

    String get(String key);

    void set(String key, String value);

    void set(String key, String value, int seconds);

    boolean setnx(String key, String value);

    boolean setnx(String key, String value, int seconds);

    long expire(String key, int seconds);

    void del(String key);

    Long incr(String key);

    Long incr(String key, int seconds);

    void lset(String key, String... value);

    List<String> lget(String key, long start, long end);

    long lsize(String key);

    Long lpush(String key, String value);

    Long rpush(String key, String value);

    String lpop(String key);

    String rpop(String key);

    List<String> brpop(String... key);

    List<String> blpop(String key);

    Long zadd(String key, double score, String value);

    Long zrem(String key, String value);

    Long hset(String key, String field, String value);

    Long hdel(String key, String field);

    String getSet(String key, String value);

    List<String> scan(String key);
}
