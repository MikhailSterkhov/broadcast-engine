package io.broadcast.engine.record.extract;

import io.broadcast.engine.record.ChunkyRecordSelector;
import io.broadcast.engine.record.Record;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

@UtilityClass
public class Extractors {

    public <I, T> RecordExtractor<I, T> immutable(Iterable<Record<I, T>> recordIterable) {
        return new ImmutableRecordExtractor<>(recordIterable);
    }

    public <I, T> RecordExtractor<I, T> supplier(Supplier<Iterable<Record<I, T>>> recordIterableSupplier) {
        return new SupplierRecordExtractor<>(recordIterableSupplier);
    }

    public <I, T> RecordExtractor<I, T> chunkyParallel(ChunkyRecordSelector<I, T> recordSelector) {
        return new ChunkyParallelRecordExtractor<>(recordSelector);
    }

    public <I, T> RecordExtractor<I, T> wrapAsync(RecordExtractor<I, T> extractor) {
        return WrappedAsynchronousRecordExtractor.wrap(extractor);
    }

    public <I, T> RecordExtractor<I, T> immutableAsync(Iterable<Record<I, T>> recordIterable) {
        return wrapAsync(immutable(recordIterable));
    }

    public <I, T> RecordExtractor<I, T> supplierAsync(Supplier<Iterable<Record<I, T>>> recordIterableSupplier) {
        return wrapAsync(supplier(recordIterableSupplier));
    }

    public <I, T> RecordExtractor<I, T> chunkyParallelAsync(ChunkyRecordSelector<I, T> recordSelector) {
        return wrapAsync(chunkyParallel(recordSelector));
    }
}
