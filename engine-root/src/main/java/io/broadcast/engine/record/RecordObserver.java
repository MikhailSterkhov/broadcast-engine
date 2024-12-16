package io.broadcast.engine.record;

import org.jetbrains.annotations.NotNull;

/**
 * Functional interface for observing {@link Record} objects during processing.
 *
 * <p>The {@code RecordObserver} interface defines a single method {@link #observe(Record)},
 * which is invoked whenever a {@link Record} instance needs to be observed or processed.
 * This interface can be implemented to provide custom logic for handling records
 * during their lifecycle in the broadcast system or other processing workflows.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * RecordObserver<Integer, String> observer = record -> {
 *     System.out.println("Observed record: " + record);
 * };
 * observer.observe(new Record<>(1, "Sample Entity"));
 * }
 * </pre>
 *
 * @param <I> The type of the identifier in the observed record.
 * @param <T> The type of the entity in the observed record.
 */
@FunctionalInterface
public interface RecordObserver<I> {

    /**
     * Observes the given {@link Record}.
     *
     * @param record The record to be observed. Must not be {@code null}.
     * @throws NullPointerException if {@code record} is {@code null}.
     */
    void observe(@NotNull Record<I> record);
}
