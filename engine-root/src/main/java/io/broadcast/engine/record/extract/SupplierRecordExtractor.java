package io.broadcast.engine.record.extract;

import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class SupplierRecordExtractor<I> implements RecordExtractor<I> {

    private final Supplier<Iterable<Record<I>>> recordIterableSupplier;

    @Override
    public void extract(@NotNull RecordObserver<I> recordObserver) {
        recordIterableSupplier.get().forEach(recordObserver::observe);
    }
}
