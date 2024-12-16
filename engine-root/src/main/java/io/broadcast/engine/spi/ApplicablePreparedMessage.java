package io.broadcast.engine.spi;

import io.broadcast.engine.record.RecordToTextSerializer;
import lombok.RequiredArgsConstructor;
import io.broadcast.engine.Announcement;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
public class ApplicablePreparedMessage<I, T> implements PreparedMessage<I, T> {

    private final RecordToTextSerializer<I, T> serializer;

    @Override
    public @NotNull Announcement<I, T> createAnnouncement(@NotNull Record<I, T> record) {
        return new Announcement<>(record,
                Optional.ofNullable(serializer).map(serializer -> serializer.serializeToText(record))
                        .orElse(null));
    }
}
