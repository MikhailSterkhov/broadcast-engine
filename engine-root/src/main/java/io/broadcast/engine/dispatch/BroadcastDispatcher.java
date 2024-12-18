package io.broadcast.engine.dispatch;

import io.broadcast.engine.announcement.Announcement;
import io.broadcast.engine.record.Record;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BroadcastDispatcher<I, A extends Announcement> {

    void dispatch(@NotNull Record<I> record, @NotNull A announcement);

    @Contract("_ -> new")
    static <I, A extends Announcement> @NotNull BroadcastDispatcher<I, A> complex(Iterable<BroadcastDispatcher<I, A>> dispatchers) {
        return ComplexBroadcastDispatcher.complex(dispatchers);
    }

    @Contract("_ -> new")
    @SafeVarargs
    static <I, A extends Announcement> @NotNull BroadcastDispatcher<I, A> complex(BroadcastDispatcher<I, A>... dispatchers) {
        return ComplexBroadcastDispatcher.complex(dispatchers);
    }

    @Contract(value = " -> new", pure = true)
    static <I, A extends Announcement> @NotNull BroadcastDispatcher<I, A> stdout() {
        return new STDOUTBroadcastDispatcher<>();
    }
}
