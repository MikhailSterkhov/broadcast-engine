package io.broadcast.engine.record.extract;

import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class SupplierRecordExtractor<I, T> implements RecordExtractor<I, T> {

    private final Supplier<Iterable<Record<I, T>>> recordIterableSupplier;

    @Override
    public void extract(@NotNull RecordObserver<I, T> recordObserver) {
        recordIterableSupplier.get().forEach(recordObserver::observe);
    }
}
