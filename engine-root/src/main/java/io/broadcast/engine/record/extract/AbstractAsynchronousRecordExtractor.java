package io.broadcast.engine.record.extract;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.record.RecordObserver;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public abstract class AbstractAsynchronousRecordExtractor<I, T> implements RecordExtractor<I, T> {
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    public static void runAsync(Runnable command) {
        CompletableFuture.runAsync(command, THREAD_POOL);
    }

    public abstract void extractAsync(@NotNull RecordObserver<I, T> recordObserver);

    @Override
    public final void extract(@NotNull RecordObserver<I, T> recordObserver) {
        runAsync(() -> extractAsync(recordObserver));
    }
}
