package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.wrapper.jeds.JedisBroadcastPipeline;
import io.broadcast.wrapper.jeds.dispatcher.JedisDispatcher;
import io.broadcast.wrapper.jeds.extractor.JedisRecordExtractor;
import redis.clients.jedis.Jedis;

public class JedisBroadcastExample {

    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        AnnouncementExtractor<StringAnnouncement> announcementExtractor =
                AnnouncementExtractor.fromID(String.class, (id) -> new StringAnnouncement(String.format("[ID: %s] -> \"Hello world!\"", id)));

        JedisBroadcastPipeline broadcastPipeline = JedisBroadcastPipeline.createPipeline()
                .setDispatcher(JedisDispatcher.publishQueue(jedis, "players_queue"))
                .setRecordExtractor(JedisRecordExtractor.lrange(jedis, "players"))
                .setAnnouncementExtractor(announcementExtractor);

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }
}
