package io.broadcast.wrapper.spigot.dispatcher;

import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.record.Record;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SpigotPlayersByUuidDispatcher implements BroadcastDispatcher<UUID, StringAnnouncement> {

    @Override
    public void dispatch(@NotNull Record<UUID> record, @NotNull StringAnnouncement announcement) {
        Player player = Bukkit.getPlayer(record.getId());
        
        if (player != null) {
            player.sendMessage(announcement.getContent());
        }
    }
}
