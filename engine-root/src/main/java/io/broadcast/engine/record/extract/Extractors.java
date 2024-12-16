package io.broadcast.engine.record.extract;

import io.broadcast.engine.record.ChunkyRecordSelector;
import io.broadcast.engine.record.Record;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

/**
 * Utility class providing factory methods for creating various types of {@link RecordExtractor} instances.
 *
 * <p>The {@code Extractors} class offers different implementations of record extractors, supporting
 * immutable data sets, suppliers, chunk-based parallel processing, and asynchronous wrapping.
 * This class cannot be instantiated and all methods are static.</p>
 */
@UtilityClass
public class Extractors {

    /**
     * Creates an immutable {@link RecordExtractor} that iterates over the given {@link Iterable} of records.
     *
     * @param recordIterable The iterable containing records to be extracted. Must not be {@code null}.
     * @param <I>            The type of the record identifier.
     * @return A {@link RecordExtractor} that provides immutable access to the given records.
     * @throws NullPointerException if {@code recordIterable} is {@code null}.
     */
    public <I> RecordExtractor<I> constant(Iterable<Record<I>> recordIterable) {
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
    public <I> RecordExtractor<I> mutable(Supplier<Iterable<Record<I>>> recordIterableSupplier) {
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
    public <I> RecordExtractor<I> chunkyParallel(ChunkyRecordSelector<I> recordSelector) {
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
    public <I> RecordExtractor<I> wrapAsync(RecordExtractor<I> extractor) {
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
    public <I> RecordExtractor<I> immutableAsync(Iterable<Record<I>> recordIterable) {
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
    public <I> RecordExtractor<I> supplierAsync(Supplier<Iterable<Record<I>>> recordIterableSupplier) {
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
    public <I> RecordExtractor<I> chunkyParallelAsync(ChunkyRecordSelector<I> recordSelector) {
        return wrapAsync(chunkyParallel(recordSelector));
    }
}

