package io.broadcast.wrapper.spigot.dispatcher;

import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.record.Record;
import io.broadcast.wrapper.spigot.extractor.announcement.PermissibleStringAnnouncement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PermissibleSpigotPlayersByUuidDispatcher implements BroadcastDispatcher<UUID, PermissibleStringAnnouncement> {

    @Override
    public void dispatch(@NotNull Record<UUID> record, @NotNull PermissibleStringAnnouncement announcement) {
        Player player = Bukkit.getPlayer(record.getId());

        if (player != null && announcement.hasPermission(player)) {
            player.sendMessage(announcement.getMessage());
        }
    }
}
