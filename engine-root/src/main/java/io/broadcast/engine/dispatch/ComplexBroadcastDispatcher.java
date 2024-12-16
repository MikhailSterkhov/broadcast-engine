package io.broadcast.engine.dispatch;

import io.broadcast.engine.announcement.Announcement;
import io.broadcast.engine.record.Record;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

@RequiredArgsConstructor
public class ComplexBroadcastDispatcher<I, A extends Announcement> implements BroadcastDispatcher<I, A> {

    @Contract("_ -> new")
    public static <I, A extends Announcement> @NotNull ComplexBroadcastDispatcher<I, A> complex(Iterable<BroadcastDispatcher<I, A>> dispatchers) {
        return new ComplexBroadcastDispatcher<>(dispatchers);
    }

    @Contract("_ -> new")
    @SafeVarargs
    public static <I, A extends Announcement> @NotNull ComplexBroadcastDispatcher<I, A> complex(BroadcastDispatcher<I, A>... dispatchers) {
        return new ComplexBroadcastDispatcher<>(new ArrayList<>(Arrays.asList(dispatchers)));
    }

    private final Iterable<BroadcastDispatcher<I, A>> dispatchers;

    @Override
    public void dispatch(@NotNull Record<I> record, @NotNull A announcement) {
        dispatchers.forEach(dispatcher -> dispatcher.dispatch(record, announcement));
    }
}
