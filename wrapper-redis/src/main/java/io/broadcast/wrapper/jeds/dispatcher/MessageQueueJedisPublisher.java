package io.broadcast.wrapper.jeds.dispatcher;

import io.broadcast.engine.announcement.ContentedAnnouncement;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.function.Function;

public class MessageQueueJedisPublisher extends AbstractJedisPublisher {
    private static final MessageQueueExtractor SPLIT_VALUE_EXTRACTOR =
             announcement -> String.format("%s:%s", announcement.getSubject(), announcement.getContent());

    private final String channel;
    private final MessageQueueExtractor messageQueueExtractor;

    public MessageQueueJedisPublisher(@NotNull JedisPool jedisPool, @NotNull String channel,
                                      @NotNull MessageQueueExtractor messageQueueExtractor) {
        super(jedisPool);
        this.channel = channel;
        this.messageQueueExtractor = messageQueueExtractor;
    }

    public MessageQueueJedisPublisher(@NotNull Jedis jedis, @NotNull String channel,
                                      @NotNull MessageQueueExtractor messageQueueExtractor) {
        super(jedis);
        this.channel = channel;
        this.messageQueueExtractor = messageQueueExtractor;
    }

    public MessageQueueJedisPublisher(@NotNull JedisPool jedisPool, @NotNull String channel) {
        super(jedisPool);
        this.channel = channel;
        this.messageQueueExtractor = SPLIT_VALUE_EXTRACTOR;
    }

    public MessageQueueJedisPublisher(@NotNull Jedis jedis, @NotNull String channel) {
        super(jedis);
        this.channel = channel;
        this.messageQueueExtractor = SPLIT_VALUE_EXTRACTOR;
    }

    @Override
    public void publish(Jedis jedis, String key, String value) {
        jedis.publish(channel, messageQueueExtractor.extractFrom(new ContentedAnnouncement<>(key, value)));
    }
}
