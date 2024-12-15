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

    @Contract("_, _ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> immutable(String subject, String text) {
        return new ImmutableStringPreparedMessage<>(subject, text);
    }

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> immutable(String text) {
        return new ImmutableStringPreparedMessage<>(null, text);
    }

    @Contract("_, _ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> func(Function<Record<I, T>, String> subjectFunction,
                                                      Function<Record<I, T>, String> textFunction) {
        return new ApplicablePreparedMessage<>(subjectFunction, textFunction);
    }

    @Contract("_ -> new")
    static <I, T> @NotNull PreparedMessage<I, T> func(Function<Record<I, T>, String> textFunction) {
        return new ApplicablePreparedMessage<>(null, textFunction);
    }
}
