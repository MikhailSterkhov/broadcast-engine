package io.broadcast.wrapper.jeds.dispatcher;

import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import io.broadcast.wrapper.jeds.extractor.JedisRecordExtractor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RequiredArgsConstructor
public abstract class AbstractJedisPublisher implements JedisDispatcher {

    private final Jedis jedis;

    public AbstractJedisPublisher(@NotNull JedisPool jedisPool) {
        this(jedisPool.getResource());
    }

    public abstract void publish(Jedis jedis, String key, String value);

    @Override
    public void dispatch(@NotNull Record<String> record, @NotNull StringAnnouncement announcement) {
        publish(jedis, record.getId(), announcement.getContent());
    }
}
