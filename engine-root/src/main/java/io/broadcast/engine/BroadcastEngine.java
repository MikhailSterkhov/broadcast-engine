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

@SuppressWarnings({"unchecked", "CodeBlock2Expr", "rawtypes"})
@RequiredArgsConstructor
public final class BroadcastEngine {

    private final BroadcastPipeline pipeline;
    @Setter
    private Scheduler scheduler;

    private <I, T> void broadcast0() {
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

            Announcement<I, T> announcement = preparedMessage.createAnnouncement(record);
            firePreparedMessageEvent(announcement);

            if (announcement.isTextPrepared()) {
                dispatch0(announcement);
            }
        });

        fireEndEvent();
    }

    public void broadcastNow() {
        sneakyThrows(this::broadcast0);
    }

    public void scheduleBroadcastsNow(@NotNull Duration duration) {
        if (scheduler == null) {
            scheduler = Scheduler.defaultScheduler();
        }
        scheduler.schedule(duration, this::broadcastNow);
        fireScheduleEvent(duration);
    }

    private void fireStartEvent() {
        BroadcastStartEventContext eventContext = new BroadcastStartEventContext(
                Instant.now()
        );
        listenEvent(broadcastListener -> broadcastListener.broadcastStart(eventContext));
    }

    private void fireEndEvent() {
        BroadcastEndEventContext eventContext = new BroadcastEndEventContext(
                Instant.now()
        );
        listenEvent(broadcastListener -> broadcastListener.broadcastEnd(eventContext));
    }

    private void fireScheduleEvent(Duration duration) {
        BroadcastScheduleEventContext eventContext = new BroadcastScheduleEventContext(
                Instant.now(),
                duration
        );
        listenEvent(broadcastListener -> broadcastListener.broadcastSchedule(eventContext));
    }

    private void fireDispatchEvent(Announcement announcement) {
        BroadcastDispatchEventContext eventContext = new BroadcastDispatchEventContext(
                announcement.getRecord(),
                Instant.now(),
                announcement.getPreparedText()
        );
        listenEvent(broadcastListener -> broadcastListener.broadcastDispatch(eventContext));
    }

    private void fireRecordExtractedEvent(Record record) {
        RecordExtractedEventContext eventContext = new RecordExtractedEventContext(
                record,
                Instant.now()
        );
        listenEvent(broadcastListener -> broadcastListener.recordExtracted(eventContext));
    }

    private void firePreparedMessageEvent(Announcement announcement) {
        PreparedMessageEventContext eventContext = new PreparedMessageEventContext(
                announcement.getRecord(),
                Instant.now(),
                announcement.getPreparedText(),
                announcement.isTextPrepared()
        );
        listenEvent(broadcastListener -> broadcastListener.preparedMessage(eventContext));
    }

    private void dispatch0(Announcement announcement) {
        BroadcastDispatcher dispatcher = pipeline.getDispatcher();
        dispatcher.dispatch(announcement);

        fireDispatchEvent(announcement);
    }

    private void listenEvent(Consumer<BroadcastListener> listenerConsumer) {
        sneakyThrows(() -> {
            pipeline.getListeners().forEach(listenerConsumer);
        });
    }

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

    @FunctionalInterface
    interface CauseCatcher {
        void accept() throws Throwable;
    }
}
