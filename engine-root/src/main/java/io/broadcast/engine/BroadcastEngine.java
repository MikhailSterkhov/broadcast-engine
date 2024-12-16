package io.broadcast.engine;

import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.event.context.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import io.broadcast.engine.event.BroadcastListener;
import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.scheduler.Scheduler;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

/**
 * BroadcastEngine is the core class responsible for managing the broadcast process.
 * It orchestrates the extraction of records, preparation of messages, and their dispatching.
 * Additionally, it provides event-driven mechanisms to notify listeners about key stages
 * of the broadcast lifecycle and supports scheduling periodic broadcasts.
 *
 * <p>This class is designed to work with a {@link BroadcastPipeline}, which encapsulates
 * the required components for broadcasting, such as a {@link RecordExtractor},
 * {@link PreparedMessage}, and {@link BroadcastDispatcher}.</p>
 *
 * <p>Listeners can subscribe to various events by implementing the {@link BroadcastListener}
 * interface, enabling hooks for specific broadcast lifecycle events.</p>
 *
 * <p>To schedule broadcasts periodically, a {@link Scheduler} can be provided or the default
 * scheduler will be used.</p>
 *
 * <p>Exceptions occurring during the broadcast process are forwarded to listeners or
 * wrapped in a {@link BroadcastEngineException} if no listeners are available.</p>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@RequiredArgsConstructor
public final class BroadcastEngine {

    private final BroadcastPipeline pipeline;
    @Setter
    private Scheduler scheduler;

    /**
     * Initiates the broadcasting process, coordinating record extraction, message preparation,
     * and dispatching. This method also fires events at various stages of the broadcast lifecycle.
     */
    private void broadcast0() {
        fireStartEvent();

        RecordExtractor recordExtractor = pipeline.getRecordExtractor();
        PreparedMessage preparedMessage = pipeline.getPreparedMessage();

        if (recordExtractor == null) {
            throw new BroadcastEngineException("records-extractor is not initialized");
        }
        if (preparedMessage == null) {
            throw new BroadcastEngineException("prepared-message is not initialized");
        }

        recordExtractor.extract(record -> {
            fireRecordExtractedEvent(record);

            Announcement announcement = preparedMessage.createAnnouncement(record);
            firePreparedMessageEvent(announcement);

            if (announcement.hasMessage()) {
                dispatch0(announcement);
            }
        });

        fireEndEvent();
    }

    /**
     * Executes the broadcasting process immediately.
     * Wraps the core broadcasting logic in an exception-handling mechanism to handle errors gracefully.
     */
    public void broadcastNow() {
        sneakyThrows(this::broadcast0);
    }

    /**
     * Schedules the broadcasting process to run at regular intervals.
     *
     * @param duration The interval between each scheduled broadcast.
     *                 Must not be null.
     * @throws IllegalArgumentException if {@code duration} is null.
     */
    public void scheduleBroadcastEverytime(@NotNull Duration duration) {
        if (scheduler == null) {
            scheduler = Scheduler.defaultScheduler();
        }
        scheduler.schedule(duration, this::broadcastNow);
        fireScheduleEvent(duration);
    }

    /**
     * Fires an event signaling the start of the broadcast process.
     * Creates a {@link BroadcastStartEventContext} and notifies listeners.
     */
    private void fireStartEvent() {
        BroadcastStartEventContext eventContext = new BroadcastStartEventContext(
                Instant.now()
        );
        listenEvent(broadcastListener -> broadcastListener.broadcastStart(eventContext));
    }

    /**
     * Fires an event signaling the end of the broadcast process.
     * Creates a {@link BroadcastEndEventContext} and notifies listeners.
     */
    private void fireEndEvent() {
        BroadcastEndEventContext eventContext = new BroadcastEndEventContext(
                Instant.now()
        );
        listenEvent(broadcastListener -> broadcastListener.broadcastEnd(eventContext));
    }

    /**
     * Fires an event when a broadcast is scheduled.
     * Creates a {@link BroadcastScheduleEventContext} containing the scheduling details
     * and notifies listeners.
     *
     * @param duration The interval between scheduled broadcasts.
     */
    private void fireScheduleEvent(Duration duration) {
        BroadcastScheduleEventContext eventContext = new BroadcastScheduleEventContext(
                Instant.now(),
                duration
        );
        listenEvent(broadcastListener -> broadcastListener.broadcastSchedule(eventContext));
    }

    /**
     * Fires an event after an announcement has been dispatched.
     * Creates a {@link BroadcastDispatchEventContext} containing the dispatch details
     * and notifies listeners.
     *
     * @param announcement The announcement that was dispatched.
     */
    private void fireDispatchEvent(Announcement announcement) {
        BroadcastDispatchEventContext eventContext = new BroadcastDispatchEventContext(
                announcement.getRecord(),
                Instant.now(),
                announcement.getTextMessage()
        );
        listenEvent(broadcastListener -> broadcastListener.broadcastDispatch(eventContext));
    }

    /**
     * Fires an event when a record has been successfully extracted.
     * Creates a {@link RecordExtractedEventContext} and notifies listeners.
     *
     * @param record The extracted record.
     */
    private void fireRecordExtractedEvent(Record record) {
        RecordExtractedEventContext eventContext = new RecordExtractedEventContext(
                record, Instant.now()
        );
        listenEvent(broadcastListener -> broadcastListener.recordExtracted(eventContext));
    }

    /**
     * Fires an event when a message has been prepared from an extracted record.
     * Creates a {@link PreparedMessageEventContext} and notifies listeners.
     *
     * @param announcement The prepared announcement.
     */
    private void firePreparedMessageEvent(Announcement announcement) {
        PreparedMessageEventContext eventContext = new PreparedMessageEventContext(
                announcement.getRecord(),
                Instant.now(),
                announcement.getTextMessage(),
                announcement.hasMessage()
        );
        listenEvent(broadcastListener -> broadcastListener.preparedMessage(eventContext));
    }

    /**
     * Dispatches an announcement using the dispatcher defined in the pipeline.
     * Fires a dispatch event upon successful completion.
     *
     * @param announcement The announcement to dispatch.
     */
    private void dispatch0(Announcement announcement) {
        BroadcastDispatcher dispatcher = pipeline.getDispatcher();
        dispatcher.dispatch(announcement);

        fireDispatchEvent(announcement);
    }

    /**
     * Utility method for notifying all registered listeners with a given consumer.
     * Wraps any exceptions that occur during listener notification.
     *
     * @param listenerConsumer The consumer to apply to each listener.
     */
    private void listenEvent(Consumer<BroadcastListener> listenerConsumer) {
        sneakyThrows(() -> {
            pipeline.getListeners().forEach(listenerConsumer);
        });
    }

    /**
     * Executes a block of code that may throw exceptions. If an exception is thrown,
     * it is propagated to listeners or wrapped in a {@link BroadcastEngineException}.
     *
     * @param causeCatcher The block of code to execute.
     */
    private void sneakyThrows(CauseCatcher causeCatcher) {
        try {
            causeCatcher.accept();
        } catch (Throwable throwable) {
            Iterable<BroadcastListener> pipelineListeners = pipeline.getListeners();
            if (!pipelineListeners.iterator().hasNext()) {
                throw new BroadcastEngineException("Internal broadcast engine error.", throwable);
            }
            for (BroadcastListener broadcastListener : pipelineListeners) {
                broadcastListener.throwsException(throwable);
            }
        }
    }

    /**
     * Functional interface for executing code that may throw exceptions.
     */
    @FunctionalInterface
    interface CauseCatcher {
        /**
         * Executes the block of code.
         *
         * @throws Throwable if an error occurs during execution.
         */
        void accept() throws Throwable;
    }
}
