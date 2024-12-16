package io.broadcast.engine.scheduler;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ThreadSchedulerExecutor implements Scheduler {

    private final int corePoolSize;
    private ScheduledExecutorService executorService;

    @Override
    public void schedule(@NotNull Duration duration, @NotNull Runnable runnable) {
        if (executorService == null) {
            executorService = Executors.newScheduledThreadPool(corePoolSize);
        }
        executorService.scheduleWithFixedDelay(runnable,
                duration.toNanos(),
                duration.toNanos(), TimeUnit.NANOSECONDS);
    }
}
