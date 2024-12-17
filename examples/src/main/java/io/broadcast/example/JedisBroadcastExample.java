package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.dispatch.STDOUTBroadcastDispatcher;
import io.broadcast.wrapper.jeds.JedisRecordExtractor;
import redis.clients.jedis.Jedis;

public class JedisBroadcastExample {

    private static JedisRecordExtractor createJedisExtractor(String key) {
        Jedis jedis = new Jedis();
        jedis.hset(key, "itzstonlex", "");

        return JedisRecordExtractor.hgetall(jedis, key);
    }

    public static void main(String[] args) {
        AnnouncementExtractor<StringAnnouncement> announcementExtractor =
                AnnouncementExtractor.fromID(Integer.class, (id) -> new StringAnnouncement(String.format("[ID: %s] -> \"Hello world!\"", id)));

        BroadcastPipeline<String, StringAnnouncement> broadcastPipeline = BroadcastPipeline.createPipeline(String.class, StringAnnouncement.class)
                .setDispatcher(new STDOUTBroadcastDispatcher<>())
                .setRecordExtractor(createJedisExtractor("players"))
                .setAnnouncementExtractor(announcementExtractor);

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }
}
