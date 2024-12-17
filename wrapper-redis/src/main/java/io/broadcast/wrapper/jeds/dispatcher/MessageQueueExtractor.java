package io.broadcast.wrapper.jeds.dispatcher;

import io.broadcast.engine.announcement.ContentedAnnouncement;

@FunctionalInterface
public interface MessageQueueExtractor {

    String extractFrom(ContentedAnnouncement<String> announcement);
}
