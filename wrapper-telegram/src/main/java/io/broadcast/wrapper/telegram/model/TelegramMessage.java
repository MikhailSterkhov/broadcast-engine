package io.broadcast.wrapper.telegram.model;

import io.broadcast.engine.announcement.DecoratedAnnouncement;
import io.broadcast.wrapper.telegram.model.media.Photo;
import io.broadcast.wrapper.telegram.model.media.Video;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class TelegramMessage extends DecoratedAnnouncement {

    private final Text text;

    private final NeededStars paidMediaStars;

    private final Multimedia<Photo> attachPhoto;
    private final Multimedia<Video> attachVideo;

    public boolean isOnlyText() {
        return text != null
                && attachPhoto == null
                && attachVideo == null;
    }
}
