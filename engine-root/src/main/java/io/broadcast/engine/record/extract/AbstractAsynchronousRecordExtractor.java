package io.broadcast.engine.record.extract;

import lombok.RequiredArgsConstructor;
import io.broadcast.engine.record.RecordObserver;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public abstract class AbstractAsynchronousRecordExtractor<I, T> implements RecordExtractor<I, T> {

    private static ExecutorService THREAD_POOL;

    public static void runAsync(Runnable command) {
        if (THREAD_POOL == null) {
            THREAD_POOL = Executors.newCachedThreadPool();
        }
        CompletableFuture.runAsync(command, THREAD_POOL);
    }

    public abstract void extractAsync(@NotNull RecordObserver<I, T> recordObserver);

    @Override
    public final void extract(@NotNull RecordObserver<I, T> recordObserver) {
        runAsync(() -> extractAsync(recordObserver));
    }
}
