package io.broadcast.wrapper.jeds.extractor;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class HGETJedisRecordExtractor extends AbstractJedisRecordExtractor {

    public HGETJedisRecordExtractor(Jedis jedis, String key) {
        super(jedis, key);
    }

    public HGETJedisRecordExtractor(JedisPool jedisPool, String key) {
        super(jedisPool, key);
    }

    @Override
    protected Iterable<String> getValues(Jedis jedis, String key) {
        return jedis.hgetAll(key).keySet();
    }
}
