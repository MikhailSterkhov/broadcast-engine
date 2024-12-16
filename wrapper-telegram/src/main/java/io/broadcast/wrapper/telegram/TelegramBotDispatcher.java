package io.broadcast.wrapper.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.broadcast.engine.Announcement;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import org.jetbrains.annotations.NotNull;

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
        SendResponse sendResponse = telegramBot.execute(new SendMessage(
                announcement.getRecord().getId(),
                announcement.getPreparedText()));

        if (!sendResponse.isOk()) {
            int errorCode = sendResponse.errorCode();
            String description = sendResponse.description();

            throw new BroadcastTelegramBotException("Failed dispatch message to telegram bot [errorCode=" + errorCode + ", description=" + description + "]");
        }
    }
}
