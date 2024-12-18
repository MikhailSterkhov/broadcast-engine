package io.broadcast.engine.record.extract;

import io.broadcast.engine.record.ChunkyRecordSelector;
import io.broadcast.engine.record.RecordObserver;
import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Functional interface for extracting records using an observer pattern.
 *
 * <p>The {@code RecordExtractor} is responsible for processing and supplying records
 * to a {@link RecordObserver} implementation. This enables the handling of records
 * in a decoupled manner, facilitating various use cases like data streaming or batch processing.</p>
 *
 * @param <I> The type of the identifier used in the records.
 */
@FunctionalInterface
public interface RecordExtractor<I> {

    /**
     * Extracts records and passes them to the specified {@link RecordObserver} for processing.
     *
     * @param recordObserver The observer that will process the extracted records. Must not be {@code null}.
     * @throws NullPointerException if {@code recordObserver} is {@code null}.
     */
    void extract(@NotNull RecordObserver<I> recordObserver);

    /**
     * Creates an immutable {@link RecordExtractor} that iterates over the given {@link Iterable} of records.
     *
     * @param recordIterable The iterable containing records to be extracted. Must not be {@code null}.
     * @param <I>            The type of the record identifier.
     * @return A {@link RecordExtractor} that provides immutable access to the given records.
     * @throws NullPointerException if {@code recordIterable} is {@code null}.
     */
    static <I> RecordExtractor<I> constant(Iterable<Record<I>> recordIterable) {
        return new ImmutableRecordExtractor<>(recordIterable);
    }

    /**
     * Creates a {@link RecordExtractor} that retrieves records from a {@link Supplier} of {@link Iterable}.
     *
     * @param recordIterableSupplier A supplier providing an iterable of records to be extracted. Must not be {@code null}.
     * @param <I>                    The type of the record identifier.
     * @return A {@link RecordExtractor} that fetches records from the supplier on demand.
     * @throws NullPointerException if {@code recordIterableSupplier} is {@code null}.
     */
    static <I> RecordExtractor<I> mutable(Supplier<Iterable<Record<I>>> recordIterableSupplier) {
        return new SupplierRecordExtractor<>(recordIterableSupplier);
    }

    /**
     * Creates a {@link RecordExtractor} that processes records in parallel using the given {@link ChunkyRecordSelector}.
     *
     * @param recordSelector The selector for chunk-based record processing. Must not be {@code null}.
     * @param <I>            The type of the record identifier.
     * @return A {@link RecordExtractor} that performs parallel chunk-based record extraction.
     * @throws NullPointerException if {@code recordSelector} is {@code null}.
     */
    static <I> RecordExtractor<I> chunkyParallel(ChunkyRecordSelector<I> recordSelector) {
        return new ChunkyParallelRecordExtractor<>(recordSelector);
    }

    /**
     * Wraps an existing {@link RecordExtractor} to perform asynchronous record extraction.
     *
     * @param extractor The original record extractor to be wrapped. Must not be {@code null}.
     * @param <I>       The type of the record identifier.
     * @return A new {@link RecordExtractor} that extracts records asynchronously.
     * @throws NullPointerException if {@code extractor} is {@code null}.
     */
    static <I> RecordExtractor<I> wrapAsync(RecordExtractor<I> extractor) {
        return WrappedAsynchronousRecordExtractor.wrap(extractor);
    }

    /**
     * Creates an asynchronous {@link RecordExtractor} for an immutable {@link Iterable} of records.
     *
     * @param recordIterable The iterable containing records to be extracted. Must not be {@code null}.
     * @param <I>            The type of the record identifier.
     * @return A {@link RecordExtractor} that performs asynchronous extraction on immutable records.
     * @throws NullPointerException if {@code recordIterable} is {@code null}.
     */
    static <I> RecordExtractor<I> immutableAsync(Iterable<Record<I>> recordIterable) {
        return wrapAsync(constant(recordIterable));
    }

    /**
     * Creates an asynchronous {@link RecordExtractor} for records supplied by a {@link Supplier}.
     *
     * @param recordIterableSupplier A supplier providing an iterable of records to be extracted. Must not be {@code null}.
     * @param <I>                    The type of the record identifier.
     * @return A {@link RecordExtractor} that performs asynchronous extraction on supplied records.
     * @throws NullPointerException if {@code recordIterableSupplier} is {@code null}.
     */
    static <I> RecordExtractor<I> supplierAsync(Supplier<Iterable<Record<I>>> recordIterableSupplier) {
        return wrapAsync(mutable(recordIterableSupplier));
    }

    /**
     * Creates an asynchronous {@link RecordExtractor} for records processed in parallel using a {@link ChunkyRecordSelector}.
     *
     * @param recordSelector The selector for chunk-based record processing. Must not be {@code null}.
     * @param <I>            The type of the record identifier.
     * @return A {@link RecordExtractor} that performs asynchronous parallel chunk-based record extraction.
     * @throws NullPointerException if {@code recordSelector} is {@code null}.
     */
    static <I> RecordExtractor<I> chunkyParallelAsync(ChunkyRecordSelector<I> recordSelector) {
        return wrapAsync(chunkyParallel(recordSelector));
    }
}
