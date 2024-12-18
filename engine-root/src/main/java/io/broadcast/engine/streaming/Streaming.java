package io.broadcast.engine.streaming;

import io.broadcast.engine.streaming.fork.ForkedSession;

import java.util.concurrent.CompletableFuture;

public interface Streaming {

    ForkedSession lastProceedSession();

    ForkedSession forkSessionNow();

    CompletableFuture<ForkedSession> futureFork();
}
