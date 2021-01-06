package org.sky.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

public class RedisLock {

    private static RedisLock redisLock;

    private final JedisPool jedisPool = new JedisPool();

    public static RedisLock getInstance() {
        if (null == redisLock) {
            redisLock = new RedisLock();
        }
        return redisLock;
    }

    public boolean lock(String lock, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            return "OK".equals(jedis.set(lock, lock, "NX", "EX", seconds));
        }
    }

    public boolean release(String lock) {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                "    return redis.call('del',KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end";
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.eval(luaScript, Collections.singletonList(lock), Collections.singletonList(lock)).equals(1L);
        }
    }
}
