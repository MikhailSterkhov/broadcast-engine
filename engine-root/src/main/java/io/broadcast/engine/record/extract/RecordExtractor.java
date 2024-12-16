package io.broadcast.engine.record.extract;

import io.broadcast.engine.record.RecordObserver;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interface for extracting records using an observer pattern.
 *
 * <p>The {@code RecordExtractor} is responsible for processing and supplying records
 * to a {@link RecordObserver} implementation. This enables the handling of records
 * in a decoupled manner, facilitating various use cases like data streaming or batch processing.</p>
 *
 * @param <I> The type of the identifier used in the records.
 * @param <T> The type of the entity associated with the records.
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
}
