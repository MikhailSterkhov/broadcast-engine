package io.broadcast.engine.event;

import io.broadcast.engine.BroadcastEngineException;
import io.broadcast.engine.event.context.*;

public abstract class BroadcastEventAdapter implements BroadcastListener {

    @Override
    public void broadcastStart(BroadcastStartEventContext eventContext) {
        // override this.
    }

    @Override
    public void broadcastEnd(BroadcastEndEventContext eventContext) {
        // override this.
    }

    @Override
    public void broadcastDispatch(BroadcastDispatchEventContext eventContext) {
        // override this.
    }

    @Override
    public void broadcastSchedule(BroadcastScheduleEventContext eventContext) {
        // override this.
    }

    @Override
    public void recordExtracted(RecordExtractedEventContext eventContext) {
        // override this.
    }

    @Override
    public void preparedMessage(PreparedMessageEventContext eventContext) {
        // override this.
    }

    @Override
    public void throwsException(Throwable throwable) {
        // override this.
    }
}
