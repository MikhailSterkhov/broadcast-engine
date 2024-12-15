package io.broadcast.engine.scheduler;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@FunctionalInterface
public interface Scheduler {

    void schedule(@NotNull Duration duration, @NotNull Runnable runnable);

    @Contract(" -> new")
    static @NotNull Scheduler defaultScheduler() {
        return threadScheduler(Runtime.getRuntime().availableProcessors());
    }

    @Contract(" -> new")
    static @NotNull Scheduler singleThreadScheduler() {
        return threadScheduler(1);
    }

    @Contract("_ -> new")
    static @NotNull Scheduler threadScheduler(int corePoolSize) {
        return new ThreadSchedulerExecutor(corePoolSize);
    }
}
