package io.broadcast.example;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import io.broadcast.engine.BroadcastEngine;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.record.map.RecordsMap;
import io.broadcast.engine.scheduler.Scheduler;
import io.broadcast.wrapper.telegram.TelegramBroadcastPipeline;
import io.broadcast.wrapper.telegram.model.NeededStars;
import io.broadcast.wrapper.telegram.model.TelegramMessage;
import io.broadcast.wrapper.telegram.model.Text;
import io.broadcast.wrapper.telegram.model.Multimedia;
import io.broadcast.wrapper.telegram.model.media.Photo;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class TelegramBotBroadcastExample {

    private static final RecordsMap<Long, String> telegramUsersById = RecordsMap.newHashMap();

    public static void main(String[] args) {
        AnnouncementExtractor<TelegramMessage> telegramMessageExtractor
                = AnnouncementExtractor.fromID(Long.class,
                (telegramUserID) -> TelegramMessage.builder()
                        .text(Text.builder()
                                .text("Hello, your current telegram account ID: " + telegramUserID)
                                .newline()
                                .text(" - Your detected username: @" + telegramUsersById.get(telegramUserID))
                                .newline()
                                .text("Please,").bold(" subscribe at bot ").text("or our email: ")
                                .email("itzstonlex@yandex.ru")
                                .build())
                        //.paidMediaStars(NeededStars.unlockByCount(2))
                        .attachPhoto(
                                Multimedia.of(
                                        Photo.fromStream(TelegramBotBroadcastExample.class.getResourceAsStream("/broadcast.png")),
                                        Photo.fromStream(TelegramBotBroadcastExample.class.getResourceAsStream("/broadcast.png"))))
                        .build());

        TelegramBroadcastPipeline broadcastPipeline = TelegramBroadcastPipeline.fromTelegramBot(startTelegramBot())
                .setRecordExtractor(RecordExtractor.mutable(telegramUsersById::toRecordsSet))
                .setAnnouncementExtractor(telegramMessageExtractor)
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
