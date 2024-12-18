package io.broadcast.wrapper.telegram;

import com.pengrad.telegrambot.TelegramBot;
import io.broadcast.engine.AbstractBroadcastPipelineWrapper;
import io.broadcast.engine.dispatch.BroadcastDispatcher;
import io.broadcast.engine.dispatch.ComplexBroadcastDispatcher;
import io.broadcast.wrapper.telegram.model.TelegramMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class TelegramBroadcastPipeline
        extends AbstractBroadcastPipelineWrapper<Long, TelegramMessage, TelegramBroadcastPipeline> {

    @Contract("_ -> new")
    public static @NotNull TelegramBroadcastPipeline fromToken(@NotNull String apiToken) {
        return new TelegramBroadcastPipeline(apiToken);
    }

    @Contract("_ -> new")
    public static @NotNull TelegramBroadcastPipeline fromTelegramBot(@NotNull TelegramBot telegramBot) {
        return new TelegramBroadcastPipeline(telegramBot);
    }

    @Contract("_ -> new")
    public static @NotNull TelegramBroadcastPipeline fromTelegramBot(@NotNull TelegramBot.Builder builder) {
        return new TelegramBroadcastPipeline(builder);
    }

    private final Set<BroadcastDispatcher<Long, TelegramMessage>> customDispatchers = new HashSet<>();

    public TelegramBroadcastPipeline(@NotNull String apiToken) {
        this(new TelegramBot(apiToken));
    }

    public TelegramBroadcastPipeline(@NotNull TelegramBot.Builder builder) {
        this(builder.build());
    }

    public TelegramBroadcastPipeline(@NotNull TelegramBot telegramBot) {
        this.customDispatchers.add(new TelegramBotDispatcher(telegramBot));
    }

    @Override
    public TelegramBroadcastPipeline setDispatcher(BroadcastDispatcher<Long, TelegramMessage> dispatcher) {
        customDispatchers.add(dispatcher);
        return this;
    }

    @Override
    public BroadcastDispatcher<Long, TelegramMessage> getDispatcher() {
        return ComplexBroadcastDispatcher.complex(customDispatchers);
    }
}
