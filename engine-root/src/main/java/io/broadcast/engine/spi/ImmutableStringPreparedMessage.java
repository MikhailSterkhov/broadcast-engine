package io.broadcast.engine.spi;

import io.broadcast.engine.TextMessage;
import lombok.RequiredArgsConstructor;
import io.broadcast.engine.Announcement;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ImmutableStringPreparedMessage<I> implements PreparedMessage<I> {

    private final TextMessage textMessage;

    @Override
    public @NotNull Announcement<I> createAnnouncement(@NotNull Record<I> record) {
        return new Announcement<>(record, textMessage);
    }
}
