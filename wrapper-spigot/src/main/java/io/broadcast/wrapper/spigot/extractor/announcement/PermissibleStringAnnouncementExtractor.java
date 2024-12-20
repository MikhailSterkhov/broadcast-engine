package io.broadcast.wrapper.spigot.extractor.announcement;

import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.record.Record;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class PermissibleStringAnnouncementExtractor implements AnnouncementExtractor<PermissibleStringAnnouncement> {

    private final String permission;
    private final String message;

    @Override
    public @Nullable <I> PermissibleStringAnnouncement extractAnnouncement(Record<I> record) {
        return message != null ? new PermissibleStringAnnouncement(permission, message) : null;
    }
}
