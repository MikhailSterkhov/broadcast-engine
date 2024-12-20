package io.broadcast.wrapper.spigot.extractor.record;

import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import io.broadcast.engine.record.extract.RecordExtractor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class SpigotOnlinePlayersByNameExtractor implements RecordExtractor<String> {

    @Override
    public void extract(@NotNull RecordObserver<String> recordObserver) {
        Bukkit.getOnlinePlayers().forEach(player -> recordObserver.observe(new Record<>(player.getName())));
    }
}
