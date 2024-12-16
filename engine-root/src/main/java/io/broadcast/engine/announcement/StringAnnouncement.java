package io.broadcast.engine.announcement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class StringAnnouncement extends DecoratedAnnouncement {

    private final String content;

    public boolean hasContent() {
        return content != null;
    }
}
