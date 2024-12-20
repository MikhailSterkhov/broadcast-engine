package io.broadcast.wrapper.spigot.dispatcher;

import io.broadcast.engine.announcement.StringAnnouncement;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.wrapper.spigot.extractor.announcement.PermissibleStringAnnouncement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface SpigotDispatcher {

    @Contract(value = " -> new", pure = true)
    static @NotNull BroadcastDispatcher<Player, PermissibleStringAnnouncement> permissibleOnlinePlayers() {
        return new PermissibleSpigotPlayersDispatcher();
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull BroadcastDispatcher<String, PermissibleStringAnnouncement> permissibleOnlinePlayersByName() {
        return new PermissibleSpigotPlayersByNameDispatcher();
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull BroadcastDispatcher<UUID, PermissibleStringAnnouncement> permissibleOnlinePlayersByUniqueId() {
        return new PermissibleSpigotPlayersByUuidDispatcher();
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull BroadcastDispatcher<Player, StringAnnouncement> onlinePlayers() {
        return new SpigotPlayersDispatcher();
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull BroadcastDispatcher<String, StringAnnouncement> onlinePlayersByName() {
        return new SpigotPlayersByNameDispatcher();
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull BroadcastDispatcher<UUID, StringAnnouncement> onlinePlayersByUniqueId() {
        return new SpigotPlayersByUuidDispatcher();
    }
}
