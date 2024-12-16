package io.broadcast.engine;

import io.broadcast.engine.announcement.Announcement;
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
public class LinkedBroadcastPipeline<I, A extends Announcement> implements BroadcastPipeline<I, A> {

    private AnnouncementExtractor<A> announcementExtractor;
    private BroadcastDispatcher<I, A> dispatcher = new STDOUTBroadcastDispatcher<>();
    private RecordExtractor<I> recordExtractor;
    private Scheduler scheduler = Scheduler.defaultScheduler();

    private final Set<BroadcastListener> listeners = new HashSet<>();

    @Override
    public BroadcastPipeline<I, A> setAnnouncementExtractor(AnnouncementExtractor<A> announcementExtractor) {
        this.announcementExtractor = announcementExtractor;
        return this;
    }

    @Override
    public BroadcastPipeline<I, A> setDispatcher(BroadcastDispatcher<I, A> dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    @Override
    public BroadcastPipeline<I, A> setRecordExtractor(RecordExtractor<I> recordsExtractor) {
        this.recordExtractor = recordsExtractor;
        return this;
    }

    @Override
    public BroadcastPipeline<I, A> setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    @Override
    public BroadcastPipeline<I, A> addListener(BroadcastListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public Iterable<BroadcastListener> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }
}
