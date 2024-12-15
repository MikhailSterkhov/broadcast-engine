package io.broadcast.engine.event.context;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import io.broadcast.engine.record.Record;

import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class PreparedMessageEventContext {

    private final Record<?,?> record;
    private final Instant instant;
    private final String text;
    private final boolean isPrepared;
}
