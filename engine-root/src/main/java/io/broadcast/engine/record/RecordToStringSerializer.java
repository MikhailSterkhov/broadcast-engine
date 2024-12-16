package io.broadcast.engine.record;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RecordToStringSerializer<I, T> {

    @Nullable
    String serializeToString(@NotNull Record<I, T> record);

    @Contract(pure = true)
    static <I, T> @NotNull RecordToStringSerializer<I, T> single(@NotNull String text) {
        return ((record) -> text);
    }
}
