package io.broadcast.wrapper.jeds.dispatcher;

import io.broadcast.engine.announcement.ContentedAnnouncement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MessageQueueExtractor {

    @NotNull
    String extractFrom(@NotNull ContentedAnnouncement<String> announcement);

    @Contract(pure = true)
    static @NotNull MessageQueueExtractor separated(String separator) {
        return announcement -> (announcement.getSubject() + separator + announcement.getContent());
    }

    static @NotNull MessageQueueExtractor separated(char separator) {
        return separated(Character.toString(separator));
    }
}
