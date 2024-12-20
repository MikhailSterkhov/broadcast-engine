package io.broadcast.example.multistep;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.wrapper.jeds.extractor.JedisRecordExtractor;
import io.broadcast.wrapper.spigot.dispatcher.SpigotDispatcher;
import redis.clients.jedis.Jedis;

public class Multistep_RedisToSpigotBroadcastExample {

    public static void main(String[] args) {
        Jedis jedis = new Jedis();

        AnnouncementExtractor<StringAnnouncement> announcementExtractor =
                AnnouncementExtractor.fromID(String.class, (id) ->
                        new StringAnnouncement(String.format("Hello, %s! Your account has detected in Redis database!", id)));

        BroadcastPipeline<String, StringAnnouncement> broadcastPipeline = BroadcastPipeline.createPipeline(String.class, StringAnnouncement.class)
                .setAnnouncementExtractor(announcementExtractor)
                .setDispatcher(SpigotDispatcher.onlinePlayersByName())
                .setRecordExtractor(JedisRecordExtractor.lrange(jedis, "players"));

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.broadcastNow();
    }
}
