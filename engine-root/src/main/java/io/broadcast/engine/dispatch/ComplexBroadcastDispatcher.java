package io.broadcast.engine.dispatch;

import io.broadcast.engine.Announcement;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@RequiredArgsConstructor
public class ComplexBroadcastDispatcher<I> implements BroadcastDispatcher<I> {

    @Contract("_ -> new")
    public static <I> @NotNull ComplexBroadcastDispatcher<I> complex(Iterable<BroadcastDispatcher<I>> dispatchers) {
        return new ComplexBroadcastDispatcher<>(dispatchers);
    }

    @Contract("_ -> new")
    @SafeVarargs
    public static <I> @NotNull ComplexBroadcastDispatcher<I> complex(BroadcastDispatcher<I>... dispatchers) {
        return new ComplexBroadcastDispatcher<>(Arrays.asList(dispatchers));
    }

    private final Iterable<BroadcastDispatcher<I>> dispatchers;

    @Override
    public void dispatch(@NotNull Announcement<I> announcement) {
        dispatchers.forEach(dispatcher -> dispatcher.dispatch(announcement));
    }
}
