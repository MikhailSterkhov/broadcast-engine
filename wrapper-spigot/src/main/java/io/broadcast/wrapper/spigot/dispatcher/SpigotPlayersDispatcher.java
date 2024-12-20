package io.broadcast.wrapper.spigot.dispatcher;

import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.record.Record;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotPlayersDispatcher implements BroadcastDispatcher<Player, StringAnnouncement> {

    @Override
    public void dispatch(@NotNull Record<Player> record, @NotNull StringAnnouncement announcement) {
        Player player = record.getId();
        
        if (player != null) {
            player.sendMessage(announcement.getContent());
        }
    }
}
