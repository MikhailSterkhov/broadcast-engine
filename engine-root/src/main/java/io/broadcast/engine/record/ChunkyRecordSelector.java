package io.broadcast.engine.record;

/**
 * Interface for selecting records in chunks, providing support for pagination.
 *
 * <p>The {@code ChunkyRecordSelector} is designed to handle large datasets by allowing
 * the selection of {@link Record} objects in chunks based on a specified index.
 * It also provides methods to determine the total size of the dataset and the size of each chunk.</p>
 *
 * @param <I> The type of the identifier used in the records.
 * @param <T> The type of the entity associated with the records.
 */
public interface ChunkyRecordSelector<I> {

    /**
     * Returns the total number of records available.
     *
     * @return The total size of the dataset.
     */
    long totalSize();

    /**
     * Returns the size of each chunk.
     *
     * @return The number of records in each chunk.
     */
    long chunkSize();

    /**
     * Selects a chunk of records based on the given index.
     *
     * @param index The index of the chunk to retrieve. Must be non-negative and less than the total number of chunks.
     * @return An {@link Iterable} containing the records in the specified chunk.
     * @throws IllegalArgumentException if the {@code index} is negative or exceeds the total number of chunks.
     */
    Iterable<Record<I>> select(long index);
}
