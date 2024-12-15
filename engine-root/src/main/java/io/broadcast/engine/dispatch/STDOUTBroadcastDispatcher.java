package io.broadcast.engine.dispatch;

import io.broadcast.engine.Announcement;
import org.jetbrains.annotations.NotNull;

public class STDOUTBroadcastDispatcher<I, T> implements BroadcastDispatcher<I, T> {

    @Override
    public void dispatch(@NotNull Announcement<I, T> announcement) {
        System.out.println(announcement.getPreparedText());
    }
}
