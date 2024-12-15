package io.broadcast.engine.spi;

import io.broadcast.engine.dispatch.STDOUTBroadcastDispatcher;
import lombok.Getter;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.event.BroadcastListener;
import io.broadcast.engine.record.extract.RecordExtractor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("rawtypes")
@Getter
public class LinkedBroadcastPipeline implements BroadcastPipeline {

    private PreparedMessage preparedMessage;
    private BroadcastDispatcher dispatcher = new STDOUTBroadcastDispatcher();
    private RecordExtractor recordExtractor;

    private final Set<BroadcastListener> listeners = new HashSet<>();

    @Override
    public BroadcastPipeline setPreparedMessage(PreparedMessage preparedMessage) {
        this.preparedMessage = preparedMessage;
        return this;
    }

    @Override
    public BroadcastPipeline setDispatcher(BroadcastDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    @Override
    public BroadcastPipeline setRecordExtractor(RecordExtractor recordsExtractor) {
        this.recordExtractor = recordsExtractor;
        return this;
    }

    @Override
    public BroadcastPipeline addListener(BroadcastListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public Iterable<BroadcastListener> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }
}
