package io.broadcast.engine.dispatch;

import io.broadcast.engine.Announcement;
import io.broadcast.engine.TextMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class STDOUTBroadcastDispatcher<I, T> implements BroadcastDispatcher<I, T> {

    @Override
    public void dispatch(@NotNull Announcement<I, T> announcement) {
        TextMessage textMessage = announcement.getTextMessage();

        if (textMessage != null) {
            String content = textMessage.getContent();

            if (content != null) {
                String subject = Optional.ofNullable(textMessage.getSubject()).orElse("<Non-Subject>");
                System.out.printf("%s - %s%n", subject, content);
            }
        }
    }
}
