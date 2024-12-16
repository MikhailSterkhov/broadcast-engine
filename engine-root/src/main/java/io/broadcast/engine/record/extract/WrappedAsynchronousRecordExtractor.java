package io.broadcast.engine.record.extract;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.record.RecordObserver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class WrappedAsynchronousRecordExtractor<I> extends AbstractAsynchronousRecordExtractor<I> {

    @Contract("_ -> new")
    public static <I> @NotNull WrappedAsynchronousRecordExtractor<I> wrap(RecordExtractor<I> internal) {
        return new WrappedAsynchronousRecordExtractor<>(internal);
    }

    private final RecordExtractor<I> internal;

    @Override
    public void extractAsync(@NotNull RecordObserver<I> recordObserver) {
        internal.extract(recordObserver);
    }
}
