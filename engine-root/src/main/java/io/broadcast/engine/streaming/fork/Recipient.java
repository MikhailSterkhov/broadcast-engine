package io.broadcast.engine.streaming.fork;

import io.broadcast.engine.dispatch.BroadcastDispatcher;

public interface Recipient {

    static Recipient dispatches(BroadcastDispatcher<?, ?> dispatcher) {
        return null;
    }
}
