package io.broadcast.engine.event;

import io.broadcast.engine.event.context.*;

public class STDOUTBroadcastListener implements BroadcastListener {

    @Override
    public void broadcastStart(BroadcastStartEventContext eventContext) {
        System.out.println(eventContext);
    }

    @Override
    public void broadcastEnd(BroadcastEndEventContext eventContext) {
        System.out.println(eventContext);
    }

    @Override
    public void broadcastDispatch(BroadcastDispatchEventContext eventContext) {
        System.out.println(eventContext);
    }

    @Override
    public void broadcastSchedule(BroadcastScheduleEventContext eventContext) {
        System.out.println(eventContext);
    }

    @Override
    public void recordExtracted(RecordExtractedEventContext eventContext) {
        System.out.println(eventContext);
    }

    @Override
    public void preparedMessage(PreparedMessageEventContext eventContext) {
        System.out.println(eventContext);
    }

    @Override
    public void throwsException(Throwable throwable) {
        throwable.printStackTrace();
    }
}
