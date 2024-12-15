package io.broadcast.engine.event;

import io.broadcast.engine.event.context.*;

public interface BroadcastListener {

    void broadcastStart(BroadcastStartEventContext eventContext);

    void broadcastEnd(BroadcastEndEventContext eventContext);

    void broadcastDispatch(BroadcastDispatchEventContext eventContext);

    void broadcastSchedule(BroadcastScheduleEventContext eventContext);

    void recordExtracted(RecordExtractedEventContext eventContext);

    void preparedMessage(PreparedMessageEventContext eventContext);

    void throwsException(Throwable throwable);
}
