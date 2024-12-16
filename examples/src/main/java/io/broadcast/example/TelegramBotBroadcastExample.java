package io.broadcast.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.PreparedMessage;
import io.broadcast.engine.dispatch.ComplexBroadcastDispatcher;
import io.broadcast.engine.dispatch.STDOUTBroadcastDispatcher;
import io.broadcast.engine.record.Record;
import io.broadcast.engine.record.RecordToStringSerializer;
import io.broadcast.engine.record.extract.Extractors;
import io.broadcast.engine.record.map.RecordsMap;
import io.broadcast.engine.scheduler.Scheduler;
import io.broadcast.wrapper.telegram.TelegramBotDispatcher;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class TelegramBotBroadcastExample {

    private static final RecordsMap<Long, String> telegramUsersById = RecordsMap.newHashMap();

    public static void main(String[] args) {
        PreparedMessage<Long> preparedMessage
                = PreparedMessage.serialize(
                        RecordToStringSerializer.single("Announcement"),
                        (record) -> String.format("Hello, @%s, your telegram-id: %d", telegramUsersById.get(record), record.getId()));

        BroadcastPipeline broadcastPipeline = BroadcastPipeline.createPipeline()
                .setDispatcher(ComplexBroadcastDispatcher.complex(
                        new TelegramBotDispatcher(startTelegramBot()),
                        new STDOUTBroadcastDispatcher<>()))
                .setRecordExtractor(Extractors.supplier(telegramUsersById::toRecordsSet))
                .setPreparedMessage(preparedMessage)
                .setScheduler(Scheduler.threadScheduler(2));

        BroadcastEngine broadcastEngine = new BroadcastEngine(broadcastPipeline);
        broadcastEngine.scheduleBroadcastEverytime(Duration.ofSeconds(10));
    }

    private static @NotNull TelegramBot startTelegramBot() {
        TelegramBot telegramBot = new TelegramBot("<your-telegram-bot-token>");
        telegramBot.setUpdatesListener(updates -> {

            for (Update update : updates) {
                Chat chat = update.message().chat();

                telegramUsersById.put(chat.id(), chat.username());

                System.out.println("Added user @" + chat.username());
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        return telegramBot;
    }
}
