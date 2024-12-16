package io.broadcast.engine.announcement;

import io.broadcast.engine.record.Record;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class AnnouncementAndID<I, T> extends DecoratedAnnouncement {
    
    private final I id;
    private final T message;

    public Record<I> createRecord() {
        return new Record<>(id);
    }
}
