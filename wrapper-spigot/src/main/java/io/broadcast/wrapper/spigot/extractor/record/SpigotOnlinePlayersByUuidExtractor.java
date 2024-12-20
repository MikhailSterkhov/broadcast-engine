package io.broadcast.wrapper.spigot.extractor.record;

import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import io.broadcast.engine.record.extract.RecordExtractor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SpigotOnlinePlayersByUuidExtractor implements RecordExtractor<UUID> {

    @Override
    public void extract(@NotNull RecordObserver<UUID> recordObserver) {
        Bukkit.getOnlinePlayers().forEach(player -> recordObserver.observe(new Record<>(player.getUniqueId())));
    }
}
