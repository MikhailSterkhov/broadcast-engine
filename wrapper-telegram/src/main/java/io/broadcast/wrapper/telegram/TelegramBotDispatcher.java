package io.broadcast.wrapper.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.record.Record;
import io.broadcast.wrapper.telegram.objects.TelegramMessage;
import io.broadcast.wrapper.telegram.objects.Text;
import org.jetbrains.annotations.NotNull;

public class TelegramBotDispatcher implements BroadcastDispatcher<Long, TelegramMessage> {

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
    public void dispatch(@NotNull Record<Long> record, @NotNull TelegramMessage announcement) {
        Text text = announcement.getText();
        if (text == null) {
            return;
        }

        SendMessage sendMessageRequest = new SendMessage(record.getId(), text.getText())
                .entities(text.getMessageEntityList().toArray(new MessageEntity[0]));

        SendResponse sendResponse = telegramBot.execute(sendMessageRequest);

        if (!sendResponse.isOk()) {
            int errorCode = sendResponse.errorCode();
            String description = sendResponse.description();

            throw new BroadcastTelegramBotException("Failed dispatch message to telegram bot [errorCode=" + errorCode + ", description=" + description + "]");
        }
    }
}
