package io.broadcast.wrapper.jedis.extractor;

import io.broadcast.engine.record.extract.RecordExtractor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public interface JedisRecordExtractor extends RecordExtractor<String> {

    @Contract("_, _ -> new")
    static @NotNull JedisRecordExtractor hgetall(Jedis jedis, String key) {
        return new HGETJedisRecordExtractor(jedis, key);
    }

    @Contract("_, _ -> new")
    static @NotNull JedisRecordExtractor hgetall(JedisPool jedis, String key) {
        return new HGETJedisRecordExtractor(jedis, key);
    }

    @Contract("_, _ -> new")
    static @NotNull JedisRecordExtractor lrange(Jedis jedis, String key) {
        return new LRANGEJedisRecordExtractor(jedis, key);
    }

    @Contract("_, _ -> new")
    static @NotNull JedisRecordExtractor lrange(JedisPool jedis, String key) {
        return new LRANGEJedisRecordExtractor(jedis, key);
    }
}
