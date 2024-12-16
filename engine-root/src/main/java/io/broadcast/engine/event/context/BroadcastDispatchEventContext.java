package io.broadcast.engine.event.context;

import io.broadcast.engine.announcement.Announcement;
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
public class BroadcastDispatchEventContext {

    private final Record<?> record;
    private final Instant instant;
    private final Announcement announcement;
}
