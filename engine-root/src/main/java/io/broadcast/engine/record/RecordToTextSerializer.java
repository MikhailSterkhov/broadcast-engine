package io.broadcast.engine.record;

import io.broadcast.engine.TextMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RecordToTextSerializer<I> {

    @Nullable
    TextMessage serializeToText(@NotNull Record<I> record);

    @Contract(pure = true)
    static <I> @NotNull RecordToTextSerializer<I> single(@NotNull TextMessage textMessage) {
        return ((record) -> textMessage);
    }
}
