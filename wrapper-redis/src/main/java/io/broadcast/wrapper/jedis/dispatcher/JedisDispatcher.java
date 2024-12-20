package io.broadcast.wrapper.jedis.dispatcher;

import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public interface JedisDispatcher extends BroadcastDispatcher<String, StringAnnouncement> {

    String REDIS_CHANNEL_NAME = "broadcast";

    @Contract("_ -> new")
    static @NotNull JedisDispatcher lpush(@NotNull JedisPool jedisPool) {
        return new LPUSHJedisPublisher(jedisPool);
    }

    @Contract("_ -> new")
    static @NotNull JedisDispatcher lpush(@NotNull Jedis jedis) {
        return new LPUSHJedisPublisher(jedis);
    }

    @Contract("_ -> new")
    static @NotNull JedisDispatcher rpush(@NotNull JedisPool jedisPool) {
        return new RPUSHJedisPublisher(jedisPool);
    }

    @Contract("_ -> new")
    static @NotNull JedisDispatcher rpush(@NotNull Jedis jedis) {
        return new RPUSHJedisPublisher(jedis);
    }

    @Contract("_ -> new")
    static @NotNull JedisDispatcher publishQueue(@NotNull JedisPool jedisPool) {
        return new MessageQueueJedisPublisher(jedisPool, REDIS_CHANNEL_NAME);
    }

    @Contract("_ -> new")
    static @NotNull JedisDispatcher publishQueue(@NotNull Jedis jedis) {
        return new MessageQueueJedisPublisher(jedis, REDIS_CHANNEL_NAME);
    }

    @Contract("_, _ -> new")
    static @NotNull JedisDispatcher publishQueue(@NotNull JedisPool jedisPool, @NotNull String channel) {
        return new MessageQueueJedisPublisher(jedisPool, channel);
    }

    @Contract("_, _ -> new")
    static @NotNull JedisDispatcher publishQueue(@NotNull Jedis jedis, @NotNull String channel) {
        return new MessageQueueJedisPublisher(jedis, channel);
    }

    @Contract("_, _, _ -> new")
    static @NotNull JedisDispatcher publishQueue(@NotNull JedisPool jedisPool, @NotNull String channel,
                                                 @NotNull MessageQueueExtractor messageQueueExtractor) {
        return new MessageQueueJedisPublisher(jedisPool, channel, messageQueueExtractor);
    }

    @Contract("_, _, _ -> new")
    static @NotNull JedisDispatcher publishQueue(@NotNull Jedis jedis, @NotNull String channel,
                                                 @NotNull MessageQueueExtractor messageQueueExtractor) {
        return new MessageQueueJedisPublisher(jedis, channel, messageQueueExtractor);
    }
}
