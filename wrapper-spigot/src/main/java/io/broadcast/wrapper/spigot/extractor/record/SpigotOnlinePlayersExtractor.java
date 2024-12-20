package io.broadcast.wrapper.spigot.extractor.record;

import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import io.broadcast.engine.record.extract.RecordExtractor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotOnlinePlayersExtractor implements RecordExtractor<Player> {

    @Override
    public void extract(@NotNull RecordObserver<Player> recordObserver) {
        Bukkit.getOnlinePlayers().forEach(player -> recordObserver.observe(new Record<>(player)));
    }
}
