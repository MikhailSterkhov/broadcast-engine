package io.broadcast.engine;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
public class TextMessage {

    private final String subject;
    private final String content;

    public boolean hasSubject() {
        return subject != null;
    }

    public boolean hasContent() {
        return content != null;
    }
}
