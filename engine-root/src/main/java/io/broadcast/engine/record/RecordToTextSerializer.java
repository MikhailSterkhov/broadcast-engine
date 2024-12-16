package io.broadcast.engine.record;

import io.broadcast.engine.TextMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RecordToTextSerializer<I, T> {

    @Nullable
    TextMessage serializeToText(@NotNull Record<I, T> record);

    @Contract(pure = true)
    static <I, T> @NotNull RecordToTextSerializer<I, T> single(@NotNull TextMessage textMessage) {
        return ((record) -> textMessage);
    }
}
