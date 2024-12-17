package io.broadcast.engine.announcement.mapping;

import io.broadcast.engine.announcement.Announcement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface AnnouncementMappingFactory<V extends Announcement, T extends Announcement> {

    @Nullable
    V createBy(@NotNull T announcement);

    @Contract(pure = true)
    static <V extends Announcement, T extends Announcement> @NotNull AnnouncementMappingFactory<V, T> constant(@NotNull V value) {
        return announcement -> (value);
    }
}
