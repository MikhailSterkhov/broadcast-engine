package io.broadcast.engine.event.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class BroadcastStartEventContext {

    private final Instant instant;
}
