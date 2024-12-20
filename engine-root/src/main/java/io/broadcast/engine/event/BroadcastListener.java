package io.broadcast.engine.event;

import io.broadcast.engine.event.context.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface BroadcastListener {

    void broadcastStart(BroadcastStartEventContext eventContext);

    void broadcastEnd(BroadcastEndEventContext eventContext);

    void broadcastDispatch(BroadcastDispatchEventContext eventContext);

    void broadcastSchedule(BroadcastScheduleEventContext eventContext);

    void recordExtracted(RecordExtractedEventContext eventContext);

    void preparedMessage(PreparedMessageEventContext eventContext);

    void throwsException(Throwable throwable);

    @Contract(value = " -> new", pure = true)
    static @NotNull BroadcastListener stdout() {
        return new STDOUTBroadcastListener();
    }
}
