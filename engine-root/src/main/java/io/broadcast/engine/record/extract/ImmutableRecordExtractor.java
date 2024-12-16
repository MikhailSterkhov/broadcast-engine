package io.broadcast.engine.record.extract;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ImmutableRecordExtractor<I> implements RecordExtractor<I> {

    private final Iterable<Record<I>> recordIterable;

    @Override
    public void extract(@NotNull RecordObserver<I> recordObserver) {
        recordIterable.forEach(recordObserver::observe);
    }
}
