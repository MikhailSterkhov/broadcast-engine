package io.broadcast.wrapper.jeds.extractor;

import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RequiredArgsConstructor
public abstract class AbstractJedisRecordExtractor implements JedisRecordExtractor {

    private final Jedis jedis;
    private final String key;

    public AbstractJedisRecordExtractor(@NotNull JedisPool jedisPool, @NotNull String key) {
        this(jedisPool.getResource(), key);
    }

    protected abstract Iterable<String> getValues(Jedis jedis, String key);

    @Override
    public void extract(@NotNull RecordObserver<String> recordObserver) {
        try {
            getValues(jedis, key).forEach(s -> recordObserver.observe(new Record<>(s)));
        } finally {
            jedis.close();
        }
    }
}
