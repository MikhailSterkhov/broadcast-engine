package io.broadcast.engine.record;

public interface ChunkyRecordSelector<I, T> {

    long totalSize();

    long chunkSize();

    Iterable<Record<I, T>> select(long index);
}
