package io.broadcast.engine;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import io.broadcast.engine.record.Record;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Announcement<I> {

    private final Record<I> record;
    private final TextMessage textMessage;

    public boolean hasMessage() {
        return textMessage != null;
    }
}
