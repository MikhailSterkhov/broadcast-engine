package io.broadcast.engine.dispatch;

import io.broadcast.engine.Announcement;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BroadcastDispatcher<I, T> {

    void dispatch(@NotNull Announcement<I, T> announcement);
}
