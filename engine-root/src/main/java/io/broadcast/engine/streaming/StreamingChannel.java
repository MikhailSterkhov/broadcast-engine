package io.broadcast.engine.streaming;

import io.broadcast.engine.streaming.fork.Recipient;
import io.broadcast.engine.streaming.fork.Source;
import io.broadcast.engine.streaming.fork.Timeline;

public interface StreamingChannel {

    StreamingChannel from(Source source);

    StreamingChannel to(Recipient recipient);

    StreamingChannel at(Timeline timeline);

    Streaming start();
}
