package com.zxkj.common.cache.lock;

import com.zxkj.common.cache.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class RedisLock extends AbstractLock {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    private RedisUtil redisUtil;

    private Random retryMillisRandom;

    protected String lockKey;
    // 锁的有效时长(毫秒)
    protected long lockExpires;

    public RedisLock(RedisUtil redisUtil, String lockKey, long lockExpires) {
        this.redisUtil = redisUtil;
        this.lockKey = lockKey;
        this.lockExpires = lockExpires;
        this.retryMillisRandom = new Random(System.currentTimeMillis());
    }

    protected boolean lock(long timeout, TimeUnit unit, boolean interrupt) throws InterruptedException {
        if (interrupt) {
            checkInterruption();
        }
        long timeoutMillis = unit == null ? 0 : unit.toMillis(timeout);

        while (timeoutMillis >= 0) {
            if (interrupt) {
                checkInterruption();
            }

            long lockExpireTime = System.currentTimeMillis() + lockExpires + 1;// 锁超时时间
            String stringOfLockExpireTime = String.valueOf(lockExpireTime);
            if (setNX(lockKey, stringOfLockExpireTime)) { // 获取到锁
                // 成功获取到锁, 设置相关标识
                locked = true;
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }

            String value = this.get(lockKey);
            if (value != null && isTimeExpired(value)) { // lock is expired
                String oldValue = this.getSet(lockKey, stringOfLockExpireTime);
                if (oldValue != null && oldValue.equals(value)) {
                    locked = true;
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            }

            long delayMillis = randomDelay();
            long sleepMillis = timeoutMillis < delayMillis ? timeoutMillis : delayMillis;
            Thread.sleep(sleepMillis);
            timeoutMillis = timeoutMillis - sleepMillis == 0 ? -1 : timeoutMillis - sleepMillis;
        }
        return false;
    }

    private long randomDelay() {
        return retryMillisRandom.nextInt(50) + 50;
    }

    public boolean isLocked() {
        if (locked) {
            return true;
        } else {
            String value = redisUtil.get(lockKey);
            return !isTimeExpired(value);
        }
    }

    @Override
    protected void unlock0() {
        // 判断锁是否过期
        String value = redisUtil.get(lockKey);
        if (!isTimeExpired(value)) {
            doUnlock();
        }
    }

    private void checkInterruption() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    private boolean isTimeExpired(String value) {
        return value == null || Long.parseLong(value) < System.currentTimeMillis();
    }

    private void doUnlock() {
        redisUtil.del(lockKey);
    }

    public String getLockKey() {
        return lockKey;
    }

    private String get(final String key) {
        return redisUtil.get(key);
    }

    private boolean setNX(final String key, final String value) {
        return redisUtil.setnx(key, value);
    }

    private String getSet(final String key, final String value) {
        return redisUtil.getSet(key, value);
    }

}