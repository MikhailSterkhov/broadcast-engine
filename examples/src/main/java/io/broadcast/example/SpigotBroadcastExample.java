package io.broadcast.example;

import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.wrapper.jeds.JedisBroadcastPipeline;
import io.broadcast.wrapper.jeds.dispatcher.JedisDispatcher;
import io.broadcast.wrapper.jeds.extractor.JedisRecordExtractor;
import io.broadcast.wrapper.spigot.SpigotBroadcastPipeline;
import io.broadcast.wrapper.spigot.extractor.announcement.PermissibleStringAnnouncement;
import io.broadcast.wrapper.spigot.extractor.announcement.SpigotAnnouncementExtractor;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

public class SpigotBroadcastExample {

    public static void main(String[] args) {
        String permission = "broadcast.join";

        AnnouncementExtractor<PermissibleStringAnnouncement> announcementExtractor =
                AnnouncementExtractor.fromIDAndExtract(String.class, (id) ->
                        SpigotAnnouncementExtractor.permissible(permission,
                                String.format("Player %s has joined to the server!", id)));

        SpigotBroadcastPipeline<String, PermissibleStringAnnouncement> spigotPipeline =
                SpigotBroadcastPipeline.preparePermissibleOnlinePlayersByName()
                        .setAnnouncementExtractor(announcementExtractor);

        BroadcastEngine broadcastEngine = new BroadcastEngine(spigotPipeline);
        broadcastEngine.broadcastNow();
    }
}
