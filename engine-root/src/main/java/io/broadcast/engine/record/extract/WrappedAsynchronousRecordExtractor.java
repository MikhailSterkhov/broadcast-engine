package io.broadcast.engine.record.extract;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.record.RecordObserver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class WrappedAsynchronousRecordExtractor<I, T> extends AbstractAsynchronousRecordExtractor<I, T> {

    @Contract("_ -> new")
    public static <I, T> @NotNull WrappedAsynchronousRecordExtractor<I, T> wrap(RecordExtractor<I, T> internal) {
        return new WrappedAsynchronousRecordExtractor<>(internal);
    }

    private final RecordExtractor<I, T> internal;

    @Override
    public void extractAsync(@NotNull RecordObserver<I, T> recordObserver) {
        internal.extract(recordObserver);
    }
}
