package io.broadcast.engine;

import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.dispatch.STDOUTBroadcastDispatcher;
import io.broadcast.engine.scheduler.Scheduler;
import lombok.Getter;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.event.BroadcastListener;
import io.broadcast.engine.record.extract.RecordExtractor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
public class LinkedBroadcastPipeline<I> implements BroadcastPipeline<I> {

    private AnnouncementExtractor<?> announcementExtractor;
    private BroadcastDispatcher<I, ?> dispatcher = new STDOUTBroadcastDispatcher<>();
    private RecordExtractor<I> recordExtractor;
    private Scheduler scheduler = Scheduler.defaultScheduler();

    private final Set<BroadcastListener> listeners = new HashSet<>();

    @Override
    public BroadcastPipeline<I> setAnnouncementExtractor(AnnouncementExtractor<?> announcementExtractor) {
        this.announcementExtractor = announcementExtractor;
        return this;
    }

    @Override
    public BroadcastPipeline<I> setDispatcher(BroadcastDispatcher<I, ?> dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    @Override
    public BroadcastPipeline<I> setRecordExtractor(RecordExtractor<I> recordsExtractor) {
        this.recordExtractor = recordsExtractor;
        return this;
    }

    @Override
    public BroadcastPipeline<I> setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    @Override
    public BroadcastPipeline<I> addListener(BroadcastListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public Iterable<BroadcastListener> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }
}
