package io.broadcast.engine;

import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.event.BroadcastListener;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.scheduler.Scheduler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a pipeline for broadcasting operations, which includes the configuration
 * of message preparation, dispatching, record extraction, and event listening.
 *
 * <p>The {@code BroadcastPipeline} interface provides a modular way to manage and
 * execute broadcast-related tasks. Implementations of this interface should
 * ensure thread safety and proper lifecycle management for the components added
 * to the pipeline.</p>
 */
public interface BroadcastPipeline<I> {

    /**
     * Sets the {@link AnnouncementExtractor} instance to be used by the pipeline for preparing broadcast messages.
     *
     * @param announcementExtractor The {@link AnnouncementExtractor} instance. Must not be {@code null}.
     * @return The current {@code BroadcastPipeline} instance for method chaining.
     */
    BroadcastPipeline<I> setAnnouncementExtractor(AnnouncementExtractor<?> announcementExtractor);

    /**
     * Sets the {@link BroadcastDispatcher} responsible for dispatching prepared messages.
     *
     * @param dispatcher The {@link BroadcastDispatcher} instance. Must not be {@code null}.
     * @return The current {@code BroadcastPipeline} instance for method chaining.
     */
    BroadcastPipeline<I> setDispatcher(BroadcastDispatcher<I, ?> dispatcher);

    /**
     * Sets the {@link RecordExtractor} responsible for extracting records to be broadcasted.
     *
     * @param recordsExtractor The {@link RecordExtractor} instance. Must not be {@code null}.
     * @return The current {@code BroadcastPipeline} instance for method chaining.
     */
    BroadcastPipeline<I> setRecordExtractor(RecordExtractor<I> recordsExtractor);

    /**
     * Sets the {@link Scheduler} responsible for extracting records to be broadcasted.
     *
     * @param scheduler The {@link Scheduler} instance. Must not be {@code null}.
     * @return The current {@code BroadcastPipeline} instance for method chaining.
     */
    BroadcastPipeline<I> setScheduler(Scheduler scheduler);

    /**
     * Adds a {@link BroadcastListener} to the pipeline to listen for broadcast events.
     *
     * @param listener The {@link BroadcastListener} to be added. Must not be {@code null}.
     * @return The current {@code BroadcastPipeline} instance for method chaining.
     */
    BroadcastPipeline<I> addListener(BroadcastListener listener);

    /**
     * Retrieves the {@link AnnouncementExtractor} configured in the pipeline.
     *
     * @return The {@link AnnouncementExtractor} instance. May return {@code null} if not set.
     */
    AnnouncementExtractor<?> getAnnouncementExtractor();

    /**
     * Retrieves the {@link BroadcastDispatcher} configured in the pipeline.
     *
     * @return The {@link BroadcastDispatcher} instance. May return {@code null} if not set.
     */
    BroadcastDispatcher<I, ?> getDispatcher();

    /**
     * Retrieves the {@link RecordExtractor} configured in the pipeline.
     *
     * @return The {@link RecordExtractor} instance. May return {@code null} if not set.
     */
    RecordExtractor<I> getRecordExtractor();

    /**
     * Retrieves the {@link Scheduler} configured in the pipeline.
     *
     * @return The {@link Scheduler} instance. May return {@code null} if not set.
     */
    Scheduler getScheduler();

    /**
     * Retrieves all {@link BroadcastListener} instances registered in the pipeline.
     *
     * @return An {@link Iterable} of {@link BroadcastListener} instances. Never {@code null}.
     */
    Iterable<BroadcastListener> getListeners();

    /**
     * Creates a new instance of the default {@code BroadcastPipeline} implementation.
     *
     * @return A new {@link BroadcastPipeline} instance.
     */
    @Contract("_ -> new")
    static <I> @NotNull BroadcastPipeline<I> createPipeline(@NotNull Class<I> recordIdType) {
        return new LinkedBroadcastPipeline<>();
    }

    /**
     * Creates a new instance of the default {@code BroadcastPipeline} implementation.
     *
     * @return A new {@link BroadcastPipeline} instance.
     */
    @Contract(" -> new")
    static <I> @NotNull BroadcastPipeline<I> createPipeline() {
        return new LinkedBroadcastPipeline<>();
    }
}
