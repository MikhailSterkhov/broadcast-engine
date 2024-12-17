package io.broadcast.wrapper.jeds;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class LRANGEJedisRecordExtractor extends AbstractJedisRecordExtractor {

    public LRANGEJedisRecordExtractor(Jedis jedis, String key) {
        super(jedis, key);
    }

    public LRANGEJedisRecordExtractor(JedisPool jedisPool, String key) {
        super(jedisPool, key);
    }

    @Override
    protected Iterable<String> getValues(Jedis jedis, String key) {
        return jedis.lrange(key, 0, -1);
    }
}
