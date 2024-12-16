package io.broadcast.engine.announcement;

import io.broadcast.engine.record.Record;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class RecordedAnnouncement<I, V> implements Announcement {
    
    private final Record<I> record;
    private final V message;
}
