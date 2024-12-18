package io.broadcast.engine.streaming.fork;

import java.time.Duration;

public interface Timeline {

    static Timeline every(Duration duration) {
        return null;
    }

    static Timeline replicationMode() {
        return null;
    }
}
