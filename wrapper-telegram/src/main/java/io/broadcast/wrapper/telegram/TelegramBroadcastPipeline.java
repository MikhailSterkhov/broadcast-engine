package io.broadcast.wrapper.telegram;

import com.pengrad.telegrambot.TelegramBot;
import io.broadcast.engine.BroadcastPipeline;
import io.broadcast.engine.announcement.AnnouncementExtractor;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.dispatch.ComplexBroadcastDispatcher;
import io.broadcast.engine.event.BroadcastListener;
import io.broadcast.engine.record.extract.RecordExtractor;
import io.broadcast.engine.scheduler.Scheduler;
import io.broadcast.wrapper.telegram.objects.TelegramMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TelegramBroadcastPipeline implements BroadcastPipeline<Long, TelegramMessage> {
    
    private final BroadcastPipeline<Long, TelegramMessage> internalPipe = BroadcastPipeline.createPipeline(Long.class, TelegramMessage.class);
    private final Set<BroadcastDispatcher<Long, TelegramMessage>> dispatchers = new HashSet<>();
    
    private final TelegramBot telegramBot;

    public TelegramBroadcastPipeline(@NotNull String apiToken) {
        this(new TelegramBot(apiToken));
    }

    public TelegramBroadcastPipeline(@NotNull TelegramBot.Builder builder) {
        this(builder.build());
    }

    public TelegramBroadcastPipeline(@NotNull TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    
    @Override
    public BroadcastPipeline<Long, TelegramMessage> setAnnouncementExtractor(AnnouncementExtractor<TelegramMessage> announcementExtractor) {
        return internalPipe.setAnnouncementExtractor(announcementExtractor);
    }

    @Override
    public BroadcastPipeline<Long, TelegramMessage> setDispatcher(BroadcastDispatcher<Long, TelegramMessage> dispatcher) {
        dispatchers.add(dispatcher);
        return this;
    }

    @Override
    public BroadcastPipeline<Long, TelegramMessage> setRecordExtractor(RecordExtractor<Long> recordsExtractor) {
        return internalPipe.setRecordExtractor(recordsExtractor);
    }

    @Override
    public BroadcastPipeline<Long, TelegramMessage> setScheduler(Scheduler scheduler) {
        return internalPipe.setScheduler(scheduler);
    }

    @Override
    public BroadcastPipeline<Long, TelegramMessage> addListener(BroadcastListener listener) {
        return internalPipe.addListener(listener);
    }

    @Override
    public AnnouncementExtractor<TelegramMessage> getAnnouncementExtractor() {
        return internalPipe.getAnnouncementExtractor();
    }

    @Override
    public BroadcastDispatcher<Long, ?> getDispatcher() {
        if (internalPipe.getDispatcher() == null) {
            Set<BroadcastDispatcher<Long, TelegramMessage>> clone = new HashSet<>(dispatchers);
            clone.add(new TelegramBotDispatcher(telegramBot));
            
            internalPipe.setDispatcher(ComplexBroadcastDispatcher.complex(clone));
        }
        return internalPipe.getDispatcher();
    }

    @Override
    public RecordExtractor<Long> getRecordExtractor() {
        return internalPipe.getRecordExtractor();
    }

    @Override
    public Scheduler getScheduler() {
        return internalPipe.getScheduler();
    }

    @Override
    public Iterable<BroadcastListener> getListeners() {
        return internalPipe.getListeners();
    }
}
