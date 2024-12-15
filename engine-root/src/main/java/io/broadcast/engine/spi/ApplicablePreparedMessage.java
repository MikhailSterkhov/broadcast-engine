package io.broadcast.engine.spi;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.Announcement;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class ApplicablePreparedMessage<I, T> implements PreparedMessage<I, T> {

    private final Function<Record<I, T>, String> recordToTextFunction;

    @Override
    public @NotNull Announcement<I, T> createAnnouncement(@NotNull Record<I, T> record) {
        return new Announcement<>(record,
                Optional.ofNullable(recordToTextFunction).map(func -> func.apply(record))
                        .orElse(null));
    }
}
