package io.broadcast.engine.record.extract;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.record.ChunkyRecordSelector;
import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordObserver;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class ChunkyParallelRecordExtractor<I, T> implements RecordExtractor<I, T> {

    private static ExecutorService PARALLEL_EXECUTOR;
    private final ChunkyRecordSelector<I, T> recordSelector;

    @Override
    public void extract(@NotNull RecordObserver<I, T> recordObserver) {
        if (PARALLEL_EXECUTOR == null) {
            PARALLEL_EXECUTOR = Executors.newWorkStealingPool(4);
        }

        long totalSize = recordSelector.totalSize();
        long chunksSize = recordSelector.chunkSize();

        if (totalSize <= 0 || chunksSize <= 0) {
            return;
        }

        long indexesCount = totalSize / Math.min(chunksSize, totalSize);
        for (long index = 0; index < indexesCount; index++) {
            long finalIndex = index;

            PARALLEL_EXECUTOR.submit(() -> {

                Iterable<Record<I, T>> chunk = recordSelector.select(finalIndex);
                chunk.forEach(recordObserver::observe);
            });
        }
    }
}
