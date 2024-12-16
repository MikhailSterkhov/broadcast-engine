package io.broadcast.engine.record.extract;

import io.broadcast.engine.record.RecordObserver;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface RecordExtractor<I, T> {

    void extract(@NotNull RecordObserver<I, T> recordObserver);
}
