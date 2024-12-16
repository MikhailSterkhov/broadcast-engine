package io.broadcast.engine.record;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RecordToStringSerializer<I> {

    @Nullable
    String serializeToString(@NotNull Record<I> record);

    @Contract(pure = true)
    static <I> @NotNull RecordToStringSerializer<I> single(@NotNull String text) {
        return ((record) -> text);
    }
}
