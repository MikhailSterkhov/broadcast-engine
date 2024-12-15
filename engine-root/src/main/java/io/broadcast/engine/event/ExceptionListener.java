package io.broadcast.engine.event;

public abstract class ExceptionListener extends BroadcastEventAdapter {

    @Override
    public abstract void throwsException(Throwable throwable);
}
