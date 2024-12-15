package io.broadcast.engine.event.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class BroadcastScheduleEventContext {

    private final Instant instant;
    private final Duration scheduledDuration;
}
