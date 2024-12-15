package io.broadcast.engine;

import io.broadcast.engine.record.Record;
import io.broadcast.engine.spi.ApplicablePreparedMessage;
import io.broadcast.engine.spi.ImmutableStringPreparedMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@FunctionalInterface
public interface PreparedMessage<I, T> {

    @NotNull
    Announcement<I, T> createAnnouncement(@NotNull Record<I, T> record);

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> immutable(String text) {
        return new ImmutableStringPreparedMessage<>(text);
    }

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> func(Function<Record<I, T>, String> function) {
        return new ApplicablePreparedMessage<>(function);
    }
}
