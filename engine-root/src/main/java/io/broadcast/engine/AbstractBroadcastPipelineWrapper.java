package io.broadcast.engine;

import io.broadcast.engine.announcement.Announcement;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.event.BroadcastListener;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.scheduler.Scheduler;

@SuppressWarnings("unchecked")
public class AbstractBroadcastPipelineWrapper<I, A extends Announcement, T extends AbstractBroadcastPipelineWrapper<I, A, T>>
        implements BroadcastPipeline<I, A> {
    
    protected final BroadcastPipeline<I, A> internalPipe = BroadcastPipeline.createPipeline();

    @Override
    public T setAnnouncementExtractor(AnnouncementExtractor<A> announcementExtractor) {
        internalPipe.setAnnouncementExtractor(announcementExtractor);
        return (T) this;
    }

    @Override
    public T setDispatcher(BroadcastDispatcher<I, A> dispatcher) {
        internalPipe.setDispatcher(dispatcher);
        return (T) this;
    }

    @Override
    public T setRecordExtractor(RecordExtractor<I> recordsExtractor) {
        internalPipe.setRecordExtractor(recordsExtractor);
        return (T) this;
    }

    @Override
    public T setScheduler(Scheduler scheduler) {
        internalPipe.setScheduler(scheduler);
        return (T) this;
    }

    @Override
    public T addListener(BroadcastListener listener) {
        internalPipe.addListener(listener);
        return (T) this;
    }

    @Override
    public AnnouncementExtractor<A> getAnnouncementExtractor() {
        return internalPipe.getAnnouncementExtractor();
    }

    @Override
    public BroadcastDispatcher<I, A> getDispatcher() {
        return internalPipe.getDispatcher();
    }

    @Override
    public RecordExtractor<I> getRecordExtractor() {
        return internalPipe.getRecordExtractor();
    }

    @Override
    public Scheduler getScheduler() {
        return internalPipe.getScheduler();
    }

    @Override
    public Iterable<BroadcastListener> getListeners() {
        return internalPipe.getListeners();
    }
}
