package io.broadcast.wrapper.spigot.extractor.announcement;

import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.announcement.StringAnnouncement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SpigotAnnouncementExtractor {

    @Contract("_, _ -> new")
    static @NotNull AnnouncementExtractor<PermissibleStringAnnouncement> permissible(@Nullable String permission, @NotNull String message) {
        return new PermissibleStringAnnouncementExtractor(permission, message);
    }

    @Contract("_ -> new")
    static @NotNull AnnouncementExtractor<PermissibleStringAnnouncement> permissible(@NotNull String message) {
        return new PermissibleStringAnnouncementExtractor(null, message);
    }

    static @NotNull AnnouncementExtractor<StringAnnouncement> text(@NotNull String message) {
        return AnnouncementExtractor.constant(new StringAnnouncement(message));
    }
}
