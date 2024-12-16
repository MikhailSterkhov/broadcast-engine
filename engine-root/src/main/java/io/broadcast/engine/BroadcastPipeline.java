package io.broadcast.engine;

import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.event.BroadcastListener;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.spi.LinkedBroadcastPipeline;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
public interface BroadcastPipeline {

    BroadcastPipeline setPreparedMessage(PreparedMessage preparedMessage);

    BroadcastPipeline setDispatcher(BroadcastDispatcher dispatcher);

    BroadcastPipeline setRecordExtractor(RecordExtractor recordsExtractor);

    BroadcastPipeline addListener(BroadcastListener listener);

    PreparedMessage getPreparedMessage();

    BroadcastDispatcher getDispatcher();

    RecordExtractor getRecordExtractor();

    Iterable<BroadcastListener> getListeners();

    @Contract(" -> new")
    static @NotNull BroadcastPipeline createPipeline() {
        return new LinkedBroadcastPipeline();
    }
}
