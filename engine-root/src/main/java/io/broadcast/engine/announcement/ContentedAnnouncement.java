package io.broadcast.engine.announcement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class ContentedAnnouncement<T> extends DecoratedAnnouncement {

    private final T subject;
    private final T content;

    public boolean hasSubject() {
        return subject != null;
    }

    public boolean hasContent() {
        return content != null;
    }
}
