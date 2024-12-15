package io.broadcast.engine.record.extract;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ImmutableRecordExtractor<I, T> implements RecordExtractor<I, T> {

    private final Iterable<Record<I, T>> recordIterable;

    @Override
    public void extract(@NotNull RecordObserver<I, T> recordObserver) {
        recordIterable.forEach(recordObserver::observe);
    }
}
