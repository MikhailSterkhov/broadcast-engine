package io.broadcast.engine.dispatch;

import io.broadcast.engine.announcement.Announcement;
import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BroadcastDispatcher<I, A extends Announcement> {

    void dispatch(@NotNull Record<I> record, @NotNull A announcement);
}
