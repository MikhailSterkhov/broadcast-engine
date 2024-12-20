package io.broadcast.wrapper.spigot.dispatcher;

import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.record.Record;
import io.broadcast.wrapper.spigot.extractor.announcement.PermissibleStringAnnouncement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PermissibleSpigotPlayersDispatcher implements BroadcastDispatcher<Player, PermissibleStringAnnouncement> {

    @Override
    public void dispatch(@NotNull Record<Player> record, @NotNull PermissibleStringAnnouncement announcement) {
        Player player = record.getId();

        if (player != null && announcement.hasPermission(player)) {
            player.sendMessage(announcement.getMessage());
        }
    }
}
