package io.broadcast.engine.record;

import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Function;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Record<I, T> {

    @Contract("_, _ -> new")
    public static <I, T> @NotNull Record<I, T> of(@NotNull T entity, @NotNull Function<T, I> idGetter) {
        return new Record<>(idGetter.apply(entity), entity);
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull Record<Integer, T> ofInt(@NotNull T entity, @NotNull Function<T, Integer> idGetter) {
        return of(entity, idGetter);
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull Record<Long, T> ofLong(@NotNull T entity, @NotNull Function<T, Long> idGetter) {
        return of(entity, idGetter);
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull Record<String, T> ofString(@NotNull T entity, @NotNull Function<T, String> idGetter) {
        return of(entity, idGetter);
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull Record<UUID, T> ofUUID(@NotNull T entity, @NotNull Function<T, UUID> idGetter) {
        return of(entity, idGetter);
    }

    private final I id;
    private final T entity;
}
