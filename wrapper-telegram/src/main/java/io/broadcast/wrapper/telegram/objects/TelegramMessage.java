package io.broadcast.wrapper.telegram.objects;

import io.broadcast.engine.announcement.DecoratedAnnouncement;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class TelegramMessage extends DecoratedAnnouncement {

    private final Text text;
}
