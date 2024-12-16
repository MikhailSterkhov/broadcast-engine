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
public class AnnouncementAndRecord<I, M> extends DecoratedAnnouncement {
    
    private final Record<I> record;
    private final M message;

    public I getId() {
        return record.getId();
    }
}
