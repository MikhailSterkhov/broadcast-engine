package io.broadcast.wrapper.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.broadcast.engine.Announcement;
import io.broadcast.engine.TextMessage;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TelegramBotDispatcher<T> implements BroadcastDispatcher<Long, T> {

    private final TelegramBot telegramBot;

    public TelegramBotDispatcher(@NotNull String apiToken) {
        this(new TelegramBot(apiToken));
    }

    public TelegramBotDispatcher(@NotNull TelegramBot.Builder builder) {
        this(builder.build());
    }

    public TelegramBotDispatcher(@NotNull TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void dispatch(@NotNull Announcement<Long, T> announcement) {
        SendMessage messageRequest = createSendMessageRequest(
                announcement.getRecord().getId(),
                announcement.getTextMessage());

        if (messageRequest == null) {
            return;
        }

        SendResponse sendResponse = telegramBot.execute(messageRequest);

        if (!sendResponse.isOk()) {
            int errorCode = sendResponse.errorCode();
            String description = sendResponse.description();

            throw new BroadcastTelegramBotException("Failed dispatch message to telegram bot [errorCode=" + errorCode + ", description=" + description + "]");
        }
    }

    private SendMessage createSendMessageRequest(long chatID, TextMessage textMessage) {
        if (textMessage == null || textMessage.getContent() == null) {
            return null;
        }

        String subjectString = Optional.of(textMessage).map(TextMessage::getSubject).orElse("");
        String contentString = textMessage.getContent();

        String messageString = (subjectString + "\n" + contentString);

        SendMessage sendMessage = new SendMessage(chatID, messageString);

        if (textMessage.hasSubject()) {
            sendMessage = sendMessage.entities(
                    new MessageEntity(MessageEntity.Type.bold, 0, textMessage.getSubject().length()));
        }
        return sendMessage;
    }
}
