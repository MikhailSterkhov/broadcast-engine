package io.broadcast.wrapper.jedis.dispatcher;

import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RPUSHJedisPublisher extends AbstractJedisPublisher {

    public RPUSHJedisPublisher(@NotNull JedisPool jedisPool) {
        super(jedisPool);
    }

    public RPUSHJedisPublisher(@NotNull Jedis jedis) {
        super(jedis);
    }

    @Override
    public void publish(Jedis jedis, String key, String value) {
        jedis.rpush(key, value);
    }
}
