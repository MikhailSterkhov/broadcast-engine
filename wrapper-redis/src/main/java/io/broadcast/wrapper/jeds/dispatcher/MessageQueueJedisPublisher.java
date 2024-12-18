package io.broadcast.wrapper.jeds.dispatcher;

import io.broadcast.engine.announcement.ContentedAnnouncement;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MessageQueueJedisPublisher extends AbstractJedisPublisher {

    private static final MessageQueueExtractor SEPARATED_QUEUE_EXTRACTOR
            = MessageQueueExtractor.separated(':');

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
        this.messageQueueExtractor = SEPARATED_QUEUE_EXTRACTOR;
    }

    public MessageQueueJedisPublisher(@NotNull Jedis jedis, @NotNull String channel) {
        super(jedis);
        this.channel = channel;
        this.messageQueueExtractor = SEPARATED_QUEUE_EXTRACTOR;
    }

    @Override
    public void publish(Jedis jedis, String key, String value) {
        jedis.publish(channel, messageQueueExtractor.extractFrom(new ContentedAnnouncement<>(key, value)));
    }
}
