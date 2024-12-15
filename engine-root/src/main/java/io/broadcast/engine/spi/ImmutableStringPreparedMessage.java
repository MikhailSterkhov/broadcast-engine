package io.broadcast.engine.spi;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.Announcement;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ImmutableStringPreparedMessage<I, T> implements PreparedMessage<I, T> {

    private final String text;

    @Override
    public @NotNull Announcement<I, T> createAnnouncement(@NotNull Record<I, T> record) {
        return new Announcement<>(record, text);
    }
}
