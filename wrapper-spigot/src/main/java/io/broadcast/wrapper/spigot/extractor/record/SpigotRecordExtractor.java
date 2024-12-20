package io.broadcast.wrapper.spigot.extractor.record;

import io.broadcast.engine.record.extract.RecordExtractor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface SpigotRecordExtractor {

    @Contract(value = " -> new", pure = true)
    static @NotNull RecordExtractor<Player> onlinePlayers() {
        return new SpigotOnlinePlayersExtractor();
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull RecordExtractor<String> onlinePlayersByName() {
        return new SpigotOnlinePlayersByNameExtractor();
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull RecordExtractor<UUID> onlinePlayersByUniqueId() {
        return new SpigotOnlinePlayersByUuidExtractor();
    }
}
