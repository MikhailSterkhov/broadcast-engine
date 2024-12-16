package io.broadcast.engine.dispatch;

import io.broadcast.engine.Announcement;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BroadcastDispatcher<I> {

    void dispatch(@NotNull Announcement<I> announcement);
}
