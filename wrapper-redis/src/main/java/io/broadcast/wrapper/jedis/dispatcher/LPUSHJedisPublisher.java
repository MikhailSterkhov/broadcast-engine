package io.broadcast.wrapper.jedis.dispatcher;

import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class LPUSHJedisPublisher extends AbstractJedisPublisher {

    public LPUSHJedisPublisher(@NotNull JedisPool jedisPool) {
        super(jedisPool);
    }

    public LPUSHJedisPublisher(@NotNull Jedis jedis) {
        super(jedis);
    }

    @Override
    public void publish(Jedis jedis, String key, String value) {
        jedis.lpush(key, value);
    }
}
