package io.broadcast.engine.dispatch;

import io.broadcast.engine.announcement.Announcement;
import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.NotNull;

public class STDOUTBroadcastDispatcher<I, A extends Announcement> implements BroadcastDispatcher<I, A> {

    @Override
    public void dispatch(@NotNull Record<I> record, @NotNull A announcement) {
        System.out.printf("[Record: %s] %s%n", record.getId(), announcement.beatifyToString());
    }
}
