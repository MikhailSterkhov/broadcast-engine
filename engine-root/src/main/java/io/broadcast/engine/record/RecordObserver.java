package io.broadcast.engine.record;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RecordObserver<I, T> {

    void observe(@NotNull Record<I, T> record);
}
