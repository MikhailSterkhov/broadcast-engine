package io.broadcast.engine.announcement;

import org.jetbrains.annotations.Nullable;

public interface AnnouncementExtractor<A extends Announcement> {

    @Nullable
    A extractAnnouncement();
}
